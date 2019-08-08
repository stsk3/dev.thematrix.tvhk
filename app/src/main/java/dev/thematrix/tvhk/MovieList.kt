package dev.thematrix.tvhk

object MovieList {
    val CATEGORY = arrayOf(
        "CableTV",
        "Now TV",
        "RTHK TV",
        "TDM"/*,
        "FOXSports"*/
    )

    val list: List<Movie> by lazy {
        setupMovies()
    }

    private var count: Int = 0

    private fun setupMovies(): List<Movie> {
        val title = arrayOf(
            "香港開電視",
            "有線新聞台",
            "有線直播台",
            "SKIP",
            "ViuTV",
            "now新聞台",
            "now直播台",
            "SKIP",
            "港台電視31",
            "港台電視32",
            "SKIP",
            "澳視體育",
            "澳視澳門",
            "澳視資訊",
            "澳視高清",
            "澳視衛星",
            "澳視葡文",
            "SKIP"/*,
            "FS1",
            "FS2",
            "FS3",
            "SKIP"*/
        )

        val description = arrayOf(
            "",
            "",
            "畫面比例可能不符合你的電視",
            "SKIP",
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
            "",
            "",
            "SKIP"/*,
            "",
            "",
            "",
            "SKIP"*/
        )

        val cardImageUrl = arrayOf(
            R.drawable.opentv,
            R.drawable.cablenews,
            R.drawable.cablelivenews,
            0,
            R.drawable.viutv,
            R.drawable.nowtv332,
            R.drawable.nowtv331,
            0,
            R.drawable.rthktv31,
            R.drawable.rthktv32,
            0,
            R.drawable.tdmsport,
            R.drawable.tdmmacau,
            R.drawable.tdminfo,
            R.drawable.tdmhd,
            R.drawable.tdmsat,
            R.drawable.tdmportugal,
            0/*,
            R.drawable.foxsports,
            R.drawable.foxsports,
            R.drawable.foxsports,
            0*/
        )

        val videoUrl = arrayOf(
            "http://media.fantv.hk/m3u8/archive/channel2_stream1.m3u8",
            "",
            "",
            "SKIP",
            "",
            "",
            "",
            "SKIP",
            "https://www.rthk.hk/feeds/dtt/rthktv31_https.m3u8",
            "https://www.rthk.hk/feeds/dtt/rthktv32_https.m3u8",
            "SKIP",
            "http://live1.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch4/sport_ch4.live/playlist.m3u8",
            "http://live1.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch1/ch1.live/playlist.m3u8",
            "http://live1.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch5/info_ch5.live/playlist.m3u8",
            "http://live1.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch6/hd_ch6.live/playlist.m3u8",
            "http://live1.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch3/ch3.live/playlist.m3u8",
            "http://live1.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#http://live2.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#http://live3.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8#http://live4.tdm.com.mo:1935/ch2/ch2.live/playlist.m3u8",
            "SKIP"/*,
            "http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/index_4700_av-p.m3u8#http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/index_2700_av-p.m3u8#http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/index_1500_av-p.m3u8",
            "http://foxsports07-i.akamaihd.net/hls/live/716920/live04/index.m3u8",
            "http://foxsports07-i.akamaihd.net/hls/live/716919/live03/index.m3u8",
            "SKIP"*/
        )

        val func = arrayOf(
            "fantv",
            "cabletv109",
            "cabletv110",
            "SKIP",
            "viutv99",
            "nowtv332",
            "nowtv331",
            "SKIP",
            "rthk31",
            "rthk32",
            "SKIP",
            "",
            "",
            "",
            "",
            "",
            "",
            "SKIP"/*,
            "",
            "",
            "",
            "SKIP"*/
        )

        val list = title.indices.map {
            buildMovieInfo(
                title[it],
                description[it],
                cardImageUrl[it],
                videoUrl[it],
                func[it]
            )
        }

        return list
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
        movie.exo = func in arrayOf("viutv99", "nowtv332", "nowtv331", "fantv", "rthk31", "rthk32")


        return movie
    }
}
