package pl.kksionek.siaraapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import java.util.*

val BUNDLE_KEY_MOOD = "MOOD"
val BUNDLE_VAL_HAPPY = "HAPPY"
val BUNDLE_VAL_SAD = "SAD"

class PlayService : Service() {

    private var mMediaPlayer: MediaPlayer? = null
    lateinit private var mResIds: IntArray
    private val mRand: Random = Random()

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        val sounds = resources.obtainTypedArray(R.array.texts)
        mResIds = IntArray(sounds.length())
        for (i in 0 until sounds.length()) {
            mResIds[i] = sounds.getResourceId(i, -1)
        }
        sounds.recycle()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("KAROL", "Start")
        if (mMediaPlayer != null) {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
        val item = when (intent?.getStringExtra(BUNDLE_KEY_MOOD)) {
            BUNDLE_VAL_HAPPY -> R.raw.zaimponowales
            BUNDLE_VAL_SAD -> R.raw.zamknij_morde
            else -> mResIds[mRand.nextInt(mResIds.size)]
        }
        mMediaPlayer = MediaPlayer.create(this, item)
        mMediaPlayer?.setOnCompletionListener { stopSelf() }
        mMediaPlayer?.start() // no need to call prepare(); create() does that for you
        return Service.START_NOT_STICKY
    }
}
