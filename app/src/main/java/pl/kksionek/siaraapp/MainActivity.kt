package pl.kksionek.siaraapp

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import pl.kksionek.siaraapp.ShakeDetector.OnShakeListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        siaraImage.setOnClickListener { v -> handleShakeEvent() }

        happyButton.setOnClickListener { v -> happyClicked() }

        sadButton.setOnClickListener { v -> sadClicked() }

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector()
        mShakeDetector?.setOnShakeListener(object : OnShakeListener {

            override fun onShake() {
                handleShakeEvent()
            }
        })
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
        mSensorManager?.registerListener(
                mShakeDetector,
                mAccelerometer,
                SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        mSensorManager?.unregisterListener(mShakeDetector)
        super.onPause()
    }
}
