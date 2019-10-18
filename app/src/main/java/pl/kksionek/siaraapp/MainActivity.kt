package pl.kksionek.siaraapp

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pl.kksionek.siaraapp.ShakeDetector.OnShakeListener

class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private val shakeDetector: ShakeDetector = ShakeDetector(object : OnShakeListener {
        override fun onShake() {
            handleShakeEvent()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        siaraImage.setOnClickListener { handleShakeEvent() }

        happyButton.setOnClickListener { happyClicked() }

        sadButton.setOnClickListener { sadClicked() }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun handleShakeEvent() {
        val intent = Intent(this, PlayService::class.java)
        startService(intent)
    }

    private fun happyClicked() {
        val intent = Intent(this, PlayService::class.java)
        intent.putExtra(BUNDLE_KEY_MOOD, BUNDLE_VAL_HAPPY)
        startService(intent)
    }

    private fun sadClicked() {
        val intent = Intent(this, PlayService::class.java)
        intent.putExtra(BUNDLE_KEY_MOOD, BUNDLE_VAL_SAD)
        startService(intent)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            shakeDetector,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        sensorManager.unregisterListener(shakeDetector)
        super.onPause()
    }
}
