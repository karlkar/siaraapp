package pl.kksionek.siaraapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import java.util.*

const val BUNDLE_KEY_MOOD = "MOOD"
const val BUNDLE_VAL_HAPPY = "HAPPY"
const val BUNDLE_VAL_SAD = "SAD"

class PlayService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var resIds: List<Int>
    private val rand: Random = Random()

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun onCreate() {
        super.onCreate()

        resIds = R.raw::class.java.fields
            .map { it.name }
            .map { resources.getIdentifier(it, "raw", application.packageName) }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null

        val item = when (intent?.getStringExtra(BUNDLE_KEY_MOOD)) {
            BUNDLE_VAL_HAPPY -> R.raw.zaimponowales
            BUNDLE_VAL_SAD -> R.raw.zamknij_morde
            else -> resIds[rand.nextInt(resIds.size)]
        }
        mediaPlayer = MediaPlayer.create(this, item).apply {
            setOnCompletionListener { stopSelf() }
            start() // no need to call prepare(); create() does that for you
        }
        return START_NOT_STICKY
    }
}
