package dev.thematrix.tvhk

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.NoCache

class PlaybackActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpNetwork()

        if (savedInstanceState == null) {
            val (id, title, _, _, videoUrl, func, exo) = this?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as Movie

            if (exo) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, PlaybackVideoExoFragment())
                    .commit()
            }
            else
            {
                supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, PlaybackVideoFragment())
                    .commit()
            }
        }

        toast = Toast.makeText(this, "", Toast.LENGTH_LONG)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }



    private fun setUpNetwork(){
        requestQueue = RequestQueue(NoCache(), BasicNetwork(HurlStack())).apply {
            start()
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        lateinit var direction: String

        if(
            event.keyCode == KeyEvent.KEYCODE_CHANNEL_UP ||
            event.keyCode == KeyEvent.KEYCODE_DPAD_UP ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_REWIND ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_SKIP_BACKWARD ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD ||
            event.keyCode == KeyEvent.KEYCODE_NAVIGATE_PREVIOUS ||
            event.keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT
        ){
            direction = "PREVIOUS"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN ||
            event.keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_SKIP_FORWARD ||
            event.keyCode == KeyEvent.KEYCODE_MEDIA_STEP_FORWARD ||
            event.keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT ||
            event.keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT
        ){
            direction = "NEXT"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_DPAD_LEFT
        ){
            direction = "LEFT"
        }else if(
            event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
            (event.keyCode == KeyEvent.KEYCODE_BACK && event.isLongPress)
        ){
            direction = "NEXT"
        }else{
            return super.dispatchKeyEvent(event)
        }

        if(event.action == KeyEvent.ACTION_DOWN){
            val list = MovieList.list

            var videoId = currentVideoID

            if(direction.equals("PREVIOUS")){
                videoId--
                if (list[videoId].title == "SKIP")
                {
                    videoId--
                }
                currentSourceIndex = 0
            }else if(direction.equals("NEXT")) {
                videoId++
                if (list[videoId].title == "SKIP")
                {
                    videoId++
                }
                currentSourceIndex = 0
            }

            val channelCount = list.count()
            if(videoId < 0){
                videoId = channelCount - 1
            }else if(videoId >= channelCount){
                videoId = 0
            }

            val item = list[videoId]

            if (item.videoUrl != "") {
                var sourceCount = item.videoUrl.split("#").size
                if (direction.equals("LEFT")) {
                    currentSourceIndex = (currentSourceIndex - 1) % sourceCount
                } else if (direction.equals("RIGHT") || direction.equals("RETRY")) {
                    currentSourceIndex = (currentSourceIndex + 1) % sourceCount
                }
            }

            toast.setText("正在轉台到 " + item.title + "[" + currentSourceIndex + "]")
            toast.show()


            this?.intent?.putExtra(DetailsActivity.MOVIE, item)
            if (item.exo && !isCurrentExo)
            {
                supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, PlaybackVideoExoFragment())
                    .commit()
            }
            else if (!item.exo && isCurrentExo)
            {
                supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, PlaybackVideoFragment())
                    .commit()
            }
            else
            {
                preparePlayer(item)
            }
        }


        return true
    }

    private fun preparePlayer(item:Movie)
    {
        if (isCurrentExo)
            PlaybackVideoExoFragment().prepareVideo(item.id, item.title, item.videoUrl, item.func)
        else
            PlaybackVideoFragment().prepareVideo(item.id, item.title, item.videoUrl, item.func)
    }

    companion object {
        var currentVideoID = -1
        var currentSourceIndex = 0
        internal var isCurrentExo = false
        lateinit var toast: Toast
        lateinit var requestQueue: RequestQueue
    }
}
