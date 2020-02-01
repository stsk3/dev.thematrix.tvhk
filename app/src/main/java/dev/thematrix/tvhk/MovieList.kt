package dev.thematrix.tvhk

object MovieList {
    const val FB_INDEX = 8
    const val FB_CATEGORY_INDEX = 1

    val CATEGORY = arrayOf(
        "News",
        "HK01",
        "Sports",
        "FOXSports",
        "CableTV",
        "Now TV",
        "RTHK TV",
        "UTV",
        "GDTV",
        "TDM"
    )

    val TITLE = mutableListOf(
        "有線財經資訊台",
        "有線直播台",
        "有線新聞台",
        "now新聞台",
        "now直播台",
        "港台電視32",
        "ATV亞視數碼媒體",
        "SKIP",
        "HK01",
        "HK01",
        "HK01",
        "SKIP",
        "now630",
        "澳視體育",
        "CCTV5",
        "CCTV5+",
        "博斯魅力",
        /*"BT Sport2",
        "BT ESPN",
        "Digi Sport2",
        "Digi Sport3",
        "Digi Sport4",
        "Bein Sport1",
        "Sport Klub",
        "Arena Sport4",
        "Arena Sport5",*/
        "皇馬台",
        "NBATV",
        "SKIP",
        "FS1",
        "FS2",
        "FS3",
        "SKIP",
        "香港開電視",
        "有線綜合娛樂台",
        "SKIP",
        "ViuTV",
        "SKIP",
        "港台電視31",
        "SKIP",
        "C+",
        "賽馬頻道",
        "SKIP",
        "廣東衛視",
        "珠江頻道",
        "體育頻道",
        "公共頻道",
        "新聞頻道",
        "嘉佳卡通",
        "國際頻道",
        "TVS1經濟科教",
        "TVS2南方衛視",
        "TVS3廣東綜藝",
        "TVS4廣東影視",
        "TVS5廣東少兒",
        "房產頻道",
        "現代教育",
        "文化頻道",
        "SKIP",
        "澳視澳門",
        "澳視資訊",
        "澳視高清",
        "澳視衛星",
        "澳視葡文",
        "SKIP"
    )

    val DESCRIPTION = mutableListOf(
        "",
        "畫面比例可能不符合你的電視",
        "",
        "",
        "",
        "",
        "",
        "SKIP",
        "",
        "",
        "",
        "SKIP",
        "",
        "",
        "",
        "",
        "",
        /*"",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",*/
        "",
        "",
        "SKIP",
        "",
        "",
        "",
        "SKIP",
        "",
        "",
        "SKIP",
        "",
        "SKIP",
        "",
        "SKIP",
        "",
        "",
        "SKIP",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
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
        "",
        "SKIP"
    )

    val CARD_IMAGE_URL = mutableListOf(
        R.drawable.cablefinance,
        R.drawable.cablelivenews,
        R.drawable.cablenews,
        R.drawable.nowtv332,
        R.drawable.nowtv331,
        R.drawable.rthktv32,
        R.drawable.fb_hkatv,
        0,
        R.drawable.fb_hk01wemedia,
        R.drawable.fb_hk01wemedia,
        R.drawable.fb_hk01wemedia,
        0,
        R.drawable.nowtv630,
        R.drawable.tdmsport,
        R.drawable.cctv5,
        R.drawable.cctv5plus,
        R.drawable.sportcast,
        /*R.drawable.btsport2,
        R.drawable.btsportespn,
        R.drawable.digisport2,
        R.drawable.digisport3,
        R.drawable.digisport4,
        R.drawable.beinsport1,
        R.drawable.sportklub,
        R.drawable.arenasport4,
        R.drawable.arenasport5,*/
        R.drawable.rmtv,
        R.drawable.nbatv,
        0,
        R.drawable.foxsports,
        R.drawable.foxsports,
        R.drawable.foxsports,
        0,
        R.drawable.opentv,
        R.drawable.cableentertainment,
        0,
        R.drawable.viutv,
        0,
        R.drawable.rthktv31,
        0,
        R.drawable.utv,
        R.drawable.utv,
        0,
        R.drawable.gdtv_star,
        R.drawable.gdtv_zj,
        R.drawable.gdtv_sports,
        R.drawable.gdtv_public,
        R.drawable.gdtv_news,
        R.drawable.gdtv_cartoon,
        R.drawable.gdtv_world,
        R.drawable.gdtv_tvs1,
        R.drawable.gdtv_tvs2,
        R.drawable.gdtv_tvs3,
        R.drawable.gdtv_tvs4,
        R.drawable.gdtv_tvs5,
        R.drawable.gdtv_house,
        R.drawable.gdtv_edu,
        R.drawable.gdtv_wh,
        0,
        R.drawable.tdmmacau,
        R.drawable.tdminfo,
        R.drawable.tdmhd,
        R.drawable.tdmsat,
        R.drawable.tdmportugal,
        0
    )

