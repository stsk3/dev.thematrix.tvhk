package dev.thematrix.tvhk

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dev.thematrix.tvhk.PlaybackActivity.Companion.currentSourceIndex
import dev.thematrix.tvhk.PlaybackActivity.Companion.currentVideoID
import dev.thematrix.tvhk.PlaybackActivity.Companion.requestQueue
import dev.thematrix.tvhk.PlaybackActivity.Companion.toast
import kotlinx.android.synthetic.main.activity_simple.view.*
import org.json.JSONArray
import org.json.JSONObject


class PlaybackVideoExoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_simple, container,
            false)

        val (id, title, _, _, videoUrl, func) = activity?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as Movie

        PlaybackActivity.isCurrentExo = true

        setupExoPlayer(view)

        prepareVideo(id, title, videoUrl, func)

        return view
    }

    private fun setupExoPlayer(view: View) {
        // setup track selector
        val bandwithMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwithMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        // create player
        player = ExoPlayerFactory.newSimpleInstance(activity, trackSelector)
        player.playWhenReady = true
        playerView = view.player_view
        playerView.useController = true
        playerView.requestFocus()
        playerView.player = player
        playerView.hideController()

        dataSourceFactory = DefaultDataSourceFactory(activity, "exoplayer", bandwithMeter)
        hlsMediaSourceFactory = HlsMediaSource.Factory(dataSourceFactory)
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }


    internal fun prepareVideo(id: Int, title: String, videoUrl: String, func: String){
        currentVideoID = id

        if(videoUrl.equals("")){
            getVideoUrl(title, func)
        }else{
            playVideo(videoUrl.split("#")[currentSourceIndex])
        }
    }

    fun playVideo(mediaUrl: String) {
        val videoUri = Uri.parse(mediaUrl)
        val mediaSource = hlsMediaSourceFactory.createMediaSource(videoUri)

        player.prepare(mediaSource)
    }

    private fun getVideoUrl(title: String, ch: String) {
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
                    try {
                        var mediaUrl: String = JSONArray(JSONObject(JSONObject(response.get("asset").toString()).get("hls").toString()).get("adaptive").toString()).get(0).toString()

                        playVideo(mediaUrl)
                    }catch (exception: Exception){
                        showPlaybackErrorMessage(title)
                    }
                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title)
                }
            )

            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

            requestQueue.add(jsonObjectRequest)
        }else if(ch.equals("cabletv109") || ch.equals("cabletv110")){
            url = "https://mobileapp.i-cable.com/iCableMobile/API/api.php"

            val stringRequest = object: StringRequest(
                Method.POST,
                url,
                Response.Listener { response ->
                    try {
                        playVideo(JSONObject(JSONObject(response).get("result").toString()).get("stream").toString())
                    }catch (exception: Exception){
                        showPlaybackErrorMessage(title)
                    }
                },
                Response.ErrorListener{ error ->
                    showPlaybackErrorMessage(title)
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

    private fun showPlaybackErrorMessage(title: String){
        toast.setText(title + " 暫時未能播放，請稍候再試。")
        toast.show()
    }



    companion object {
        private lateinit var player: SimpleExoPlayer
        private lateinit var playerView: SimpleExoPlayerView
        private lateinit var dataSourceFactory: DefaultDataSourceFactory
        private lateinit var hlsMediaSourceFactory: HlsMediaSource.Factory
    }
}
