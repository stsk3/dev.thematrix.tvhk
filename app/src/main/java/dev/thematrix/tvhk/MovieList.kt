package dev.thematrix.tvhk

object MovieList {

    val SDK_VER = android.os.Build.VERSION.SDK_INT
    const val OLD_SDK_VERSION = 19
    const val NEWS_INDEX = 7
    const val NEWS_CATEGORY_INDEX = 1
    const val SPORTS_INDEX = 17
    const val SPORTS_CATEGORY_INDEX = 2

    val CATEGORY = arrayOf(
        "新聞",
        "體育", //1
        "有線電視", //2
        "RTHK",
        "亞洲電視",
        "其他新聞",
        "澳門",
        "廣州電視", //!!
        "自選"
    )

    val TITLE = mutableListOf(
        "有線財經資訊台", //!!
        "有線新聞台", //!!
        "now新聞台",
        "now直播台",
        "港台電視32",
        "HK01",
        "SKIP",
        "now630", //7
        "澳門體育",
        "CCTV5", //!!
        "CCTV5+",
        "魅力足球",
        "新視覺HD",
        "五星體育",
        "勁爆體育",
        "先鋒乒羽",
        "SKIP",
        "香港開電視", //17
        "有線綜合娛樂台", //!!
        "SKIP",
        "港台電視31",
        "SKIP",
        "香港追擊搜",
        "鳳凰衛視香港台",
        "鳳凰衛視資訊台",
        "香港衛視",
        "亞旅衛視",
        "SKIP",
        "民視新聞",
        "澳門資訊",
        "CCTV新聞", //!!
        "CCTV1",
        "SKIP",
        "澳視澳門",
        "澳門綜藝",
        "澳門-MACAU",
        "澳視葡文",
        "SKIP",
        "綜合頻道", //!!
        "競賽頻道",
        "新聞頻道",
        "影視頻道",
        "南國都市(4K)",
        "法治頻道",
        "SKIP",
        "自選1", //!!
        "自選2",
        "自選Exo1",
        "自選Exo2",
        "SKIP"
    )

    val CARD_IMAGE_URL = mutableListOf<Any>(
        R.drawable.cablefinance,
        R.drawable.cablenews,
        R.drawable.nowtv332,
        R.drawable.nowtv331,
        R.drawable.rthktv32,
        R.drawable.fb_hk01wemedia,
        0,
        R.drawable.nowtv630,
        R.drawable.tdmsport,
        R.drawable.cctv5,
        R.drawable.cctv5plus,
        R.drawable.sport_yule,
        R.drawable.sport_newying,
        R.drawable.sport_5star,
        R.drawable.sport_gingbao,
        R.drawable.sport_tabletennisbadminton,
        0,
        R.drawable.opentv,
        R.drawable.cableentertainment,
        0,
        R.drawable.rthktv31,
        0,
        R.drawable.atv,
        R.drawable.atv_phk,
        R.drawable.atv_pinfo,
        R.drawable.atv_ws,
        R.drawable.atv_tws,
        0,
        R.drawable.tw_ftvn,
        R.drawable.tdminfo,
        R.drawable.cctv13,
        R.drawable.cctv1,
        0,
        R.drawable.tdmmacau,
        R.drawable.tdmhd,
        R.drawable.tdmsat,
        R.drawable.tdmportugal,
        0,
        R.drawable.gz_zhonghe,
        R.drawable.gz_jingsai,
        R.drawable.gz_xinwen,
        R.drawable.gz_yingshi,
        R.drawable.gz_nanguodushi,
        R.drawable.gz_jingji,
        0,
        R.drawable.custom,
        R.drawable.custom,
        R.drawable.custom,
        R.drawable.custom,
        0
    )

