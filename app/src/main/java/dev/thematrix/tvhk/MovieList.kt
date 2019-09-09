package dev.thematrix.tvhk

object MovieList {
    const val FB_INDEX = 7
    const val FB_CATEGORY_INDEX = 1

    val CATEGORY = arrayOf(
        "News",
        "CableTV",
        "Now TV",
        "RTHK TV",
        "TDM",
        "FOXSports"
    )

    val TITLE = mutableListOf(
        "有線直播台",
        "有線新聞台",
        "now新聞台",
        "now直播台",
        "港台電視32",
        "HK01",
        "SKIP",
        "香港開電視",
        "SKIP",
        "now630",
        "ViuTV",
        "SKIP",
        "港台電視31",
        "SKIP",
        "澳視體育",
        "澳視澳門",
        "澳視資訊",
        "澳視高清",
        "澳視衛星",
        "澳視葡文",
        "SKIP",
        "FS1",
        "FS2",
        "FS3",
        "SKIP"
    )

    val DESCRIPTION = mutableListOf(
        "畫面比例可能不符合你的電視",
        "",
        "",
        "",
        "",
        "",
        "SKIP",
        "",
        "SKIP",
        "",
        "",
        "SKIP",
        "",
        "SKIP",
        "",
        "",
        "",
        "",
        "",
        "",
        "SKIP",
        "",
        "",
        "",
        "SKIP"
    )

    val CARD_IMAGE_URL = mutableListOf(
        R.drawable.cablelivenews,
        R.drawable.cablenews,
        R.drawable.nowtv332,
        R.drawable.nowtv331,
        R.drawable.rthktv32,
        R.drawable.fb_hk01wemedia,
        0,
        R.drawable.opentv,
        0,
        R.drawable.nowtv630,
        R.drawable.viutv,
        0,
        R.drawable.rthktv31,
        0,
        R.drawable.tdmsport,
        R.drawable.tdmmacau,
        R.drawable.tdminfo,
        R.drawable.tdmhd,
        R.drawable.tdmsat,
        R.drawable.tdmportugal,
        0,
        R.drawable.foxsports,
        R.drawable.foxsports,
        R.drawable.foxsports,
        0
    )

    val VIDEO_URL = mutableListOf(
        "",
        "",
        "",
        "",
        "https://www.rthk.hk/feeds/dtt/rthktv32_https.m3u8",
        "http://live.cdn.hk01.com/origin/smil:01news.smil/playlist.m3u8",
        "SKIP",
        "http://media.fantv.hk/m3u8/archive/channel2_stream1.m3u8",
        "SKIP",
        "",
        "",
        "SKIP",
        "https://www.rthk.hk/feeds/dtt/rthktv31_https.m3u8",
        "SKIP",
        "http://live1.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8",
        "SKIP",
        "http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/index_4700_av-p.m3u8#http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/index_2700_av-p.m3u8#http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/index_1500_av-p.m3u8",
        "http://foxsports07-i.akamaihd.net/hls/live/716920/live04/index.m3u8",
        "http://foxsports07-i.akamaihd.net/hls/live/716919/live03/index.m3u8",
        "SKIP"
    )

    val FUNC = mutableListOf(
        "cabletv110",
        "cabletv109",
        "nowtv332",
        "nowtv331",
        "rthk32",
        "",
        "SKIP",
        "fantv",
        "SKIP",
        "nowtv630",
        "viutv99",
        "SKIP",
        "rthk31",
        "SKIP",
        "",
        "",
        "",
        "",
        "",
        "",
        "SKIP",
        "fox",
        "fox",
        "fox",
        "SKIP"
    )

    val list: MutableList<Movie> by lazy {
        setupMovies()
    }

    fun updateList() {
        count = FB_INDEX

        val newMovieCount = TITLE.count() - list.count()

        //Fix index
        list.forEachIndexed { index, movie ->
            if (index >= FB_INDEX)
            {
                movie.id += newMovieCount
            }
        }

        //ADD new
        for (i in 0 until newMovieCount)
        {
            val movie = buildMovieInfo(
                TITLE[count],
                DESCRIPTION[count],
                CARD_IMAGE_URL[count],
                VIDEO_URL[count],
                FUNC[count]
            )
            list.add(count - 1, movie)
        }
    }

    private var count: Int = 0

    private fun setupMovies(): MutableList<Movie> {


        val list = TITLE.indices.map {
            buildMovieInfo(
                TITLE[it],
                DESCRIPTION[it],
                CARD_IMAGE_URL[it],
                VIDEO_URL[it],
                FUNC[it]
            )
        }

        return list.toMutableList()
    }

    private fun buildMovieInfo(
        title: String,
        description: String,
        cardImageUrl: Int,
        videoUrl: String,
        func: String
    ): Movie {
        val movie = Movie()
        movie.id = count++
        movie.title = title
        movie.description = description
        movie.cardImageUrl = cardImageUrl
        movie.videoUrl = videoUrl
        movie.func = func
        movie.exo = func in arrayOf("viutv99", "nowtv332", "nowtv331", "nowtv630", "fantv", "rthk31", "rthk32", "fox", "fb")


        return movie
    }
}
