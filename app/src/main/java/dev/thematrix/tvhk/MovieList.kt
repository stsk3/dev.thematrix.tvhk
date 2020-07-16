package dev.thematrix.tvhk

object MovieList {

    val SDK_VER = android.os.Build.VERSION.SDK_INT
    const val OLD_SDK_VERSION = 19
    const val NEWS_INDEX = 7
    const val NEWS_CATEGORY_INDEX = 1
    const val SPORTS_INDEX = 33
    const val SPORTS_CATEGORY_INDEX = 4

    val CATEGORY = arrayOf(
        "新聞",
        "HK01", //1
        "體育",
        "其他體育",
        "FOX體育", //4
        "有線電視",
        //"ViuTV",
        "RTHK",
        "其他新聞",
        "澳門",
        "廣州電視", //!!
        "UTV",
        "自選"
    )

    val TITLE = mutableListOf(
        "有線財經資訊台", //!!
        "有線直播台",
        "有線新聞台", //!!
        "now新聞台",
        "now直播台",
        "港台電視32",
        "SKIP",
        "HK01", //7
        "HK01",
        "HK01",
        "SKIP",
        "now630",
        "澳門體育",
        "博斯魅力",
        "皇馬台(西班牙)",
        "皇馬台(英文)",
        "NBATV",
        "SKIP",
        "CCTV5",
        "CCTV5+",
        "廣東體育",
        "廣州競賽",
        "五星體育", //!!
        "勁爆體育",
        "新視覺HD",
        "北京冬奧紀實",
        "遼寧體育",
        "先鋒乒羽",
        "江蘇體育休閒",
        "大連文體",
        "SKIP",
        "FS1", //33
        "FS2",
        "FS3",
        "FS4",
        "SKIP",
        "香港開電視",
        "有線綜合娛樂台", //!!
        "SKIP",
        //"ViuTV",
        //"SKIP",
        "港台電視31",
        "SKIP",
        "民視新聞",
        "三立新聞",
        "澳門資訊",
        "廣州新聞頻道",
        "CCTV新聞",
        "CCTV1",
        "SKIP",
        "澳視澳門",
        "澳門綜藝",
        "澳門-MACAU",
        "澳視葡文",
        "SKIP",
        "綜合頻道", //!!
        "影視頻道",
        "生活頻道",
        "購物頻道",
        "少兒頻道",
        "法治頻道",
        "SKIP",
        "C+", //!!
        "賽馬頻道", //!!
        "SKIP",
        "自選1", //!!
        "自選2",
        "自選Exo1",
        "自選Exo2",
        "SKIP"
    )

