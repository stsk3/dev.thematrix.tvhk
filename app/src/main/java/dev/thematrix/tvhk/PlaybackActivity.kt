package dev.thematrix.tvhk

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.volley.*
import com.android.volley.toolbox.*
import dev.thematrix.tvhk.MainFragment.Companion.webInfoMap
import org.json.JSONArray
import org.json.JSONObject
import org.xml.sax.InputSource
import java.io.IOException
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


class PlaybackActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init
        setUpNetwork()
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //First time channel (on click)
        if (savedInstanceState == null) {
            currentMovie = this?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as Movie
            currentVideoID = currentMovie.id
            currentSourceIndex = 0

            prepareAndChangePlayer(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        requestQueue.cancelAll{ true }
    }

    private fun setUpNetwork(){
        //Header NO redirect
        val hurlStack = object : HurlStack() {
            @Throws(IOException::class) override fun createConnection(url: URL): HttpURLConnection {
                val connection = super.createConnection(url)
                connection.instanceFollowRedirects = false
                return connection
            }
        }

        //Volley
        requestQueue = RequestQueue(NoCache(), BasicNetwork(hurlStack)).apply {
            start()
        }
    }

    private fun prepareAndChangePlayer(playDirectly: Boolean)
    {
        val videoUrl = currentMovie.videoUrl
        val title = currentMovie.title
        retryGetLink = defaultRetryGetLinkNum


        when {
            videoUrl == "" || (currentSourceIndex == 0 && videoUrl.startsWith("#"))
                -> getVideoUrl(playDirectly)
            playDirectly -> playByPlayer(videoUrl)
            else -> changePlayer(videoUrl)
        }

        val sourceText = if (videoUrl.split("#").size > 1) ", Source $currentSourceIndex" else ""

        toast.setText("正在轉台到 $title$sourceText")
        toast.show()
    }


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        lateinit var direction: String

        if(
            event.keyCode == KeyEvent.KEYCODE_DPAD_UP
        ){
            direction = "PREVIOUS"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
            (event.keyCode == KeyEvent.KEYCODE_BACK && event.isLongPress)
        ){
            direction = "NEXT"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_DPAD_LEFT
        ){
            direction = "LEFT"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
        ){
            direction = "RIGHT"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_MENU
        ){
            direction = "MENU"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_PAGE_UP ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
        ){
            direction = "RW"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_PAGE_DOWN ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
        ){
            direction = "FF"
        }else{
            return super.dispatchKeyEvent(event)
        }



        // Find correct channel / source
        if(event.action == KeyEvent.ACTION_DOWN && !isDownloadingChannelInfo) {
            if (direction == "MENU") {
                if (isCurrentExo)
                    (currentPlayerFragment as PlaybackVideoExoFragment).trackSelectionDialog(this)
            } else if (direction == "RW" || direction == "FF") {
                if (isCurrentExo)
                    (currentPlayerFragment as PlaybackVideoExoFragment).seek(direction == "FF")
                else
                    (currentPlayerFragment as PlaybackVideoFragment).seek(direction == "FF")
            } else {
                val list = MovieList.list
                var videoId = currentVideoID
                val channelCount = list.count()

                // Change channel
                val skipChannelList = "^SKIP$".toRegex()
                if (direction == "PREVIOUS" || direction == "NEXT") {
                    if (direction == "PREVIOUS") {
                        videoId--

                        //Fix Underflow
                        if (videoId < 0) {
                            videoId = channelCount - 2 //Skipping SKIP
                        } else if (list[videoId].title.contains(skipChannelList)) {
                            videoId--
                        }
                        currentSourceIndex = 0
                    } else if (direction == "NEXT") {
                        videoId++

                        //Fix Overflow
                        if (videoId >= (channelCount - 1)) { //Skipping SKIP
                            videoId = 0
                        } else if (list[videoId].title.contains(skipChannelList)) {
                            videoId++
                        }
                        currentSourceIndex = 0
                    }

                    // New Channel
                    val item = list[videoId]
                    currentVideoID = videoId
                    currentMovie = item

                    // Play
                    prepareAndChangePlayer(item.exo == isCurrentExo)
                }

                //Change Source
                else if (direction == "LEFT" || direction == "RIGHT") {
                    val item = list[videoId]
                    val sourceCount = item.videoUrl.split("#").size
                    if (item.videoUrl != "" && sourceCount > 1) {
                        if (direction == "LEFT") {
                            currentSourceIndex = (currentSourceIndex - 1) % sourceCount
                            if (currentSourceIndex < 0)
                                currentSourceIndex = sourceCount - 1
                        } else if (direction == "RIGHT") {
                            currentSourceIndex = (currentSourceIndex + 1) % sourceCount
                        }

                        if (isCurrentExo)
                            (currentPlayerFragment as PlaybackVideoExoFragment).videoSeek(direction == "RIGHT")
                        else
                            prepareAndChangePlayer(item.exo == isCurrentExo)

                    } else {
                        toast.setText("只有一個Source")
                        toast.show()
                    }
                }
            }
        }

        return true
    }

    private fun playByPlayer(videoUrl:String)
    {
        val fixRatio = currentMovie.fixRatio

        if (isCurrentExo)
            (currentPlayerFragment as PlaybackVideoExoFragment).playVideo(videoUrl, fixRatio)
        else
            (currentPlayerFragment as PlaybackVideoFragment).playVideo(videoUrl.split("#")[currentSourceIndex], fixRatio)
    }


    private fun getVideoUrl(play: Boolean) {
        val title: String = currentMovie.title
        val ch: String = currentMovie.func

        isDownloadingChannelInfo = true


        requestQueue.cancelAll(this)

        lateinit var url: String

        if(ch.equals("viutv99") || ch.equals("nowtv332") || ch.equals("nowtv331") || ch.equals("nowtv630")){
            val params = JSONObject()

            if(ch.equals("viutv99")){
                url = "https://api.viu.now.com/p8/2/getLiveURL"

                params.put("channelno", "099")

                params.put("deviceId", "AndroidTV")
                params.put("deviceType", "5")
            }else{
                url = "https://hkt-mobile-api.nowtv.now.com/09/1/getLiveURL"

                if(ch.equals("nowtv332")){
                    params.put("channelno", "332")
                }else if(ch.equals("nowtv331")){
                    params.put("channelno", "331")
                }else if (ch.equals("nowtv630")){
                    params.put("channelno", "630")
                }

                params.put("audioCode", "")
            }

            params.put("callerReferenceNo", "")
            params.put("format", "HLS")
            params.put("mode", "prod")

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                Response.Listener { response ->
                    var mediaUrl: String = JSONArray(JSONObject(JSONObject(response.get("asset").toString()).get("hls").toString()).get("adaptive").toString()).get(0).toString()

                    this.play(mediaUrl, play)
                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title, play)
                }
            )

            jsonObjectRequest.retryPolicy = getCustomerRetryPolicy()

            requestQueue.add(jsonObjectRequest)
        }else if(ch.equals("nowtv630")){
            //Deprecated
            url = "https://sports.now.com/VideoCheckOut?pid=webch630_4&service=NOW360&type=channel"

            val stringRequest = object: StringRequest(
                Method.GET,
                url,
                Response.Listener { response ->

                    var url = ""

                    val factory = DocumentBuilderFactory.newInstance()
                    val builder = factory.newDocumentBuilder()
                    val source = InputSource(StringReader(response))
                    val document = builder.parse(source)
                    val nodes = document.getElementsByTagName("RESULT").item(0).childNodes

                    for (i in 0 until nodes.length) {
                        if (nodes.item(i).nodeName == "html5streamurlhq") {
                            url = nodes.item(i).textContent
                            break
                        }
                    }

                    this.play(url, play)
                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title, play)
                }
            ){
                override fun getRetryPolicy(): RetryPolicy {
                    return getCustomerRetryPolicy()
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val params =  mutableMapOf<String, String>()

//                    params.put("Referer", "https://sports.now.com/home/630")
                    params.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0.1; TVHK Build/35.0.A.1.282)")

                    return params
                }
            }
            requestQueue.add(stringRequest)

        } else if(ch.startsWith("wowgua")){

            var url = ""
            if (ch.equals("wowgua_nbatv"))
                url = "http://teslagram.com/sports2world_api.php"
            else if (ch.equals("wowgua_ch301"))
                url = "http://teslagram.com/live/channel_301.m3u8"
            else if (ch.equals("wowgua_ch108"))
                url = "http://teslagram.com/live/channel_108.m3u8"
            else if (ch.equals("wowgua_ch109"))
                url = "http://teslagram.com/live/channel_109.m3u8"
            else if (ch.equals("wowgua_utv_c_plus"))
                url = "http://teslagram.com/live/channel_utv_c_plus.m3u8"
            else if (ch.equals("wowgua_utv_racing"))
                url = "http://teslagram.com/live/channel_utv_racing.m3u8"

            val stringRequest = object: StringRequest(
                Method.GET,
                url,
                Response.Listener { },
                Response.ErrorListener { error ->
                    if (error.networkResponse != null && error.networkResponse.statusCode == 302) {
                        val url = error.networkResponse.headers["Location"].toString()

                        this.play(url, play)
                    }
                    else
                    {
                        showPlaybackErrorMessage(title, play)
                    }
                }
            ){
                override fun getRetryPolicy(): RetryPolicy {
                    return getCustomerRetryPolicy()
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val params =  mutableMapOf<String, String>()
                    params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3977.4 Safari/537.36")

                    return params
                }
            }
            requestQueue.add(stringRequest)

        } else if(ch.startsWith("livetv")){

            var url = "http://livetv.dnsfor.me/API.php?channel=" + ch.split("_")[1]
            

            val stringRequest = object: StringRequest(
                Method.GET,
                url,
                Response.Listener { response ->
                    var mediaUrl: String = JSONObject(response).getString("asset")

                    this.play(mediaUrl, play)
                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title, play)
                }
            ){
                override fun getRetryPolicy(): RetryPolicy {
                    return getCustomerRetryPolicy()
                }
            }
            requestQueue.add(stringRequest)

        } else if(ch.startsWith("gdtvxxx")) {
            val url = "https://gdtv-api.gdtv.cn:7443/api/tv/v1/tvChannel/" + ch.substring(5)
            val stringRequest = object: StringRequest(
                Method.GET,
                url,
                Response.Listener { response ->
                    var mediaUrlJson: String = JSONObject(response).getString("playUrl")
                    val mediaUrl: String = JSONObject(mediaUrlJson).getString("hd")

                    this.play(mediaUrl, play)
                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title, play)
                }
            ){
                override fun getRetryPolicy(): RetryPolicy {
                    return getCustomerRetryPolicy()
                }
                
                override fun getHeaders(): MutableMap<String, String> {
                    val timestamp = System.currentTimeMillis().toString()

                    val params =  mutableMapOf<String, String>()
                    params.put("x-itouchtv-ca-key", webInfoMap["gdtvKey"]!!)
                    params.put("x-itouchtv-ca-signature", MainFragment.Companion.signGdtv(url.toString(), timestamp))
                    params.put("x-itouchtv-ca-timestamp", timestamp)
                    params.put("x-itouchtv-client", "WEB_PC")
                    params.put("Content-Type", "application/json")

                    return params
                }
            }
            requestQueue.add(stringRequest)

        } else if(ch.startsWith("gztv")) {

            val url = "https://www.gztv.com/gztv/api/tv/" + ch.substring(5)
            val stringRequest = object: StringRequest(
                Method.GET,
                url,
                Response.Listener { response ->
                    val result = Regex("data='.*'").find(response)
                    if (result != null)
                    {
                        var url = Regex("https.*?'").find(result.value)?.value ?: ""
                        url = url.substring(0, url.length - 1)

                        this.play(url, play)
                    }
                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title, play)
                }
            ){
                override fun getRetryPolicy(): RetryPolicy {
                    return getCustomerRetryPolicy()
                }
            }
            requestQueue.add(stringRequest)

        } else if(ch.startsWith("ggiptv")) {
            val dataList = ch.split("_")

            val url = "http://${webInfoMap["ggiptvHost"]}/?act=play&tid=" + dataList[1] +"&id=" + dataList[2]
            val stringRequest = object: StringRequest(
                Method.GET,
                url,
                Response.Listener { response ->
                    val result = Regex("<option value=\".*?\"").find(response)
                    if (result != null)
                    {
                        var url = Regex("http.*?\"").find(result.value)?.value ?: ""
                        url = url.substring(0, url.length - 1) + if (dataList.size > 3) "&p=${dataList[3]}" else ""
                        this.play(url, play)
                    }

                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title, play)
                }
            ){
                override fun getRetryPolicy(): RetryPolicy {
                    return getCustomerRetryPolicy()
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val params =  mutableMapOf<String, String>()
                    params.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4101.0 Mobile Safari/537.36")

                    return params
                }
            }
            requestQueue.add(stringRequest)

        }
    }

    private fun play(mediaUrl: String, play: Boolean) {
        if (mediaUrl == "" && retryGetLink > 0) {
            retryGetLink--
            this.getVideoUrl(play)
            toast.setText("重試取得播放址中...")
            toast.show()
        } else {

            var originalVideoUrl = currentMovie.videoUrl
            val title = currentMovie.title

            isDownloadingChannelInfo = false

            //Error in getting url, play url directly
            if (mediaUrl == "" && originalVideoUrl.startsWith("#"))
                originalVideoUrl = originalVideoUrl.removePrefix("#")


            try {
                if (play)
                    playByPlayer(mediaUrl + originalVideoUrl)
                else {
                    changePlayer(mediaUrl + originalVideoUrl)
                }

            } catch (exception: Exception) {
                showPlaybackErrorMessage(title, play)
            }
        }
    }


    private fun showPlaybackErrorMessage(title: String, play: Boolean){
        toast.setText("$title 暫時未能播放，請稍候再試。")
        toast.show()

        this.play("", play)
    }

    private fun changePlayer(mediaUrl: String)
    {
        val fixRatio = currentMovie.fixRatio
        val exo = currentMovie.exo
        currentPlayerFragment = if (exo) PlaybackVideoExoFragment() else PlaybackVideoFragment()

        this?.intent?.putExtra("videoUrl", mediaUrl)
        this?.intent?.putExtra("fixRatio", fixRatio)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, currentPlayerFragment)
            .commit()
    }

    private fun getCustomerRetryPolicy(): DefaultRetryPolicy {
        return DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * if (currentMovie.videoUrl != "") 3 else 6, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    }

    private val defaultRetryGetLinkNum = 2
    private var retryGetLink = defaultRetryGetLinkNum
    private var isDownloadingChannelInfo = false
    private lateinit var currentPlayerFragment: Fragment

    companion object {
        var currentVideoID = -1
        var currentSourceIndex = 0
        const val seekInterval = 5000
        internal var isCurrentExo = false
        internal lateinit var currentMovie: Movie
        lateinit var toast: Toast
        lateinit var requestQueue: RequestQueue
        const val SYSTEM_UI_FLAG = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}
