package dev.thematrix.tvhk

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.android.volley.*
import com.android.volley.toolbox.*
import org.json.JSONArray
import org.json.JSONObject
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class PlaybackActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init
        setUpNetwork()
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //First time channel (on click
        if (savedInstanceState == null) {
            val (id, title, _, _, videoUrl, func, exo) = this?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as Movie
            currentVideoID = id

            prepareAndChangePlayer(videoUrl, title, func, exo, false)
        }
    }
    private fun setUpNetwork(){
        requestQueue = RequestQueue(NoCache(), BasicNetwork(HurlStack())).apply {
            start()
        }
    }

    private fun prepareAndChangePlayer(videoUrl:String, title: String, func: String, exo: Boolean, playDirectly: Boolean)
    {
        when {
            videoUrl == "" -> getVideoUrl(title, func, exo, playDirectly)
            playDirectly -> playByPlayer(videoUrl.split("#")[currentSourceIndex])
            else -> changePlayer(videoUrl.split("#")[currentSourceIndex], exo)
        }

        toast.setText("正在轉台到 " + title + "[" + currentSourceIndex + "]")
        toast.show()
    }


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        lateinit var direction: String

        if(
            event.keyCode == KeyEvent.KEYCODE_CHANNEL_UP ||
            event.keyCode == KeyEvent.KEYCODE_DPAD_UP ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_REWIND ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_SKIP_BACKWARD ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD ||
            event.keyCode == KeyEvent.KEYCODE_NAVIGATE_PREVIOUS ||
            event.keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT
        ){
            direction = "PREVIOUS"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN ||
            event.keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_SKIP_FORWARD ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_STEP_FORWARD ||
            event.keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT ||
            event.keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT
        ){
            direction = "NEXT"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_DPAD_LEFT
        ){
            direction = "LEFT"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
            (event.keyCode == KeyEvent.KEYCODE_BACK && event.isLongPress)
        ){
            direction = "RIGHT"
        }else{
            return super.dispatchKeyEvent(event)
        }



        // Find correct channel / source
        if(event.action == KeyEvent.ACTION_DOWN){
            val list = MovieList.list
            var videoId = currentVideoID
            val channelCount = list.count()

            // Change channel
            if(direction.equals("PREVIOUS")){
                videoId--

                //Fix Underflow
                if(videoId < 0) {
                    videoId = channelCount - 2 //Skipping SKIP
                }
                else if (list[videoId].title == "SKIP")
                {
                    videoId--
                }
                currentSourceIndex = 0
            }else if(direction.equals("NEXT")) {
                videoId++

                //Fix Overflow
                if(videoId >= (channelCount - 1)){ //Skipping SKIP
                    videoId = 0
                }
                else if (list[videoId].title == "SKIP")
                {
                    videoId++
                }
                currentSourceIndex = 0
            }

            // New Channel
            val item = list[videoId]
            currentVideoID = videoId

            //Change Source
            if (item.videoUrl != "") {
                var sourceCount = item.videoUrl.split("#").size
                if (direction.equals("LEFT")) {
                    currentSourceIndex = (currentSourceIndex - 1) % sourceCount
                } else if (direction.equals("RIGHT")) {
                    currentSourceIndex = (currentSourceIndex + 1) % sourceCount
                }
            }


            // Play
            prepareAndChangePlayer(item.videoUrl, item.title, item.func, item.exo, item.exo == isCurrentExo)
        }

        return true
    }

    private fun playByPlayer(videoUrl:String)
    {
        if (isCurrentExo)
            PlaybackVideoExoFragment().playVideo(videoUrl)
        else
            PlaybackVideoFragment().playVideo(videoUrl)
    }


    private fun getVideoUrl(title: String, ch: String, exo: Boolean, play: Boolean) {
        requestQueue.cancelAll(this)

        lateinit var url: String

        if(ch.equals("viutv99") || ch.equals("nowtv332") || ch.equals("nowtv331")){
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
                    try {
                        if (play)
                            playByPlayer(mediaUrl)
                        else {
                            changePlayer(mediaUrl, exo)
                        }

                    }catch (exception: Exception){
                        showPlaybackErrorMessage(title, mediaUrl)
                    }
                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title, "")
                }
            )

            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

            requestQueue.add(jsonObjectRequest)
        }else if(ch.equals("nowtv630")){
            url = "https://sports.now.com/VideoCheckOut?pid=webch630_4&service=NOW360&type=channel"

            val stringRequest = object: StringRequest(
                Method.GET,
                url,
                Response.Listener { response ->
                    try {
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

                        if (url != "") {
                            if (play)
                                playByPlayer(url)
                            else {
                                changePlayer(url, exo)
                            }
                        } else {
                            showPlaybackErrorMessage(title, url)
                        }
                    }catch (exception: Exception){
                        showPlaybackErrorMessage(title, url)
                    }
                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title, url)
                }
            ){
                override fun getRetryPolicy(): RetryPolicy {
                    return DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val params =  mutableMapOf<String, String>()

//                    params.put("Referer", "https://sports.now.com/home/630")
                    params.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0.1; TVHK Build/35.0.A.1.282)")

                    return params
                }
            }
            requestQueue.add(stringRequest)

        }else if(ch.equals("cabletv109") || ch.equals("cabletv110")){
            url = "https://mobileapp.i-cable.com/iCableMobile/API/api.php"

            val stringRequest = object: StringRequest(
                Method.POST,
                url,
                Response.Listener { response ->
                    val mediaUrl = JSONObject(JSONObject(response).get("result").toString()).get("stream").toString()
                    try {
                        if (play)
                            playByPlayer(mediaUrl)
                        else
                            changePlayer(mediaUrl, exo)

                    }catch (exception: Exception){
                        showPlaybackErrorMessage(title, mediaUrl)
                    }
                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title, "")
                }
            ){
                override fun getRetryPolicy(): RetryPolicy {
                    return DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val params =  mutableMapOf<String, String>()

                    params.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0.1; AndroidTV Build/35.0.A.1.282)")

                    return params
                }

                override fun getParams(): MutableMap<String, String> {
                    val params =  mutableMapOf<String, String>()

                    if(ch.equals("cabletv109")){
                        params.put("channel_no", "_9")
                        params.put("vlink", "_9")
                    }else if(ch.equals("cabletv110")){
                        params.put("channel_no", "_10")
                        params.put("vlink", "_10")
                    }

                    params.put("device", "aos_mobile")
                    params.put("method", "streamingGenerator2")
                    params.put("quality", "h")
                    params.put("uuid", "")
                    params.put("is_premium", "0")
                    params.put("network", "wifi")
                    params.put("platform", "1")
                    params.put("deviceToken", "")
                    params.put("appVersion", "6.3.4")
                    params.put("market", "G")
                    params.put("lang", "zh_TW")
                    params.put("version", "6.3.4")
                    params.put("osVersion", "23")
                    params.put("channel_id", "106")
                    params.put("deviceModel", "AndroidTV")
                    params.put("type", "live")

                    return params
                }
            }

            requestQueue.add(stringRequest)
        }
    }


    private fun showPlaybackErrorMessage(title: String, mediaUrl: String){
        toast.setText("$title 暫時未能播放，請稍候再試。")
        toast.show()
        if (mediaUrl != "")
            playByPlayer(mediaUrl)
    }

    private fun changePlayer(mediaUrl: String, exo: Boolean)
    {
        this?.intent?.putExtra("videoUrl", mediaUrl)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, if (exo) PlaybackVideoExoFragment() else PlaybackVideoFragment())
            .commit()
    }

    companion object {
        var currentVideoID = -1
        var currentSourceIndex = 0
        internal var isCurrentExo = false
        lateinit var toast: Toast
        lateinit var requestQueue: RequestQueue
    }
}
