package dev.thematrix.tvhk

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.leanback.app.BrowseFragment
import androidx.leanback.widget.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


class MainFragment : BrowseFragment() {

    private val fbTitleList = mutableListOf<String>()
    private val fbCardImageUrlList = mutableListOf<Int>()
    private val fbVideoUrlList = mutableListOf<String>()
    private val fbFuncList = mutableListOf<String>()

    companion object {
        val webInfoMap = mutableMapOf<String, String>()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setupUIElements()

        loadRows()
        setupEventListeners()

        getWebInfo()
        addOlympicChannel()
    }

    private fun getWebInfo() {
        Thread(Runnable {
            val client = OkHttpClient()
            val url = URL("https://telegra.ph/TVSTHK-04-03")
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            val response = client.newCall(request).execute()
            val responseBody = response.body()!!.string()

            if (responseBody != "") {
                val resultHtml = Regex("(?<=<blockquote>)(.*?)(?=</blockquote>)").findAll(responseBody)
                if (resultHtml.count() > 0)
                {
                    resultHtml.forEach {
                        val result = it.value
                        val line = result.split("=")
                        webInfoMap[line[0]] = line[1]
                    }

                    fixChannel()
                    defaultPlay()
                }
            }
        }).start()
    }

    private fun fixChannel() {
        if (webInfoMap.containsKey("cableNewsDown") && !webInfoMap["cableNewsDown"]!!.toBoolean()) {
            val cableNewsMovie = MovieList.list[MovieList.TITLE.indexOf("有線新聞台")]
            cableNewsMovie.videoUrl = webInfoMap["cableNews"]!!
            cableNewsMovie.func = ""
            cableNewsMovie.exo = android.os.Build.VERSION.SDK_INT >= 19
        }

        for (i in 0..2) {
            val chinaSportMovie = MovieList.list[MovieList.TITLE.indexOf("五星體育") + i]
            chinaSportMovie.videoUrl = "http://${webInfoMap["chinaSportLink1"]}/live/program/live/${chinaSportMovie.func.split("_")[1]}/2300000/mnf.m3u8#http://${webInfoMap["chinaSportLink2"]}/live/program/live/${chinaSportMovie.func.split("_")[1]}/2300000/mnf.m3u8"
        }

        for (i in 0..3) {
            val customMovie = MovieList.list[MovieList.TITLE.indexOf("自選1") + i]
            customMovie.videoUrl = webInfoMap[customMovie.func]?:""
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
            val url = URL("https://www.olympicchannel.com/en/api/v1/d3vp/epg/live")
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
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
                        MovieList.updateList(false)
                        addOlympicChannelRows()
                    }
                }

                addOnccLive()
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
            val response = client.newCall(request).execute()
            val responseBody = response.body()!!.string()

            if (responseBody != "") {
                val resultArray = JSONArray(responseBody)
                for (i in 0..(resultArray.length() - 1)) {
                    val item : JSONObject = resultArray.getJSONObject(i)
                    val name : String = item.getString("name")
                    val payItem : Boolean = item.getBoolean("payItem")
                    if (!payItem)
                    {
                        val cameraArray = item.getJSONArray("camera")
                        for (i in 0..(cameraArray.length() - 1)) {
                            val camera : JSONObject = cameraArray.getJSONObject(i)
                            val title = camera.getJSONArray("title").join(" ")
                            val linkArray = camera.getJSONArray("signal_key")
                            val linkList = mutableListOf<String>()
                            for (i in (linkArray.length() - 1) downTo 0) { //The first one may be https only
                                val link : String = linkArray.getString(i)
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
                        MovieList.updateList(true)
                        addOnccRows()
                    }
                }

                addFbLive()
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
            getFbLiveVideo("icablenews", "有線新聞 i-Cable News", R.drawable.fb_icablenews)
            getFbLiveVideo("now.comNews", "Now News - 新聞", R.drawable.fb_now_comnews)
            getFbLiveVideo("RTHKVNEWS", "香港電台視像新聞 RTHK VNEWS", R.drawable.fb_rthkvnews)
            getFbLiveVideo("atvhongkong", "ATV亞視數碼媒體", R.drawable.fb_hkatv)
            getFbLiveVideo("cuspcusp", "中大學生報", R.drawable.fb_cuhk)
            getFbLiveVideo("socrec", "SocREC 社會記錄頻道", R.drawable.fb_socrec)
            getFbLiveVideo("fans.hkgolden", "HKGolden", R.drawable.fb_fans_hkgolden)
            getFbLiveVideo("CupidProducer", "丘品創作", R.drawable.fb_cupidproducer)
            getFbLiveVideo("cvrhk", "全民記者", R.drawable.fb_cvrhk)
            getFbLiveVideo("onccsport", "東網體育", R.drawable.fb_onccsport)


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
                    MovieList.updateList(true)
                    addFbRows()
                }
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
        val response = client.newCall(request).execute()
        val responseBody = response.body()!!.string()


        //Get Link
        val resultList = Regex("\\?src&amp;.*?id=\\d*").findAll(responseBody)
        if (resultList.count() > 0)
        {
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
        val response = client.newCall(request).execute()
        val responseBody = response.body()!!.string()

        //Get Link
        val isLive = Regex("\"is_live_stream\":true").find(responseBody)?.value ?: ""
        if (isLive != "")
        {
            val hdSrc = Regex("\"hd_src\":\".*?\"").find(responseBody)?.value ?: ""
            var hdResult = Regex("https.*[^`\"]").find(hdSrc)?.value ?: ""
            hdResult = hdResult.replace("\\", "")
            return hdResult
        }
        return ""
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
                Toast.makeText(activity, item, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
