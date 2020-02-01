package dev.thematrix.tvhk

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow

class PlaybackVideoFragment : VideoSupportFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoUrl = activity?.intent?.getStringExtra("videoUrl") as String
        PlaybackActivity.isCurrentExo = false

        setUpPlayer()

        playVideo(videoUrl)
    }

    override fun onStart() {
        super.onStart()
        this.view!!.setBackgroundColor(Color.BLACK)
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
        if (mediaUrl.contains("grtn")) {
            val screenWidth = this.view!!.width
            val screenHeight = this.view!!.height
            val p = this.surfaceView.layoutParams

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
        }
        else
        {
            super.onVideoSizeChanged(width, height)
        }
    }

    private fun setUpPlayer(){
        playerAdapter = MediaPlayerAdapter(activity)
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_ONE)
        mTransportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)

        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        mTransportControlGlue.host = glueHost

        mTransportControlGlue.isControlsOverlayAutoHideEnabled = true
        hideControlsOverlay(false)
        mTransportControlGlue.isSeekEnabled = true
    }

    fun playVideo(videoUrl: String) {
        mediaUrl = videoUrl
        playerAdapter.reset()
        playerAdapter.setDataSource(Uri.parse(handleUrl(videoUrl)))
        mTransportControlGlue.playWhenPrepared()
    }

    private fun handleUrl(url: String): String{
        if(SDK_VER < 21){
            return url.replace("https://", "http://")
        }else{
            return url
        }
    }

    companion object {
        private val SDK_VER = android.os.Build.VERSION.SDK_INT
        private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>
        private lateinit var playerAdapter: MediaPlayerAdapter
        private lateinit var mediaUrl: String
    }
}
