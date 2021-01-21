package dev.thematrix.tvhk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.leanback.app.BrowseFragment
import androidx.leanback.widget.*
import dev.thematrix.tvhk.MovieList.OLD_SDK_VERSION
import dev.thematrix.tvhk.MovieList.SDK_VER
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import android.util.Base64


class MainFragment : BrowseFragment() {

    private val fbTitleList = mutableListOf<String>()
    private val fbCardImageUrlList = mutableListOf<Int>()
    private val fbVideoUrlList = mutableListOf<String>()
    private val fbFuncList = mutableListOf<String>()

    private lateinit var toast: Toast

    companion object {
        val webInfoMap = mutableMapOf<String, String>()

        
        fun signGdtv(requestUrl: String, timestamp: String): String {
            val method = "GET";
            val secret = "dfkcY1c3sfuw0Cii9DWjOUO3iQy2hqlDxyvDXd1oVMxwYAJSgeB6phO8eW1dfuwX";
            val contentMD5 = "";
            Log.i("GDTVencode", "requestUrl: ${requestUrl}, method: ${method}, timestamp: ${timestamp}, secret: ${secret}")

            val sha256HMAC = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
            sha256HMAC.init(secretKey)
        
            val stringToSigned = "${method}\n${requestUrl}\n${timestamp}\n${contentMD5}";
            Log.i("GDTVencode", "stringToSigned: ${stringToSigned}")

            val signedByteArray = sha256HMAC.doFinal(stringToSigned.toByteArray())
            Log.i("GDTVencode", "signedByteArray: ${signedByteArray}")

            val signedBase64 = Base64.encodeToString(signedByteArray, Base64.NO_WRAP)
            Log.i("GDTVencode", "signedBase64: ${signedBase64}")

            return signedBase64;
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setupUIElements()

        loadRows()
        setupEventListeners()

        getWebInfo()
    }

    private var getWebInfoRetry = 10
    private fun getWebInfo() {
        Thread(Runnable {
            val client = OkHttpClient()
            val url = URL("https://telegra.ph/TVSTHK-04-03")
            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body()!!.string()

                if (responseBody != "") {
                    val resultHtml = Regex("(?<=<blockquote>)(.*?)(?=</blockquote>)").findAll(responseBody)
                    if (resultHtml.count() > 0) {
                        resultHtml.forEach {
                            val result = it.value
                            val line = result.split("=")
                            webInfoMap[line[0]] = line[1]

                            //Temp
                            if (line.size > 2)
                                webInfoMap[line[0]] += "=" + line[2]
                        }

                        //Other Operations
                        this.activity.runOnUiThread {
                            fixChannel()
                            defaultPlay()
                            addGdtv()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("getWebInfo", e.message)
                showToast("Error: getWebInfo - " + e.message)
                if (getWebInfoRetry > 0) {
                    getWebInfoRetry--
                    getWebInfo()
                } else {
                    showToast("Cannot get Web Info!")
                    this.activity.runOnUiThread {
                        defaultPlay()
                        addGdtv()
                    }
                }
            } finally {

            }
        }).start()
    }

    private fun fixChannel() {
        this.fixChinaSportsMovie("CCTV5", 5);
        this.fixChinaSportsMovie("CCTV新聞", 1);

        if (webInfoMap.contains("isUtvCustomLink") && webInfoMap["isUtvCustomLink"] == "true") {
            val utvLink1 = webInfoMap["utvLink1"]
            val utvLink2 = webInfoMap["utvLink2"]
            for (title in arrayOf("有線財經資訊台", "有線新聞台", "有線綜合娛樂台")) {
                val currentMovie = MovieList.list[MovieList.TITLE.indexOf(title)]
                val channelCode = currentMovie.func.split('_')[2]
                currentMovie.videoUrl =
                    if (SDK_VER >= OLD_SDK_VERSION)
                        "$utvLink1/livehls/${channelCode}/index.m3u8" +
                                "#$utvLink2/livehls/${channelCode}/index.m3u8"
                    else
                        "$utvLink1/livehls/${channelCode}/02.m3u8" +
                                "#$utvLink2/livehls/${channelCode}/02.m3u8" +
                                "#$utvLink1/livehls/${channelCode}/01.m3u8" +
                                "#$utvLink2/livehls/${channelCode}/01.m3u8"

                if (webInfoMap["utvExo"] == "true") {
                    currentMovie.func = "utvExo"
                    currentMovie.exo = true
                }
            }
        }


        for (i in 0..3) {
            val customMovie = MovieList.list[MovieList.TITLE.indexOf("自選1") + i]
            customMovie.videoUrl = webInfoMap[customMovie.func]?:""
        }
    }


    private fun fixChinaSportsMovie(name: String, size: Int) {
        val chinaSportLink1 = webInfoMap["chinaSportLink1"]
        val chinaSportLink2 = webInfoMap["chinaSportLink2"]
        for (i in 0..size) {
            val chinaSportMovie = MovieList.list[MovieList.TITLE.indexOf(name) + i]
            val func = chinaSportMovie.func.split("_")[1]
            val isHD = !func.endsWith("cctvxw")

            chinaSportMovie.videoUrl = chinaSportMovie.videoUrl +
                    "http://${chinaSportLink1}/live/program/live/${func}/1300000/mnf.m3u8" +
                    "#http://${chinaSportLink2}/live/program/live/${func}/1300000/mnf.m3u8"

            if (isHD) {
                chinaSportMovie.videoUrl = chinaSportMovie.videoUrl +
                        "#http://${chinaSportLink1}/live/program/live/${func}/2300000/mnf.m3u8" +
                        "#http://${chinaSportLink2}/live/program/live/${func}/2300000/mnf.m3u8" +
                        "#http://${chinaSportLink1}/live/program/live/${func}/4000000/mnf.m3u8" +
                        "#http://${chinaSportLink2}/live/program/live/${func}/4000000/mnf.m3u8"
            }
        }
    }

    private fun addOlympicChannel() {
        Thread(Runnable {
            val titleList = mutableListOf<String>()
            val cardImageUrlList = mutableListOf<Int>()
            val videoUrlList = mutableListOf<String>()
            val funcList = mutableListOf<String>()

            //Get live link
            val client = OkHttpClient()
            val url = URL("https://www.olympicchannel.com/en/api/v1/d3vp/epgchannels/linear/live-channels")
            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body()!!.string()

                if (responseBody != "") {
                    val resultArray = JSONArray(responseBody)
                    for (i in 0..(resultArray.length() - 1)) {
                        val item : JSONObject = resultArray.getJSONObject(i)
                        val title : String = item.getString("title")
                        val src : String = item.getString("src")
                        val additionalOptions = item.getJSONObject("additionalOptions")
                        val channelId = additionalOptions.getString("channelId")


                        val link = if (channelId == "OC1") "https://ott-live.olympicchannel.com/out/u/OC1.m3u8" else src
                        titleList.add(title)
                        cardImageUrlList.add(R.drawable.olympic)
                        videoUrlList.add(link.replace(".m3u8", "_1.m3u8")
                                + "#" + link.replace(".m3u8", "_2.m3u8")
                                + "#" + link.replace(".m3u8", "_3.m3u8")
                                + "#" + link.replace(".m3u8", "_4.m3u8")
                                + "#" + link.replace(".m3u8", "_5.m3u8") )
                        funcList.add("")
                    }


                    //Add to movie list
                    if (titleList.count() > 0) {
                        titleList.add("SKIP")
                        cardImageUrlList.add(0)
                        videoUrlList.add("SKIP")
                        funcList.add("SKIP")

                        MovieList.TITLE.addAll(MovieList.SPORTS_INDEX, titleList)
                        MovieList.CARD_IMAGE_URL.addAll(MovieList.SPORTS_INDEX, cardImageUrlList)
                        MovieList.VIDEO_URL.addAll(MovieList.SPORTS_INDEX, videoUrlList)
                        MovieList.FUNC.addAll(MovieList.SPORTS_INDEX, funcList)

                        // Update UI
                        this.activity.runOnUiThread {
                            MovieList.updateList(MovieList.SPORTS_INDEX)
                            addOlympicChannelRows()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("addOlympicChannel", e.message)
                showToast("Error: addOlympicChannel - " + e.message)
            } finally {
                this.activity.runOnUiThread { addOnccLive() }
            }
        }).start()
    }

    private fun addOnccLive() {
        Thread(Runnable {
            val titleList = mutableListOf<String>()
            val cardImageUrlList = mutableListOf<Int>()
            val videoUrlList = mutableListOf<String>()
            val funcList = mutableListOf<String>()

            //Get live link
            val client = OkHttpClient()
            val url = URL("https://tv.on.cc/js/live/live_feed_v3.js")
            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body()!!.string()

                if (responseBody != "") {
                    val resultArray = JSONArray(responseBody)
                    for (i in 0..(resultArray.length() - 1)) {
                        val item: JSONObject = resultArray.getJSONObject(i)
                        val name: String = item.getString("name")
                        val payItem: Boolean = item.getBoolean("payItem")
                        if (!payItem) {
                            val cameraArray = item.getJSONArray("camera")
                            for (i in 0..(cameraArray.length() - 1)) {
                                val camera: JSONObject = cameraArray.getJSONObject(i)
                                val title = camera.getJSONArray("title").join(" ")
                                val linkArray = camera.getJSONArray("signal_key")
                                val linkList = mutableListOf<String>()
                                for (i in (linkArray.length() - 1) downTo 0) { //The first one may be https only
                                    val link: String = linkArray.getString(i)
                                    linkList.add(link)
                                }

                                titleList.add("$name $title")
                                cardImageUrlList.add(R.drawable.oncc)
                                videoUrlList.add(linkList.joinToString("#"))
                                funcList.add("oncc")
                            }
                        }
                    }


                    //Add to movie list
                    if (titleList.count() > 0) {
                        titleList.add("SKIP")
                        cardImageUrlList.add(0)
                        videoUrlList.add("SKIP")
                        funcList.add("SKIP")

                        MovieList.TITLE.addAll(MovieList.NEWS_INDEX, titleList)
                        MovieList.CARD_IMAGE_URL.addAll(MovieList.NEWS_INDEX, cardImageUrlList)
                        MovieList.VIDEO_URL.addAll(MovieList.NEWS_INDEX, videoUrlList)
                        MovieList.FUNC.addAll(MovieList.NEWS_INDEX, funcList)

                        // Update UI
                        this.activity.runOnUiThread {
                            MovieList.updateList(MovieList.NEWS_INDEX)
                            addOnccRows()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("addOnccLive", e.message)
                showToast("Error: addOnccLive - " + e.message)
            } finally {
                this.activity.runOnUiThread { addFbLive() }
            }
        }).start()
    }

    private fun addFbLive() {
        Thread(Runnable {
            //Get live link
            getFbLiveVideo("standnewshk", "立場直播", R.drawable.fb_standnewshk)
            getFbLiveVideo("hk.nextmedia", "蘋果日報", R.drawable.fb_hk_nextmedia)
            getFbLiveVideo("inmediahknet", "香港獨立媒體網", R.drawable.fb_inmediahk)
            getFbLiveVideo("hk01wemedia", "HK01", R.drawable.fb_hk01wemedia)
            getFbLiveVideo("truthmediahk", "TMHK", R.drawable.fb_tmhk_org)
            getFbLiveVideo("onccnews", "東網", R.drawable.fb_onccnews)
            getFbLiveVideo("icable.news", "有線新聞 i-Cable News", R.drawable.fb_icablenews)
            getFbLiveVideo("now.comNews", "Now News - 新聞", R.drawable.fb_now_comnews)
            getFbLiveVideo("RTHKVNEWS", "香港電台視像新聞 RTHK VNEWS", R.drawable.fb_rthkvnews)
            getFbLiveVideo("atvhongkong", "ATV亞視數碼媒體", R.drawable.fb_hkatv)
            getFbLiveVideo("cuspcusp", "中大學生報", R.drawable.fb_cuhk)
            getFbLiveVideo("socrec", "SocREC 社會記錄頻道", R.drawable.fb_socrec)
            getFbLiveVideo("fans.hkgolden", "HKGolden", R.drawable.fb_fans_hkgolden)
            getFbLiveVideo("CupidProducer", "丘品創作", R.drawable.fb_cupidproducer)
            getFbLiveVideo("cvrhk", "全民記者", R.drawable.fb_cvrhk)
            getFbLiveVideo("onccsport", "東網體育", R.drawable.fb_onccsport)


            try {
                //Add to movie list
                if (fbTitleList.count() > 0) {
                    fbTitleList.add("SKIP")
                    fbCardImageUrlList.add(0)
                    fbVideoUrlList.add("SKIP")
                    fbFuncList.add("SKIP")

                    MovieList.TITLE.addAll(MovieList.NEWS_INDEX, fbTitleList)
                    MovieList.CARD_IMAGE_URL.addAll(MovieList.NEWS_INDEX, fbCardImageUrlList)
                    MovieList.VIDEO_URL.addAll(MovieList.NEWS_INDEX, fbVideoUrlList)
                    MovieList.FUNC.addAll(MovieList.NEWS_INDEX, fbFuncList)

                    // Update UI
                    this.activity.runOnUiThread {
                        MovieList.updateList(MovieList.NEWS_INDEX)
                        addFbRows()
                    }
                }
            } catch (e: Exception) {
                Log.e("addFbLive", e.message)
                showToast("Error: addFbLive - " + e.message)
            } finally {
                //this.activity.runOnUiThread {  }
            }
        }).start()
    }

    private fun getFbLiveVideo(page: String, pageName: String, image: Int) {
        //HTTP request
        val client = OkHttpClient()
        val url = URL("https://m.facebook.com/$page/video_grid/")
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body()!!.string()


            //Get Link
            val resultList = Regex("\\?src&amp;.*?id=\\d*").findAll(responseBody)
            if (resultList.count() > 0) {
                resultList.forEach {
                    val result = it.value
                    val id = Regex("\\d*$").find(result)?.value ?: ""
                    val liveLink = getFbLiveLink("https://www.facebook.com/$page/videos/$id/")

                    if (liveLink != "") {
                        fbTitleList.add(pageName)
                        fbCardImageUrlList.add(image)
                        fbVideoUrlList.add(liveLink)
                        fbFuncList.add("fb")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("getFbLiveVideo", e.message)
            showToast("Error: getFbLiveVideo($pageName) - " + e.message)
        }
    }

    private fun getFbLiveLink(videoUrl: String): String {
        //HTTP request
        val client = OkHttpClient()
        val url = URL("https://www.facebook.com/plugins/video.php?href=$videoUrl")
        val request = Request.Builder()
            .url(url)
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3902.4 Safari/537.36")
            .get()
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body()!!.string()

            //Get Link
            val isLive = Regex("\"is_live_stream\":true").find(responseBody)?.value ?: ""
            if (isLive != "") {
                val hdSrc = Regex("\"hd_src\":\".*?\"").find(responseBody)?.value ?: ""
                var hdResult = Regex("https.*[^`\"]").find(hdSrc)?.value ?: ""
                hdResult = hdResult.replace("\\", "")
                return hdResult
            }
        } catch (e: Exception) {
            Log.e("getFbLiveLink", e.message)
            showToast("Error: getFbLiveLink($videoUrl) - " + e.message)
        }
        return ""
    }


    private fun addYahooTWTV() {
        Thread(Runnable {
            val titleList = mutableListOf<String>()
            val cardImageUrlList = mutableListOf<Any>()
            val videoUrlList = mutableListOf<String>()
            val funcList = mutableListOf<String>()


            val builder = OkHttpClient.Builder()
            builder.addInterceptor { chain ->
                val original = chain.request()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4115.1 Safari/537.36")

                val request = requestBuilder.build()
                chain.proceed(request)
            }


            val client = builder.build()
            val url = URL("https://tw.mobi.yahoo.com/tv/sports-video/")
            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body()!!.string()

                if (responseBody != "") {
                    val uuidList = Regex("(?<=ListVideoItem-)(.*?)(?=\")").findAll(responseBody)

                    if (uuidList.count() > 0) {
                        uuidList.forEachIndexed { index, matchResult ->
                            if (index < 5) {
                                val uuid = matchResult.value

                                Log.i("Yahoo UUID", uuid)
                                val jsonUrl = URL("https://video-api.yql.yahoo.com/v1/video/sapi/streams/$uuid?region=TW")
                                val jsonRequest = Request.Builder()
                                    .url(jsonUrl)
                                    .get()
                                    .build()
                                val jsonResponse = client.newCall(jsonRequest).execute()
                                val jsonResponseBody = jsonResponse.body()!!.string()

                                if (jsonResponseBody != "") {
                                    val mediaObjArray = JSONObject(jsonResponseBody)
                                        .getJSONObject("query")
                                        .getJSONObject("results")
                                        .getJSONArray("mediaObj")

                                    if (mediaObjArray != null && mediaObjArray.length() > 0) {
                                        val mediaObj = mediaObjArray.getJSONObject(0)
                                        val statusCode = mediaObj.getJSONObject("status").getString("code")

                                        if (statusCode == "100") {
                                            val meta = mediaObj.getJSONObject("meta")
                                            val title = meta.getString("title")
                                            val thumbnail =
                                                if (meta.has("thumbnail")) meta.getString("thumbnail") else null
                                            val streamArray = mediaObj.getJSONArray("streamProfiles")

                                            val widthLinkMap = mutableMapOf<Int, String>()
                                            for (i in 0 until streamArray.length()) {
                                                val item: JSONObject = streamArray.getJSONObject(i)
                                                val width = item.getInt("width")
                                                val host: String = item.getString("host")
                                                val path: String = item.getString("path")
                                                widthLinkMap[width] = host + path
                                            }
                                            val videoList = widthLinkMap.toSortedMap(reverseOrder()).values

                                            //Add to movie list
                                            titleList.add(title)
                                            cardImageUrlList.add(thumbnail ?: R.drawable.tw_yahootv)
                                            funcList.add("")
                                            videoUrlList.add(videoList.joinToString("#"))
                                        }
                                    }
                                }
                            }
                        }

                        //Add to movie list
                        if (titleList.count() > 0) {
                            titleList.add("SKIP")
                            cardImageUrlList.add(0)
                            videoUrlList.add("SKIP")
                            funcList.add("SKIP")

                            MovieList.TITLE.addAll(MovieList.SPORTS_INDEX, titleList)
                            MovieList.CARD_IMAGE_URL.addAll(MovieList.SPORTS_INDEX, cardImageUrlList)
                            MovieList.VIDEO_URL.addAll(MovieList.SPORTS_INDEX, videoUrlList)
                            MovieList.FUNC.addAll(MovieList.SPORTS_INDEX, funcList)

                            // Update UI
                            this.activity.runOnUiThread {
                                MovieList.updateList(MovieList.SPORTS_INDEX)
                                addYahooTWTVRows()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("addYahooTWTV", e.message)
                showToast("Error: addYahooTWTV - " + e.message)
            } finally {
                this.activity.runOnUiThread { addOnccLive() }
            }
        }).start()
    }


    private fun addGdtv() {
        Thread(Runnable {
            val titleList = mutableListOf<String>()
            val cardImageUrlList = mutableListOf<Any>()
            val videoUrlList = mutableListOf<String>()
            val funcList = mutableListOf<String>()

            //Get live link
            val client = OkHttpClient()
            val url = URL("https://gdtv-api.gdtv.cn:7443/api/tv/v1/tvChannel") 
            val timestamp = System.currentTimeMillis().toString()
            val request = Request.Builder()
                .url(url)
                .addHeader("x-itouchtv-ca-key", webInfoMap["gdtvKey"]!!)
                .addHeader("x-itouchtv-ca-signature", signGdtv(url.toString(), timestamp))
                .addHeader("x-itouchtv-ca-timestamp", timestamp)
                .addHeader("x-itouchtv-client", "WEB_PC")
                .addHeader("Content-Type", "application/json")
                .get()
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body()!!.string()

                if (responseBody != "") {
                    val resultArray = JSONArray(responseBody)
                    for (i in 0..(resultArray.length() - 1)) {
                        val item: JSONObject = resultArray.getJSONObject(i)
                        val pk: Int = item.getInt("pk")

                        //Only TV
                        if (pk !in 85..92) {
                            val name: String = item.getString("name")
                            val avatarUrl = R.drawable.gdtv //item.getString("avatarUrl")
                            val playUrlJson: String = JSONObject(item.getString("playUrl")).getString("hd")
                            //val playUrl: String = "" // Get later, cuz this link is fake. JSONObject(playUrlJson).getString("hd")


                            titleList.add(name)
                            cardImageUrlList.add(avatarUrl)
                            videoUrlList.add(playUrlJson)

                            funcList.add("gdtv_" + pk)
                        }

                    }


                    //Add to movie list
                    val index = MovieList.TITLE.indexOf("綜合頻道")
                    if (titleList.count() > 0) {
                        titleList.add("SKIP")
                        cardImageUrlList.add(0)
                        videoUrlList.add("SKIP")
                        funcList.add("SKIP")

                        MovieList.TITLE.addAll(index, titleList)
                        MovieList.CARD_IMAGE_URL.addAll(index, cardImageUrlList)
                        MovieList.VIDEO_URL.addAll(index, videoUrlList)
                        MovieList.FUNC.addAll(index, funcList)

                        // Update UI
                        this.activity.runOnUiThread {
                            MovieList.updateList(index)
                            addGdtvRows(index)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("addGdtv", e.message)
                showToast("Error: addGdtv - " + e.message)
            } finally {
                this.activity.runOnUiThread { addYahooTWTV() }
            }
        }).start()
    }


    private fun defaultPlay() {
        var default = 2
        if (webInfoMap.containsKey("defaultChannel"))
            default = webInfoMap["defaultChannel"]!!.toInt()

        val intent = Intent(activity, PlaybackActivity::class.java)
        intent.putExtra(DetailsActivity.MOVIE, MovieList.list[default])
        startActivity(intent)
    }

    private fun setupUIElements() {
        title = getString(R.string.app_name)
        badgeDrawable = activity.resources.getDrawable(R.drawable.transparentbanner)
        toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT)
//        headersState = BrowseFragment.HEADERS_HIDDEN
//        isHeadersTransitionOnBackEnabled = false
    }

    private fun loadRows() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        lateinit var header: HeaderItem
        var listRowAdapter = ArrayObjectAdapter(cardPresenter)
        var headerCount = 0
        for (i in 0 until MovieList.list.count()) {
            if (MovieList.list[i].title == "SKIP") {
                header = HeaderItem(i.toLong(), MovieList.CATEGORY[headerCount++])
                if(listRowAdapter.size() > 0){
                    rowsAdapter.add(ListRow(header, listRowAdapter))
                }

                listRowAdapter = ArrayObjectAdapter(cardPresenter)
            }
            else
            {
                listRowAdapter.add(MovieList.list[i])
            }
        }

        if(listRowAdapter.size() > 0){
            rowsAdapter.add(ListRow(header, listRowAdapter))
        }

        adapter = rowsAdapter
    }



    private fun addFbRows() {
        val rowsAdapter : ArrayObjectAdapter = adapter as ArrayObjectAdapter
        val cardPresenter = CardPresenter()

        var listRowAdapter = ArrayObjectAdapter(cardPresenter)
        var i = MovieList.NEWS_INDEX
        while (MovieList.list[i].title != "SKIP")
        {
            listRowAdapter.add(MovieList.list[i])
            i++
        }

        if(listRowAdapter.size() > 0){
            val header = HeaderItem(MovieList.NEWS_INDEX.toLong(), "Facebook")
            rowsAdapter.add(MovieList.NEWS_CATEGORY_INDEX, ListRow(header, listRowAdapter))
        }
    }

    private fun addOnccRows() {
        val rowsAdapter : ArrayObjectAdapter = adapter as ArrayObjectAdapter
        val cardPresenter = CardPresenter()

        var listRowAdapter = ArrayObjectAdapter(cardPresenter)
        var i = MovieList.NEWS_INDEX
        while (MovieList.list[i].title != "SKIP")
        {
            listRowAdapter.add(MovieList.list[i])
            i++
        }

        if(listRowAdapter.size() > 0){
            val header = HeaderItem(MovieList.NEWS_INDEX.toLong(), "Oncc")
            rowsAdapter.add(MovieList.NEWS_CATEGORY_INDEX, ListRow(header, listRowAdapter))
        }
    }

    private fun addOlympicChannelRows() {
        val rowsAdapter : ArrayObjectAdapter = adapter as ArrayObjectAdapter
        val cardPresenter = CardPresenter()

        var listRowAdapter = ArrayObjectAdapter(cardPresenter)
        var i = MovieList.SPORTS_INDEX
        while (MovieList.list[i].title != "SKIP")
        {
            listRowAdapter.add(MovieList.list[i])
            i++
        }

        if(listRowAdapter.size() > 0){
            val header = HeaderItem(MovieList.SPORTS_INDEX.toLong(), "奧運")
            rowsAdapter.add(MovieList.SPORTS_CATEGORY_INDEX, ListRow(header, listRowAdapter))
        }
    }

    private fun addYahooTWTVRows() {
        val rowsAdapter : ArrayObjectAdapter = adapter as ArrayObjectAdapter
        val cardPresenter = CardPresenter()

        var listRowAdapter = ArrayObjectAdapter(cardPresenter)
        var i = MovieList.SPORTS_INDEX
        while (MovieList.list[i].title != "SKIP")
        {
            listRowAdapter.add(MovieList.list[i])
            i++
        }

        if(listRowAdapter.size() > 0){
            val header = HeaderItem(MovieList.SPORTS_INDEX.toLong(), "Yahoo台灣")
            rowsAdapter.add(MovieList.SPORTS_CATEGORY_INDEX, ListRow(header, listRowAdapter))
        }
    }


    private fun addGdtvRows(index: Int) {
        val rowsAdapter : ArrayObjectAdapter = adapter as ArrayObjectAdapter
        val cardPresenter = CardPresenter()

        var listRowAdapter = ArrayObjectAdapter(cardPresenter)
        var i = index
        while (MovieList.list[i].title != "SKIP")
        {
            listRowAdapter.add(MovieList.list[i])
            i++
        }

        if(listRowAdapter.size() > 0){
            val header = HeaderItem(index.toLong(), "廣東電視")
            rowsAdapter.add(MovieList.CATEGORY.indexOf("廣州電視"), ListRow(header, listRowAdapter))
        }
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is Movie) {
                val intent = Intent(activity, PlaybackActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, item)
                startActivity(intent)
            } else if (item is String) {
                showToast(item)
            }
        }
    }



    private fun showToast(msg: String) {
        this.activity.runOnUiThread {
            toast.setText(msg)
            toast.show()
        }
    }
}