    val VIDEO_URL = mutableListOf(
        "",
        "",
        "https://ottproxy2.mott.tv/livehls/MOB-SCC/index.m3u8",
        "",
        "",
        "https://www.rthk.hk/feeds/dtt/rthktv32_https.m3u8",
        "https://894e4e718bd20702a29a206d1eb990ee.live.prod.hkatv.com/a1_abr_lo.m3u8",
        "SKIP",
        "http://live.cdn.hk01.com/origin/smil:01news.smil/playlist.m3u8",
        "http://live2.cdn.hk01.com/origin/smil:Sudden01.smil/playlist.m3u8",
        "http://live.cdn.hk01.com/origin/smil:EventBox001.smil/playlist.m3u8",
        "SKIP",
        "",
        "http://live1.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8",
        "http://111.40.205.87/PLTV/88888888/224/3221225591/index.m3u8",
        "http://111.40.205.87/PLTV/88888888/224/3221225689/index.m3u8",
        "http://ms003.happytv.com.tw/live/OcScNdWHvBx5P4w3/index.m3u8",
        /*"https://sportbar.biz/lba/123/g3/ukbt2sport.m3u8",
        "https://sportbar.biz/lba/123/g7/btse.m3u8",
        "https://sportbar.biz/lba/123/g7/digisport2ro.m3u8",
        "https://sportbar.biz/lba/OGQyNWQ3YzE3MzkzYTg2NDNkYTkxNjhjYmI5ZGRlYzA=/g7/digi3ro.m3u8",
        "https://sportbar.biz/lba/OGQyNWQ3YzE3MzkzYTg2NDNkYTkxNjhjYmI5ZGRlYzA=/g1/rodigi4.m3u8",
        "https://sportbar.biz/lba/123/g5/beinsport1tur.m3u8",
        "https://sportbar.biz/lba/123/g5/sportklub1rs.m3u8",
        "https://sportbar.biz/lba/123/g3/arenaserbia4.m3u8",
        "https://sportbar.biz/lba/123/g7/ar5sport.m3u8",*/
        "http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hwebes_1@300661/master.m3u8#http://rmtv24hweblive-lh.akamaihd.net/i/rmtv24hweben_1@300662/master.m3u8",
        "",
        "SKIP",
        "http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/index_4700_av-p.m3u8#http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/index_2700_av-p.m3u8#http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/index_1500_av-p.m3u8",
        "http://foxsports07-i.akamaihd.net/hls/live/716920/live04/index.m3u8",
        "http://foxsports07-i.akamaihd.net/hls/live/716919/live03/index.m3u8",
        "SKIP",
        "http://media.fantv.hk/m3u8/archive/channel2_stream1.m3u8",
        "",
        "SKIP",
        "",
        "SKIP",
        "https://www.rthk.hk/feeds/dtt/rthktv31_https.m3u8",
        "SKIP",
        "",
        "",
        "SKIP",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "SKIP",
        "http://live1.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8",
        "http://live1.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8",
        "SKIP"
    )

    val FUNC = mutableListOf(
        "wowgua_ch108",
        "cabletv110",
        "cabletv109",
        "nowtv332",
        "nowtv331",
        "rthk32",
        "hkatv",
        "SKIP",
        "",
        "",
        "",
        "SKIP",
        "nowtv630",
        "",
        "",
        "",
        "",
        /*"sportbar",
        "sportbar",
        "sportbar",
        "sportbar",
        "sportbar",
        "sportbar",
        "sportbar",
        "sportbar",
        "sportbar",*/
        "",
        "wowgua_nbatv",
        "SKIP",
        "fox",
        "fox",
        "fox",
        "SKIP",
        "fantv",
        "wowgua_ch301",
        "SKIP",
        "viutv99",
        "SKIP",
        "rthk31",
        "SKIP",
        "wowgua_utv_c_plus",
        "wowgua_utv_racing",
        "SKIP",
        "gdtv_25",
        "gdtv_26",
        "gdtv_27",
        "gdtv_31",
        "gdtv_28",
        "gdtv_29",
        "gdtv_30",
        "gdtv_93",
        "gdtv_7",
        "gdtv_166",
        "gdtv_37",
        "gdtv_38",
        "gdtv_33",
        "gdtv_169",
        "gdtv_172",
        "SKIP",
        "",
        "",
        "",
        "",
        "",
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
        movie.exo = func in arrayOf("cabletv109", "viutv99", "nowtv332", "nowtv331", "nowtv630", "fantv", "rthk31", "rthk32", "fox", "fb", "hkatv", "wowgua_nbatv", "wowgua_ch301", "wowgua_utv_racing", "wowgua_utv_c_plus", "wowgua_ch108", "sportbar")


        return movie
    }
}
