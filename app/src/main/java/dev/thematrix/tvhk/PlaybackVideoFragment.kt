package dev.thematrix.tvhk

import android.net.Uri
import android.os.Bundle
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

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerAdapter.release()
    }

    private fun setUpPlayer(){
        playerAdapter = MediaPlayerAdapter(activity)
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)
        mTransportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)

        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        mTransportControlGlue.host = glueHost

        mTransportControlGlue.isControlsOverlayAutoHideEnabled = true
        hideControlsOverlay(false)
        mTransportControlGlue.isSeekEnabled = false
    }

    fun playVideo(videoUrl: String) {
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
    }
}