    val CARD_IMAGE_URL = mutableListOf<Any>(
        R.drawable.cablefinance,
        R.drawable.cablelivenews,
        R.drawable.cablenews,
        R.drawable.nowtv332,
        R.drawable.nowtv331,
        R.drawable.rthktv32,
        0,
        R.drawable.fb_hk01wemedia,
        R.drawable.fb_hk01wemedia,
        R.drawable.fb_hk01wemedia,
        0,
        R.drawable.nowtv630,
        R.drawable.tdmsport,
        R.drawable.sportcast,
        R.drawable.rmtv,
        R.drawable.rmtv,
        R.drawable.nbatv,
        0,
        R.drawable.cctv5,
        R.drawable.cctv5plus,
        R.drawable.gdtv_sports,
        R.drawable.gz_jingsai,
        R.drawable.sport_5star,
        R.drawable.sport_gingbao,
        R.drawable.sport_newying,
        R.drawable.sport_btv,
        R.drawable.sport_liaoning,
        R.drawable.sport_tabletennisbadminton,
        R.drawable.sport_jz,
        R.drawable.sport_dl,
        0,
        R.drawable.foxsports,
        R.drawable.foxsports,
        R.drawable.foxsports,
        R.drawable.foxsports,
        0,
        R.drawable.opentv,
        R.drawable.cableentertainment,
        0,
        //R.drawable.viutv,
        //0,
        R.drawable.rthktv31,
        0,
        R.drawable.tw_ftvn,
        R.drawable.tw_setn,
        R.drawable.tdminfo,
        R.drawable.gz_xinwen,
        R.drawable.cctv13,
        R.drawable.cctv1,
        0,
        R.drawable.tdmmacau,
        R.drawable.tdmhd,
        R.drawable.tdmsat,
        R.drawable.tdmportugal,
        0,
        R.drawable.gz_zhonghe,
        R.drawable.gz_yingshi,
        R.drawable.gz_shenghuo,
        R.drawable.gz_gouwu,
        R.drawable.gz_shaoer,
        R.drawable.gz_jingji,
        0,
        R.drawable.utv,
        R.drawable.utv,
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
        "",
        "https://www.rthk.hk/feeds/dtt/rthktv32_https.m3u8",
        "SKIP",
        "http://live.cdn.hk01.com/origin/smil:01news.smil/playlist.m3u8",
        "http://live2.cdn.hk01.com/origin/smil:Sudden01.smil/playlist.m3u8",
        "http://live.cdn.hk01.com/origin/smil:EventBox001.smil/playlist.m3u8",
        "SKIP",
        "",
        "http://live1.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#" +
                "http://live2.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#" +
                "http://live3.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#" +
                "http://live4.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8",
        "http://ms003.happytv.com.tw/live/OcScNdWHvBx5P4w3/index.m3u8",
        if (SDK_VER < OLD_SDK_VERSION)
            "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hwebes_1@300661/index_3_av-p.m3u8#" +
                    "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hwebes_1@300661/index_3_av-b.m3u8#" +
                    "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hwebes_1@300661/index_2_av-p.m3u8#" +
                    "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hwebes_1@300661/index_2_av-b.m3u8#" +
                    "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hwebes_1@300661/index_1_av-p.m3u8#" +
                    "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hwebes_1@300661/index_1_av-b.m3u8"
        else
            "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hwebes_1@300661/master.m3u8",
        if (SDK_VER < OLD_SDK_VERSION)
            "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hweben_1@300662/index_3_av-p.m3u8#" +
                    "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hweben_1@300662/index_3_av-b.m3u8#" +
                    "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hweben_1@300662/index_2_av-p.m3u8#" +
                    "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hweben_1@300662/index_2_av-b.m3u8#" +
                    "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hweben_1@300662/index_1_av-p.m3u8#" +
                    "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hweben_1@300662/index_1_av-b.m3u8"
        else
            "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hweben_1@300662/master.m3u8",
        "",
        "SKIP",
        "http://111.40.205.87/PLTV/88888888/224/3221225591/index.m3u8#" +
                "http://ottrrs.hl.chinamobile.com/PLTV/88888888/224/3221225591/index.m3u8",
        "http://111.40.205.87/PLTV/88888888/224/3221225689/index.m3u8#" +
                "http://ottrrs.hl.chinamobile.com/PLTV/88888888/224/3221225603/index.m3u8",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "http://play.ggiptv.com:13164/v/hunantv.php?id=329&type=.m3u8",
        "",
        "",
        "SKIP",
        "http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/master.m3u8",
        "http://foxsportstw-lh.akamaihd.net/i/live01_0@38037/master.m3u8",
        "http://foxsportstw-lh.akamaihd.net/i/live01_0@455127/master.m3u8",
        "http://foxsportstw-lh.akamaihd.net/i/live01_0@570534/master.m3u8",
        "SKIP",
        "http://media.fantv.hk/m3u8/archive/channel2.m3u8",
        "",
        "SKIP",
        //"",
        //"SKIP",
        "https://www.rthk.hk/feeds/dtt/rthktv31_https.m3u8",
        "SKIP",
        "http://210.61.56.23/hls/ftvtv/index.m3u8#" +
                "http://6.mms.vlog.xuite.net/hls/ftvtv/index.m3u8#" +
                "http://1.mms.vlog.xuite.net/hls/ftvtv/index.m3u8",
        "http://60.199.188.65/HLS/WG_ETTV-N/index.m3u8",
        "http://live1.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#" +
                "http://live2.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#" +
                "http://live3.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#" +
                "http://live4.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8",
        "",
        "http://111.40.205.87/PLTV/88888888/224/3221225599/index.m3u8#" +
                "http://ottrrs.hl.chinamobile.com/PLTV/88888888/224/3221225599/index.m3u8",
        "http://111.40.205.87/PLTV/88888888/224/3221225710/index.m3u8#" +
                "http://ottrrs.hl.chinamobile.com/PLTV/88888888/224/3221225587/index.m3u8",
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
        "SKIP",
        "",
        "",
        "",
        "",
        "SKIP"
    )

    val FUNC = mutableListOf(
        "MOB-NGW", //"wowgua_ch108",
        "wowgua_ch110",
        "MOB-SCC", //"wowgua_ch109",
        "nowtv332",
        "nowtv331",
        "rthk",
        "SKIP",
        "",
        "",
        "",
        "SKIP",
        "nowtv630",
        "",
        "",
        "rmtv",
        "rmtv",
        "wowgua_nbatv",
        "SKIP",
        "",
        "",
        "ggiptv_ty_6_5",
        "gztv_5c7f70b7e4b01c17db18fbd9",
        "chinaSport_ssty",
        "chinaSport_jbtyhd",
        "chinaSport_xsjhd",
        "ggiptv_ty_7",
        "ggiptv_ty_8",
        "",
        "ggiptv_migu_158",
        "ggiptv_tt_68",
        "SKIP",
        "fox",
        "fox",
        "fox",
        "fox",
        "SKIP",
        "fantv",
        "MOB-CN", //"wowgua_ch301",
        "SKIP",
        //"viutv99",
        //"SKIP",
        "rthk",
        "SKIP",
        "tw",
        "",
        "",
        "gztv_5c7f6f73e4b01c17db18fbd3",
        "cctv13",
        "",
        "SKIP",
        "",
        "",
        "",
        "",
        "SKIP",
        "gztv_5c7f7072e4b01c17db18fbd5",
        "gztv_5c7f70dce4b01c17db18fbdb",
        "gztv_5c7f70fee4b01c17db18fbdd",
        "gztv_5d6f31d3e4b03f60f96e2544",
        "gztv_5c7f711de4b01c17db18fbdf",
        "gztv_5c7f7097e4b01c17db18fbd7",
        "SKIP",
        "MOB-U1-NO", //"wowgua_utv_c_plus",
        "MOB-HR", //"wowgua_utv_racing",
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
            func.contains("^nowtv|^fantv$|^rthk$|^fox$|^fb$|^wowgua|^exoCustom|^ggiptv|^utvExo$".toRegex())
        else
            !func.contains("^custom".toRegex())
        movie.fixRatio = func.contains("^nowtv630$|^gdtv_fix$".toRegex()) || videoUrl.contains("".toRegex()) || title in "遼寧體育"

        return movie
    }
}
