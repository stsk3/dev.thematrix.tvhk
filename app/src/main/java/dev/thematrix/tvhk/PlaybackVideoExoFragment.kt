package dev.thematrix.tvhk

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.video.VideoListener
import kotlinx.android.synthetic.main.activity_simple.view.*


class PlaybackVideoExoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_simple, container,
            false)

        val videoUrl = activity?.intent?.getStringExtra("videoUrl") as String
        view.setBackgroundColor(Color.BLACK)
        view.systemUiVisibility = SYSTEM_UI_FLAG
        view.setOnSystemUiVisibilityChangeListener {
            if (view.systemUiVisibility != SYSTEM_UI_FLAG)
                Handler().postDelayed({ view.systemUiVisibility = SYSTEM_UI_FLAG } , 3000)
        }

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
        player.repeatMode = Player.REPEAT_MODE_ONE
        playerView = view.player_view
        playerView.useController = true
        playerView.requestFocus()
        playerView.player = player
        playerView.hideController()

        dataSourceFactory = DefaultDataSourceFactory(activity, "exoplayer", bandwithMeter)
        hlsMediaSourceFactory = HlsMediaSource.Factory(dataSourceFactory)
        dashMediaSourceFactory = DashMediaSource.Factory(dataSourceFactory)

        player.addVideoListener(object : VideoListener {
            override fun onVideoSizeChanged(
                width: Int,
                height: Int,
                unappliedRotationDegrees: Int,
                pixelWidthHeightRatio: Float
            ) {

                if (mediaUrl.contains("webch630")) {
                    val screenWidth = playerView.width
                    val screenHeight = playerView.height
                    val p = playerView.layoutParams

                    //Set all to 16:9 Ratio
                    val ratio = 16.0 / 9.0
                    if (screenWidth / screenHeight > ratio) {
                        p.width = (screenHeight * ratio).toInt()
                        p.height = screenHeight
                    } else {
                        p.width = screenWidth
                        p.height = (screenWidth / ratio).toInt()
                    }

                    playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

                    Log.d("Video Size: ", width.toString() + " " + height + " " + width / height.toDouble())
                    Log.d("Output Size: ", p.width.toString() + " " + p.height + " " + p.width / p.height.toDouble())

                    // Redraw myView
                    playerView.requestLayout()
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    fun playVideo(videoUrl: String) {
        val videoUri = Uri.parse(videoUrl)
        mediaUrl = videoUrl

        val mediaSource = if (videoUrl.contains(".mpd"))
            dashMediaSourceFactory.createMediaSource(videoUri)
        else
            hlsMediaSourceFactory.createMediaSource(videoUri)

        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

        player.prepare(mediaSource)
    }



    companion object {
        private lateinit var player: SimpleExoPlayer
        private lateinit var playerView: SimpleExoPlayerView
        private lateinit var dataSourceFactory: DefaultDataSourceFactory
        private lateinit var hlsMediaSourceFactory: HlsMediaSource.Factory
        private lateinit var dashMediaSourceFactory: DashMediaSource.Factory
        private lateinit var mediaUrl: String
        private const val SYSTEM_UI_FLAG = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}
