package dev.thematrix.tvhk

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity

class PlaybackExoActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, PlaybackVideoExoFragment())
                .commit()
        }


        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
            //PlaybackVideoExoFragment().channelSwitch(direction, true)
        }


        return true
    }
}