    val VIDEO_URL = mutableListOf(
        "",
        "",
        "",
        "",
        "https://www.rthk.hk/feeds/dtt/rthktv32_https.m3u8",
        "http://live.cdn.hk01.com/origin/smil:01news.smil/playlist.m3u8#" +
                "http://live2.cdn.hk01.com/origin/smil:Sudden01.smil/playlist.m3u8#" +
                "http://live.cdn.hk01.com/origin/smil:EventBox001.smil/playlist.m3u8",
        "SKIP",
        "",
        "http://live1.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#" +
                "http://live2.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#" +
                "http://live3.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#" +
                "http://live4.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8",
        "http://ottrrs.hl.chinamobile.com/PLTV/88888888/224/3221225591/index.m3u8#",
        "http://ottrrs.hl.chinamobile.com/PLTV/88888888/224/3221225603/index.m3u8#",
        "",
        "",
        "",
        "",
        "http://play.ggiptv.com:13164/v/hunantv.php?id=329&type=.m3u8",
        "SKIP",
        "http://media.fantv.hk/m3u8/archive/channel2.m3u8",
        "",
        "SKIP",
        "https://www.rthk.hk/feeds/dtt/rthktv31_https.m3u8",
        "SKIP",
        "",
        "",
        "",
        "",
        "",
        "SKIP",
        "http://210.61.56.23/hls/ftvtv/index.m3u8#" +
                "http://6.mms.vlog.xuite.net/hls/ftvtv/index.m3u8#" +
                "http://1.mms.vlog.xuite.net/hls/ftvtv/index.m3u8",
        "http://live1.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#" +
                "http://live2.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#" +
                "http://live3.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#" +
                "http://live4.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8",
        "http://ottrrs.hl.chinamobile.com/PLTV/88888888/224/3221225599/index.m3u8#",
        "http://ottrrs.hl.chinamobile.com/PLTV/88888888/224/3221225587/index.m3u8#",
        "SKIP",
        "http://live1.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#" +
                "http://live2.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#" +
                "http://live3.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#" +
                "http://live4.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#" +
                "http://live2.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#" +
                "http://live3.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#" +
                "http://live4.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#" +
                "http://live2.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#" +
                "http://live3.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#" +
                "http://live4.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#" +
                "http://live2.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#" +
                "http://live3.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#" +
                "http://live4.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8",
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
        "",
        "SKIP"
    )

    val FUNC = mutableListOf(
        "livetv_6_MOB-NGW", //"wowgua_ch108",
        "livetv_5_MOB-SCC", //"wowgua_ch109",
        "nowtv332",
        "nowtv331",
        "rthk",
        "",
        "SKIP",
        "nowtv630",
        "",
        "chinaSport_cctv5hd",
        "chinaSport_cctv5phd",
        "chinaSport_mlyyhd",
        "chinaSport_xsjhd",
        "chinaSport_ssty",
        "chinaSport_jbtyhd",
        "",
        "SKIP",
        "fantv",
        "livetv_7_MOB-CN", //"wowgua_ch301",
        "SKIP",
        "rthk",
        "SKIP",
        "livetv_14",
        "livetv_16",
        "livetv_21",
        "livetv_15",
        "livetv_24",
        "SKIP",
        "tw",
        "",
        "chinaSport_cctvxw",
        "chinaSport_cctv1hd",
        "SKIP",
        "",
        "",
        "",
        "",
        "SKIP",
        "gztv_zhonghe",
        "gztv_jingsai",
        "gztv_xinwen",
        "gztv_yingshi",
        "gztv_shenghuo",
        "gztv_fazhi",
        "SKIP",
        "custom1",
        "custom2",
        "exoCustom1",
        "exoCustom2",
        "SKIP"
    )

    val list: MutableList<Movie> by lazy {
        setupMovies()
    }

    fun updateList(indexType: Int) {
        count = indexType

        val newMovieCount = TITLE.count() - list.count()

        //Fix index
        list.forEachIndexed { index, movie ->
            if (index >= indexType)
            {
                movie.id += newMovieCount
            }
        }

        //ADD new (only update new channel)
        for (i in 0 until newMovieCount)
        {
            val movie = buildMovieInfo(
                TITLE[count],
                CARD_IMAGE_URL[count],
                VIDEO_URL[count],
                FUNC[count]
            )
            list.add(count - 1, movie)
            //count++ in buildMovieInfo()
        }
    }

    private var count: Int = 0

    private fun setupMovies(): MutableList<Movie> {


        val list = TITLE.indices.map {
            buildMovieInfo(
                TITLE[it],
                CARD_IMAGE_URL[it],
                VIDEO_URL[it],
                FUNC[it]
            )
        }

        return list.toMutableList()
    }

    private fun buildMovieInfo(
        title: String,
        cardImageUrl: Any, //Int or String
        videoUrl: String,
        func: String
    ): Movie {
        val movie = Movie()
        movie.id = count++
        movie.title = title
        movie.cardImageUrl = cardImageUrl
        movie.videoUrl = videoUrl
        movie.func = func
        movie.exo = if (SDK_VER < OLD_SDK_VERSION)
            func.contains("^nowtv|^fantv$|^rthk$|^fox$|^fb$|^wowgua|^exoCustom|^ggiptv|^utvExo$|^tw$|^livetv_14$|^gdtv_43$".toRegex())
        else
            !func.contains("^custom".toRegex())
        movie.fixRatio = func.contains("^nowtv630$|^gdtv_(46|74)$".toRegex()) || videoUrl.contains("".toRegex()) || title in "CCTV新聞"

        return movie
    }
}
