package dev.thematrix.tvhk

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow

class PlaybackVideoFragment : VideoSupportFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoUrl = activity?.intent?.getStringExtra("videoUrl") as String
        val fixRatio = activity?.intent?.getBooleanExtra("fixRatio", false) as Boolean
        PlaybackActivity.isCurrentExo = false

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
            } else {
                super.onVideoSizeChanged(width, height)
            }
        }
    }

    override fun onError(errorCode: Int, errorMessage: CharSequence?) {
        super.onError(errorCode, errorMessage)

        PlaybackActivity.toast.setText("未能播放! $errorMessage")
        PlaybackActivity.toast.show()

        if (retry > 0) {
            playVideo(mediaUrl, isFixRatio)
        }
    }

    private fun setUpPlayer(){
        playerAdapter = NewMediaPlayerAdapter(activity)
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_ONE)
        mTransportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)

        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.isControlsOverlayAutoHideEnabled = false
        hideControlsOverlay(false)
        mTransportControlGlue.isSeekEnabled = false
        progressBarManager.disableProgressBar()

    }

    fun playVideo(videoUrl: String, fixRatio: Boolean) {
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
        return if(SDK_VER < 19){
            url.replace("https://", "http://")
        }else{
            url
        }
    }

    private val defaultRetryNum = 1
    private var retry = defaultRetryNum

    companion object {
        private val SDK_VER = android.os.Build.VERSION.SDK_INT
        private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<NewMediaPlayerAdapter>
        private lateinit var playerAdapter: NewMediaPlayerAdapter
        private var mediaUrl: String = ""
        private var isFixRatio: Boolean = false
        private const val SYSTEM_UI_FLAG = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}
