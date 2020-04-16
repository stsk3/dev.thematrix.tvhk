package dev.thematrix.tvhk

object MovieList {

    val SDK_VER = android.os.Build.VERSION.SDK_INT
    const val OLD_SDK_VERSION = 19
    const val NEWS_INDEX = 7
    const val NEWS_CATEGORY_INDEX = 1
    const val SPORTS_INDEX = 31
    const val SPORTS_CATEGORY_INDEX = 4

    val CATEGORY = arrayOf(
        "新聞",
        "HK01", //1
        "體育",
        "其他體育",
        "FOX體育", //4
        "有線電視",
        "ViuTV",
        "RTHK",
        "其他新聞",
        "澳門",
        "台灣",
        "UAE",
        "印尼",
        "廣東電視",
        "廣州電視",
        "UTV",
        "自選"
    )

    val TITLE = mutableListOf(
        "有線財經資訊台",
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
        "ELTA1",
        "Eleven1",
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
        "SKIP",
        "FS1", //31
        "FS2",
        "FS3",
        "FS4",
        "SKIP",
        "香港開電視",
        "有線綜合娛樂台",
        "有線電影台",
        "有線18台",
        "有線HD603台",
        "SKIP",
        "ViuTV",
        "SKIP",
        "港台電視31",
        "SKIP",
        "民視新聞",
        "三立新聞",
        "澳門資訊",
        "廣東新聞頻道",
        "廣州新聞頻道",
        "NHK World",
        "Channel News Asia",
        "Aljazeera News",
        "CCTV新聞",
        "CCTV1",
        "SKIP",
        "澳視澳門",
        "澳門綜藝",
        "澳門-MACAU",
        "澳視葡文",
        "有線第一頻道",
        "有線互動新聞台",
        "有線第三頻道",
        "SKIP",
        "Yahoo TV", //!!
        "中視",
        "動物星球",
        "龍祥時代",
        "好萊塢電影台",
        "Fox Action Movie",
        "華納電影",
        "東森電影",
        "東森洋片",
        "八大綜合",
        "SKIP",
        "國家地理頻道",
        "國家地理野生頻道",
        "國家地理歷險頻道",
        "H2 HD",
        "RT Doc",
        "Star Movies",
        "Star World",
        "Tv5 Monde",
        "MBC2 Movie",
        "MBC Max",
        "MBC Action",
        "SKIP",
        "天映頻道",
        "Thrill",
        "Cinema World",
        "Hits",
        "SKIP",
        "廣東衛視",
        "珠江頻道",
        "公共頻道",
        "體育頻道",
        "嘉佳卡通",
        "國際頻道",
        "TVS1經濟科教",
        "TVS2南方衛視",
        "TVS3廣東綜藝",
        "TVS4廣東影視",
        "TVS5廣東少兒",
        "房產頻道",
        "現代教育",
        "移動頻道",
        "文化頻道",
        "SKIP",
        "綜合頻道",
        "影視頻道",
        "生活頻道",
        "購物頻道",
        "少兒頻道",
        "法治頻道",
        "SKIP",
        "C+",
        "賽馬頻道",
        "SKIP",
        "自選1", //!!
        "自選2",
        "自選Exo1",
        "自選Exo2",
        "SKIP"
    )

