package dev.thematrix.tvhk

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow
import dev.thematrix.tvhk.MovieList.SDK_VER
import dev.thematrix.tvhk.MovieList.OLD_SDK_VERSION
import dev.thematrix.tvhk.PlaybackActivity.Companion.SYSTEM_UI_FLAG
import dev.thematrix.tvhk.PlaybackActivity.Companion.seekInterval
import dev.thematrix.tvhk.PlaybackActivity.Companion.toast

class PlaybackVideoFragment : VideoSupportFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoUrl = activity?.intent?.getStringExtra("videoUrl") as String
        val fixRatio = activity?.intent?.getBooleanExtra("fixRatio", false) as Boolean
        PlaybackActivity.isCurrentExo = false
        retryHandler = Handler()
        tickleHandler = Handler()

        setUpPlayer()

        Log.i("PLAYER", "PlaybackVideoFragment")
        playVideo(videoUrl.split("#")[0], fixRatio)
    }

    override fun onStart() {
        super.onStart()
        val view = this.view!!
        view.setBackgroundColor(Color.BLACK)
        view.systemUiVisibility = SYSTEM_UI_FLAG
        view.setOnSystemUiVisibilityChangeListener {
            if (view.systemUiVisibility != SYSTEM_UI_FLAG) {
                Handler().postDelayed({
                    view.systemUiVisibility = SYSTEM_UI_FLAG
                }, 3000)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerAdapter.release()
    }

    fun seek(isForward: Boolean) {
        val sign = if (isForward) 1 else -1
        isSeeking = true
        playerAdapter.seekTo(playerAdapter.currentPosition + sign * seekInterval)
        tickle()
    }

    override fun onVideoSizeChanged(width: Int, height: Int) {
        val screenWidth = this.view!!.width
        val screenHeight = this.view!!.height
        val p = this.surfaceView.layoutParams

        if (screenWidth > 0 && screenHeight > 0) {
            if (isFixRatio) {
                //Set all to 16:9 Ratio
                val ratio = 16.0 / 9.0
                if (screenWidth / screenHeight > ratio) {
                    p.width = (screenHeight * ratio).toInt()
                    p.height = screenHeight
                } else {
                    p.width = screenWidth
                    p.height = (screenWidth / ratio).toInt()
                }

                Log.d("Video Size: ", width.toString() + " " + height + " " + width / height.toDouble())
                Log.d("Output Size: ", p.width.toString() + " " + p.height + " " + p.width / p.height.toDouble())

                this.surfaceView.layoutParams = p
            } else if (width > 0 && height > 0){
                super.onVideoSizeChanged(width, height)
            }
        }
    }

    override fun onError(errorCode: Int, errorMessage: CharSequence?) {
        super.onError(errorCode, errorMessage)

        toast.setText("未能播放! $errorMessage")
        toast.show()

        if (retry > 0) {
            toast.setText("Error; 重新連接中．．．")
            toast.show()
            playVideo(mediaUrl, isFixRatio)
        }
    }

    override fun onBufferingStateChanged(start: Boolean) {
        super.onBufferingStateChanged(start)

        if (start) {
            if (!isSeeking) {
                retryHandler.postDelayed({
                    this.activity?.runOnUiThread {
                        toast.setText("Buffer; 重新連接中．．．")
                        toast.show()
                        this.playVideo(mediaUrl, isFixRatio)
                    }
                },  6000)
            }
        }
        else {
            retryHandler.removeCallbacksAndMessages(null)
            isSeeking = false
        }

    }

    //Allow auto hide on tickle() all the time
    override fun tickle() {
        tickleHandler.removeCallbacksAndMessages(null)

        showControlsOverlay(true)

        tickleHandler.postDelayed({
            this.activity?.runOnUiThread {
                hideControlsOverlay(true)
            }
        }, 3000)
    }

    private fun setUpPlayer(){
        playerAdapter = NewMediaPlayerAdapter(activity)
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_ONE)
        mTransportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)

        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.isControlsOverlayAutoHideEnabled = false //Important
        hideControlsOverlay(true)
        mTransportControlGlue.isSeekEnabled = true
        progressBarManager.disableProgressBar()

    }

    fun playVideo(videoUrl: String, fixRatio: Boolean) {
        isSeeking = false
        if (mediaUrl == videoUrl)
            retry--
        else
            retry = defaultRetryNum

        val headers = HashMap<String, String>()
        if (videoUrl.contains("grtn")) {
            headers["Referer"] = "http://www.gdtv.cn/"
        }

        mediaUrl = videoUrl
        isFixRatio = fixRatio
        playerAdapter.reset()
        playerAdapter.setDataSource(Uri.parse(handleUrl(videoUrl)), headers)
        mTransportControlGlue.playWhenPrepared()
        Log.i("Video Link:", videoUrl)
    }

    private fun handleUrl(url: String): String{
        return if(SDK_VER < OLD_SDK_VERSION){
            url.replace("https://", "http://")
        }else{
            url
        }
    }

    private val defaultRetryNum = 3
    private var retry = defaultRetryNum
    private var isSeeking = false

    private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<NewMediaPlayerAdapter>
    private lateinit var playerAdapter: NewMediaPlayerAdapter
    private lateinit var retryHandler: Handler
    private lateinit var tickleHandler: Handler
    private var mediaUrl: String = ""
    private var isFixRatio: Boolean = false

}
