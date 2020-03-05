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

    private val default = 2
    private val titleList = mutableListOf<String>()
    private val descriptionList = mutableListOf<String>()
    private val cardImageUrlList = mutableListOf<Int>()
    private val videoUrlList = mutableListOf<String>()
    private val funcList = mutableListOf<String>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setupUIElements()

        loadRows()
        setupEventListeners()
        defaultPlay()

        addOnccLive()
    }

    private fun addOnccLive() {
        Thread(Runnable {
            val titleList = mutableListOf<String>()
            val descriptionList = mutableListOf<String>()
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
                            for (i in 0..(linkArray.length() - 1)) {
                                val link : String = linkArray.getString(i)
                                linkList.add(link.replace("https", "http"))
                            }

                            titleList.add("$name $title")
                            descriptionList.add("")
                            cardImageUrlList.add(R.drawable.oncc)
                            videoUrlList.add(linkList.joinToString("#"))
                            funcList.add("oncc")
                        }
                    }
                }


                //Add to movie list
                if (titleList.count() > 0) {
                    titleList.add("SKIP")
                    descriptionList.add("SKIP")
                    cardImageUrlList.add(0)
                    videoUrlList.add("SKIP")
                    funcList.add("SKIP")

                    MovieList.TITLE.addAll(MovieList.FB_INDEX, titleList)
                    MovieList.DESCRIPTION.addAll(MovieList.FB_INDEX, descriptionList)
                    MovieList.CARD_IMAGE_URL.addAll(MovieList.FB_INDEX, cardImageUrlList)
                    MovieList.VIDEO_URL.addAll(MovieList.FB_INDEX, videoUrlList)
                    MovieList.FUNC.addAll(MovieList.FB_INDEX, funcList)

                    // Update UI
                    this.activity.runOnUiThread {
                        MovieList.updateList()
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
            getFbLiveVideo("TMHK.ORG", "TMHK", R.drawable.fb_tmhk_org)
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
            if (titleList.count() > 0) {
                titleList.add("SKIP")
                descriptionList.add("SKIP")
                cardImageUrlList.add(0)
                videoUrlList.add("SKIP")
                funcList.add("SKIP")

                MovieList.TITLE.addAll(MovieList.FB_INDEX, titleList)
                MovieList.DESCRIPTION.addAll(MovieList.FB_INDEX, descriptionList)
                MovieList.CARD_IMAGE_URL.addAll(MovieList.FB_INDEX, cardImageUrlList)
                MovieList.VIDEO_URL.addAll(MovieList.FB_INDEX, videoUrlList)
                MovieList.FUNC.addAll(MovieList.FB_INDEX, funcList)

                // Update UI
                this.activity.runOnUiThread {
                    MovieList.updateList()
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
                    titleList.add(pageName)
                    descriptionList.add("")
                    cardImageUrlList.add(image)
                    videoUrlList.add(liveLink)
                    funcList.add("fb")
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
        var i = MovieList.FB_INDEX
        while (MovieList.list[i].title != "SKIP")
        {
            listRowAdapter.add(MovieList.list[i])
            i++
        }

        if(listRowAdapter.size() > 0){
            val header = HeaderItem(MovieList.FB_INDEX.toLong(), "Facebook")
            rowsAdapter.add(MovieList.FB_CATEGORY_INDEX, ListRow(header, listRowAdapter))
        }
    }

    private fun addOnccRows() {
        val rowsAdapter : ArrayObjectAdapter = adapter as ArrayObjectAdapter
        val cardPresenter = CardPresenter()

        var listRowAdapter = ArrayObjectAdapter(cardPresenter)
        var i = MovieList.FB_INDEX
        while (MovieList.list[i].title != "SKIP")
        {
            listRowAdapter.add(MovieList.list[i])
            i++
        }

        if(listRowAdapter.size() > 0){
            val header = HeaderItem(MovieList.FB_INDEX.toLong(), "Oncc")
            rowsAdapter.add(MovieList.FB_CATEGORY_INDEX, ListRow(header, listRowAdapter))
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