    val CARD_IMAGE_URL = mutableListOf(
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
        R.drawable.sport_elta1,
        R.drawable.sport_eleven1,
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
        0,
        R.drawable.foxsports,
        R.drawable.foxsports,
        R.drawable.foxsports,
        R.drawable.foxsports,
        0,
        R.drawable.opentv,
        R.drawable.cableentertainment,
        R.drawable.cable_movie,
        R.drawable.cable_18,
        R.drawable.cable_hd603,
        0,
        R.drawable.viutv,
        0,
        R.drawable.rthktv31,
        0,
        R.drawable.tw_ftvn,
        R.drawable.tw_setn,
        R.drawable.tdminfo,
        R.drawable.gdtv_news,
        R.drawable.gz_xinwen,
        R.drawable.in_nhkworld,
        R.drawable.in_cna,
        R.drawable.in_aljazeera,
        R.drawable.cctv13,
        R.drawable.cctv1,
        0,
        R.drawable.tdmmacau,
        R.drawable.tdmhd,
        R.drawable.tdmsat,
        R.drawable.tdmportugal,
        R.drawable.mo_ch1,
        R.drawable.mo_news,
        R.drawable.mo_ch3,
        0,
        R.drawable.tw_yahootv,
        R.drawable.tw_ctv,
        R.drawable.tw_animalplanet,
        R.drawable.tw_lstime,
        R.drawable.tw_hollywood,
        R.drawable.tw_foxactionmovie,
        R.drawable.tw_wb,
        R.drawable.tw_ebcmovie,
        R.drawable.tw_ebcwesternmovie,
        R.drawable.tw_gtv,
        0,
        R.drawable.uae_natgeo,
        R.drawable.uae_natgeowild,
        R.drawable.uae_natgeopeople,
        R.drawable.uae_history2,
        R.drawable.uae_rtdoc,
        R.drawable.uae_starmovies,
        R.drawable.uae_starworld,
        R.drawable.uae_tv5monde,
        R.drawable.uae_mbc2,
        R.drawable.uae_mbcmax,
        R.drawable.uae_mbcaction,
        0,
        R.drawable.in_celestial,
        R.drawable.in_thrill,
        R.drawable.in_cinemaworld,
        R.drawable.in_hits,
        0,
        R.drawable.gdtv_star,
        R.drawable.gdtv_zj,
        R.drawable.gdtv_public,
        R.drawable.gdtv_sports,
        R.drawable.gdtv_cartoon,
        R.drawable.gdtv_world,
        R.drawable.gdtv_tvs1,
        R.drawable.gdtv_tvs2,
        R.drawable.gdtv_tvs3,
        R.drawable.gdtv_tvs4,
        R.drawable.gdtv_tvs5,
        R.drawable.gdtv_house,
        R.drawable.gdtv_edu,
        R.drawable.gdtv_mobile,
        R.drawable.gdtv_wh,
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
        "#https://ottproxy2.mott.tv/livehls/MOB-SCC/index.m3u8",
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
        "",
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
        "SKIP",
        "http://foxsportstw-lh.akamaihd.net/i/live01_0@38035/master.m3u8",
        "http://foxsportstw-lh.akamaihd.net/i/live01_0@38037/master.m3u8",
        "http://foxsportstw-lh.akamaihd.net/i/live01_0@455127/master.m3u8",
        "http://foxsportstw-lh.akamaihd.net/i/live01_0@570534/master.m3u8",
        "SKIP",
        "http://media.fantv.hk/m3u8/archive/channel2.m3u8",
        "",
        "",
        "",
        "",
        "SKIP",
        "",
        "SKIP",
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
        "",
        "http://45.126.83.51/uq2663/h/h23/index.m3u8#http://210.210.155.35/uq2663/h/h23/index.m3u8",
        "http://45.126.83.51/uq2663/h/h29/index.m3u8#http://210.210.155.35/uq2663/h/h29/index.m3u8",
        "http://45.126.83.51/qwr9ew/s/s17/index.m3u8#http://210.210.155.35/qwr9ew/s/s17/index.m3u8",
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
        "http://202.175.127.77/live/show/ch1/index.m3u8",
        "http://202.175.127.77/live/show/ch2/index.m3u8",
        "http://202.175.127.77/live/show/ch3/index.m3u8",
        "SKIP",
        "",
        "http://104.250.154.42:8080/ZZ_zhongshi/ZZ_zhongshi.m3u8#" +
                "http://192.200.120.82:8080/ZZ_zhongshi/ZZ_zhongshi.m3u8#" +
                "http://192.154.103.75:8080/ZZ_zhongshi/ZZ_zhongshi.m3u8",
        "http://104.250.154.42:8080/ZZ_dongwuxingqiu/ZZ_dongwuxingqiu.m3u8#" +
                "http://192.200.120.82:8080/ZZ_dongwuxingqiu/ZZ_dongwuxingqiu.m3u8#" +
                "http://192.154.103.75:8080/ZZ_dongwuxingqiu/ZZ_dongwuxingqiu.m3u8",
        "http://104.250.154.42:8080/ZZ_shidai/ZZ_shidai.m3u8#" +
                "http://192.200.120.82:8080/ZZ_shidai/ZZ_shidai.m3u8#" +
                "http://192.154.103.75:8080/ZZ_shidai/ZZ_shidai.m3u8",
        "http://104.250.154.42:8080/ZZ_haolaiwu/ZZ_haolaiwu.m3u8#" +
                "http://192.200.120.82:8080/ZZ_haolaiwu/ZZ_haolaiwu.m3u8#" +
                "http://192.154.103.75:8080/ZZ_haolaiwu/ZZ_haolaiwu.m3u8",
        "http://104.250.154.42:8080/ZZ_foxaction/ZZ_foxaction.m3u8#" +
                "http://192.200.120.82:8080/ZZ_foxaction/ZZ_foxaction.m3u8#" +
                "http://192.154.103.75:8080/ZZ_foxaction/ZZ_foxaction.m3u8",
        "http://104.250.154.42:8080/ZZ_huanadianying/ZZ_huanadianying.m3u8#" +
                "http://192.200.120.82:8080/ZZ_huanadianying/ZZ_huanadianying.m3u8#" +
                "http://192.154.103.75:8080/ZZ_huanadianying/ZZ_huanadianying.m3u8",
        "http://104.250.154.42:8080/ZZ_dongsendianying/ZZ_dongsendianying.m3u8#" +
                "http://192.200.120.82:8080/ZZ_dongsendianying/ZZ_dongsendianying.m3u8#" +
                "http://192.154.103.75:8080/ZZ_dongsendianying/ZZ_dongsendianying.m3u8",
        "http://104.250.154.42:8080/ZZ_dongsenyangpian/ZZ_dongsenyangpian.m3u8#" +
                "http://192.200.120.82:8080/ZZ_dongsenyangpian/ZZ_dongsenyangpian.m3u8#" +
                "http://192.154.103.75:8080/ZZ_dongsenyangpian/ZZ_dongsenyangpian.m3u8",
        "http://104.250.154.42:8080/ZZ_zhongtianyazhou/ZZ_zhongtianyazhou.m3u8#" +
                "http://192.200.120.82:8080/ZZ_zhongtianyazhou/ZZ_zhongtianyazhou.m3u8#" +
                "http://192.154.103.75:8080/ZZ_zhongtianyazhou/ZZ_zhongtianyazhou.m3u8",
        "SKIP",
        "http://livecdnh1.tvanywhere.ae:80/hls/nat_geo/index.m3u8",
        "http://livecdnh1.tvanywhere.ae:80/hls/nat_geo_wild/index.m3u8",
        "http://livecdnh1.tvanywhere.ae:80/hls/nat_geo_people/index.m3u8",
        "http://livecdnh1.tvanywhere.ae:80/hls/h2/index.m3u8",
        "http://livecdnh1.tvanywhere.ae:80/hls/rt_doc/index.m3u8",
        "http://livecdnh1.tvanywhere.ae:80/hls/star_movies/index.m3u8",
        "http://livecdnh1.tvanywhere.ae:80/hls/star_world/index.m3u8",
        "http://livecdnh1.tvanywhere.ae:80/hls/tv5/index.m3u8",
        "http://livecdnh3.tvanywhere.ae/hls/mbc2/index.m3u8",
        "http://livecdnh3.tvanywhere.ae/hls/mbcmax/index.m3u8",
        "http://livecdnh3.tvanywhere.ae/hls/mbcaction/index.m3u8",
        "SKIP",
        "http://45.126.83.51/qwr9ew/s/s33/index.m3u8#" +
                "http://210.210.155.35/qwr9ew/s/s33/index.m3u8",
        "http://45.126.83.51/qwr9ew/s/s34/index.m3u8#" +
                "http://210.210.155.35/qwr9ew/s/s34/index.m3u8",
        "http://45.126.83.51/uq2663/h/h04/index2.m3u8#" +
                "http://210.210.155.35/uq2663/h/h04/index2.m3u8",
        "http://45.126.83.51/uq2663/h/h37/index.m3u8#" +
                "http://210.210.155.35/uq2663/h/h37/index.m3u8",
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
        "wowgua_ch108",
        "cabletv110",
        "wowgua_ch109",
        "nowtv332",
        "nowtv331",
        "rthk",
        "hkatv",
        "SKIP",
        "",
        "",
        "SKIP",
        "nowtv630",
        "",
        "",
        "rmtv",
        "rmtv",
        "wowgua_nbatv",
        "ggiptv_ty_21",
        "ggiptv_ty_28",
        "SKIP",
        "",
        "",
        "ggiptv_ty_6",
        "gztv_5c7f70b7e4b01c17db18fbd9",
        "chinaSport_ssty",
        "chinaSport_jbtyhd",
        "chinaSport_xsjhd",
        "ggiptv_ty_7_1",
        "ggiptv_ty_8",
        "",
        "SKIP",
        "fox",
        "fox",
        "fox",
        "fox",
        "SKIP",
        "fantv",
        "wowgua_ch301",
        "ggiptv_gt_30",
        "ggiptv_gt_32",
        "ggiptv_gt_33",
        "SKIP",
        "viutv99",
        "SKIP",
        "rthk",
        "SKIP",
        "tw",
        "",
        "",
        "gdtve_28",
        "gztv_5c7f6f73e4b01c17db18fbd3",
        "in",
        "in",
        "in",
        "cctv13",
        "",
        "SKIP",
        "",
        "",
        "",
        "",
        "mocable",
        "mocable",
        "mocable",
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
        "SKIP",
        "uae",
        "uae",
        "uae",
        "uae",
        "uae",
        "uae",
        "uae",
        "uae",
        "uae",
        "uae",
        "uae",
        "SKIP",
        "in",
        "in",
        "in",
        "in",
        "SKIP",
        "gdtve_25",
        "gdtve_26",
        "gdtve_31",
        "gdtve_27",
        "gdtve_29",
        "gdtve_30",
        "gdtve_93",
        "gdtve_7",
        "gdtve_166",
        "gdtve_37",
        "gdtve_38",
        "gdtv_33",
        "gdtv_169",
        "gdtv_101",
        "gdtv_172",
        "SKIP",
        "gztv_5c7f7072e4b01c17db18fbd5",
        "gztv_5c7f70dce4b01c17db18fbdb",
        "gztv_5c7f70fee4b01c17db18fbdd",
        "gztv_5d6f31d3e4b03f60f96e2544",
        "gztv_5c7f711de4b01c17db18fbdf",
        "gztv_5c7f7097e4b01c17db18fbd7",
        "SKIP",
        "wowgua_utv_c_plus",
        "wowgua_utv_racing",
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
        cardImageUrl: Int,
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
            func.contains("^nowtv|^fantv$|^rthk$|^fox$|^fb$|^wowgua|^in$|^uae$|^tw$|^viutv99$|^gdtve|^exoCustom".toRegex())
        else
            !func.contains("^custom".toRegex())
        movie.fixRatio = func.contains("^gdtv|^nowtv630$|^in$|^mocable$".toRegex()) || videoUrl.contains("".toRegex()) || title in "遼寧體育"

        return movie
    }
}
