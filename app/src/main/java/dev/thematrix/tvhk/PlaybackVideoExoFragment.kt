package dev.thematrix.tvhk

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.TrackSelectionDialogBuilder
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.video.VideoListener
import dev.thematrix.tvhk.PlaybackActivity.Companion.toast
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
            if (view.systemUiVisibility != SYSTEM_UI_FLAG) {
                val trackSelectionButton = view.exo_track_selection_button
                trackSelectionButton.visibility = VISIBLE
                val resizeModeButton = view.exo_resize_button
                resizeModeButton.visibility = VISIBLE
                Handler().postDelayed({
                    view.systemUiVisibility = SYSTEM_UI_FLAG
                    trackSelectionButton.visibility = GONE
                    resizeModeButton.visibility = GONE
                }, 3000)
            }
        }

        PlaybackActivity.isCurrentExo = true
        setupExoPlayer(view)

        playVideo(videoUrl)
        return view
    }

    private fun setupExoPlayer(view: View) {
        val context = this.context!!

        // setup track selector
        val bandwidthMeter = DefaultBandwidthMeter.Builder(context).build()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
        val trackSelectorParameters = DefaultTrackSelector.ParametersBuilder(context).build()
        trackSelector = DefaultTrackSelector(context, videoTrackSelectionFactory)
        trackSelector.parameters = trackSelectorParameters





        // create player
        player = SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setBandwidthMeter(bandwidthMeter).build()
        player.playWhenReady = true
        player.repeatMode = Player.REPEAT_MODE_ALL
        val playerView = view.player_view
        playerView.useController = true
        playerView.requestFocus()
        playerView.player = player
        playerView.hideController()


        val dataSourceFactory = DefaultDataSourceFactory(activity, "exoplayer")
        hlsMediaSourceFactory = HlsMediaSource.Factory(dataSourceFactory)
        dashMediaSourceFactory = DashMediaSource.Factory(dataSourceFactory)

        player.addAnalyticsListener(object : AnalyticsListener {
            override fun onSeekProcessed(eventTime: AnalyticsListener.EventTime) {
                toast.setText("正在轉到Source ${eventTime.windowIndex}")
                toast.show()
            }
        })

        player.addVideoListener(object : VideoListener {
            override fun onVideoSizeChanged(
                width: Int,
                height: Int,
                unappliedRotationDegrees: Int,
                pixelWidthHeightRatio: Float
            ) {

                if (mediaUrl.contains("webch630") || mediaUrl.contains("grtn")) {
                    val playerView = view.player_view
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
                else
                {
                    playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                }

            }
        })


        // Track Selection Button
        val trackSelectionButton = view.exo_track_selection_button
        trackSelectionButton.setOnClickListener {
            trackSelectionDialog(context)
        }
        // Resize Mode Button
        val resizeModeButton = view.exo_resize_button
        resizeModeButton.setOnClickListener {
            val playerView = view.player_view
            playerView.resizeMode = if (playerView.resizeMode == AspectRatioFrameLayout.RESIZE_MODE_FIT)
                AspectRatioFrameLayout.RESIZE_MODE_FILL else AspectRatioFrameLayout.RESIZE_MODE_FIT
        }
    }

    fun trackSelectionDialog(context: Context) {
        val mappedTrackInfo = trackSelector.currentMappedTrackInfo
        if (mappedTrackInfo != null) {
            val dialogPair = TrackSelectionDialogBuilder(context, "選擇品質", trackSelector, 0)
                .setShowDisableOption(false)
                .setAllowAdaptiveSelections(true)
                .build()

            dialogPair.show()
        }
    }

    fun videoSeek(index: Int) {
        player.seekTo(index, C.TIME_UNSET)
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
        val concatenatingMediaSource = ConcatenatingMediaSource()
        mediaUrl = videoUrl

        videoUrl.split("#").forEach {
            val videoUri = Uri.parse(it)

            val mediaSource = if (videoUrl.contains(".mpd"))
                dashMediaSourceFactory.createMediaSource(videoUri)
            else
                hlsMediaSourceFactory.createMediaSource(videoUri)

            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        player.prepare(concatenatingMediaSource)
    }



    companion object {
        private lateinit var player: SimpleExoPlayer
        private lateinit var trackSelector: DefaultTrackSelector
        private lateinit var hlsMediaSourceFactory: HlsMediaSource.Factory
        private lateinit var dashMediaSourceFactory: DashMediaSource.Factory
        private lateinit var mediaUrl: String
        private const val SYSTEM_UI_FLAG = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}
