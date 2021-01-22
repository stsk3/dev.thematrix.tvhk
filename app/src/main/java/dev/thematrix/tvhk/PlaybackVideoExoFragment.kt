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
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.TrackSelectionDialogBuilder
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.video.VideoListener
import dev.thematrix.tvhk.MovieList.OLD_SDK_VERSION
import dev.thematrix.tvhk.MovieList.SDK_VER
import dev.thematrix.tvhk.PlaybackActivity.Companion.SYSTEM_UI_FLAG
import dev.thematrix.tvhk.PlaybackActivity.Companion.seekInterval
import dev.thematrix.tvhk.PlaybackActivity.Companion.toast
import kotlinx.android.synthetic.main.activity_simple.view.*


class PlaybackVideoExoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_simple, container,
            false)

        val videoUrl = activity?.intent?.getStringExtra("videoUrl") as String
        val fixRatio = activity?.intent?.getBooleanExtra("fixRatio", false) as Boolean
        view.setBackgroundColor(Color.BLACK)
        view.systemUiVisibility = SYSTEM_UI_FLAG

        //Not show button on TV
        if (SDK_VER >= OLD_SDK_VERSION) {
            view.setOnSystemUiVisibilityChangeListener {
                if (view.systemUiVisibility != SYSTEM_UI_FLAG) {
                    val trackSelectionButton = view.exo_track_selection_button
                    val resizeModeButton = view.exo_resize_button
                    val liveButton = view.exo_live_button
                    val sourceNumText = view.source_num_text

                    trackSelectionButton.visibility = VISIBLE
                    resizeModeButton.visibility = VISIBLE
                    liveButton.visibility = VISIBLE
                    sourceNumText.visibility = VISIBLE
                    val sourceDisplayNum = windowIndex + 1
                    sourceNumText.text = sourceDisplayNum.toString()

                    Handler().postDelayed({
                        view.systemUiVisibility = SYSTEM_UI_FLAG
                        trackSelectionButton.visibility = GONE
                        resizeModeButton.visibility = GONE
                        liveButton.visibility = GONE
                        sourceNumText.visibility = GONE
                    }, 3000)
                }
            }
        }

        PlaybackActivity.isCurrentExo = true
        setupExoPlayer(view)

        Log.i("PLAYER", "PlaybackVideoExoFragment")
        playVideo(videoUrl, fixRatio)
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
            .setBandwidthMeter(bandwidthMeter)
            .build()
        player.playWhenReady = true
        player.repeatMode = Player.REPEAT_MODE_ALL
        val playerView = view.player_view
        playerView.useController = true
        playerView.requestFocus()
        playerView.player = player
        playerView.hideController()


        httpDataSourceFactory = DefaultHttpDataSourceFactory("exoplayer", null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true)
        val dataSourceFactory = DefaultDataSourceFactory(activity, httpDataSourceFactory)
        hlsMediaSourceFactory = HlsMediaSource.Factory(dataSourceFactory)
        dashMediaSourceFactory = DashMediaSource.Factory(dataSourceFactory)
        progressiveMediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)

        player.run {
            addAnalyticsListener(object : AnalyticsListener {
                override fun onSeekProcessed(eventTime: AnalyticsListener.EventTime) {
                    super.onSeekProcessed(eventTime)

                    if (windowIndex != eventTime.windowIndex) {
                        windowIndex = eventTime.windowIndex
                        val sourceDisplayNum = windowIndex + 1
                        view.source_num_text.text = sourceDisplayNum.toString()
                        toast.setText("已轉到Source $sourceDisplayNum/$sourceCount")
                        toast.show()
                    }
                }


            })

            addListener(object : Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)

                    when (playbackState) {
                        Player.STATE_IDLE -> {
                            toast.setText("STATE_IDLE")
                            toast.show()
                            if (mediaUrl != "")
                                playVideo(mediaUrl, isFixRatio)
                        }
                        Player.STATE_BUFFERING -> {}
                        Player.STATE_READY -> {}
                        Player.STATE_ENDED -> {
                            toast.setText("STATE_ENDED")
                            toast.show()
                            if (mediaUrl != "")
                                playVideo(mediaUrl, isFixRatio)
                        }
                    }
                }

                override fun onPlayerError(e: ExoPlaybackException) {
                    super.onPlayerError(e)
                    if (e.type == ExoPlaybackException.TYPE_SOURCE)
                    {
                        if (e.sourceException is BehindLiveWindowException) {
                            toast.setText("BehindLiveWindowException")
                            toast.show()
                            playVideo(mediaUrl, isFixRatio, player.currentWindowIndex)
                        }
                    }
                }
            })


            addVideoListener(object : VideoListener {
                override fun onVideoSizeChanged(
                    width: Int,
                    height: Int,
                    unappliedRotationDegrees: Int,
                    pixelWidthHeightRatio: Float
                ) {
                    if (isFixRatio) {
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
                        Log.d(
                            "Output Size: ",
                            p.width.toString() + " " + p.height + " " + p.width / p.height.toDouble()
                        )

                        // Redraw myView
                        playerView.requestLayout()
                    } else {
                        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    }

                }
            })
        }


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
        // Back to live Button
        val liveButton = view.exo_live_button
        liveButton.setOnClickListener {
            player.seekToDefaultPosition()
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

    fun videoSeek(isNext: Boolean) {
        if (isNext)
            player.next()
        else
            player.previous()
    }

    fun seek(isForward: Boolean) {
        val sign = if (isForward) 1 else -1
        player.seekTo(player.currentPosition + sign * seekInterval)
        view?.player_view?.showController() //Will hide automatically
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    fun playVideo(videoUrl: String, fixRatio: Boolean, window: Int = 0) {
        if (videoUrl.contains("grtn")) {
            httpDataSourceFactory.defaultRequestProperties.set("Referer", "http://www.gdtv.cn/")
        } else {
            httpDataSourceFactory.defaultRequestProperties.clear()
        }


        val concatenatingMediaSource = ConcatenatingMediaSource()
        mediaUrl = videoUrl
        isFixRatio = fixRatio
        windowIndex = window
        sourceCount = videoUrl.split("#").size

        videoUrl.split("#").forEach {
            val videoUri = Uri.parse(it)

            val mediaSource = when {
                videoUrl.contains(".mpd") -> dashMediaSourceFactory.createMediaSource(videoUri)
                videoUrl.contains(".m3u8") -> hlsMediaSourceFactory.createMediaSource(videoUri)
                else -> progressiveMediaSourceFactory.createMediaSource(videoUri)
            }

            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        player.prepare(concatenatingMediaSource)
        player.seekToDefaultPosition(windowIndex)

        Log.i("Video Link:", videoUrl)
    }



    private lateinit var player: SimpleExoPlayer
    private lateinit var trackSelector: DefaultTrackSelector
    private lateinit var hlsMediaSourceFactory: HlsMediaSource.Factory
    private lateinit var dashMediaSourceFactory: DashMediaSource.Factory
    private lateinit var progressiveMediaSourceFactory: ProgressiveMediaSource.Factory
    private lateinit var httpDataSourceFactory: DefaultHttpDataSourceFactory
    private lateinit var mediaUrl: String
    private var isFixRatio: Boolean = false
    private var windowIndex: Int = 0
    private var sourceCount: Int = 0

}
