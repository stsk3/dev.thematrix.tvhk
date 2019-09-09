package dev.thematrix.tvhk

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.BaseMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.activity_simple.view.*


class PlaybackVideoExoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_simple, container,
            false)

        val videoUrl = activity?.intent?.getStringExtra("videoUrl") as String

        PlaybackActivity.isCurrentExo = true
        setupExoPlayer(view)

        playVideo(videoUrl)

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
        dashMediaSourceFactory = DashMediaSource.Factory(dataSourceFactory)
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    fun playVideo(mediaUrl: String) {
        val videoUri = Uri.parse(mediaUrl)
        val mediaSource = if (mediaUrl.contains(".mpd"))
            dashMediaSourceFactory.createMediaSource(videoUri)
        else
            hlsMediaSourceFactory.createMediaSource(videoUri)

        playerView.resizeMode = if (mediaUrl.contains("webch630"))
            AspectRatioFrameLayout.RESIZE_MODE_FILL
        else
            AspectRatioFrameLayout.RESIZE_MODE_FIT

        player.prepare(mediaSource)
    }



    companion object {
        private lateinit var player: SimpleExoPlayer
        private lateinit var playerView: SimpleExoPlayerView
        private lateinit var dataSourceFactory: DefaultDataSourceFactory
        private lateinit var hlsMediaSourceFactory: HlsMediaSource.Factory
        private lateinit var dashMediaSourceFactory: DashMediaSource.Factory
    }
}
