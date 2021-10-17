package com.example.hamster

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(),  SensorEventListener{

    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null
    private var pressure: Sensor? = null
    private var temp: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        temp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

    }


    override fun onSensorChanged(event: SensorEvent) {
            when(event.values[0]) {
                in -300f .. 17f -> findViewById<TextView>(R.id.tv_temp).setTextColor(Color.parseColor("#0093ff"))
                in  17f .. 25f -> findViewById<TextView>(R.id.tv_temp).setTextColor(Color.BLACK)
                in  25f .. Float.MAX_VALUE -> findViewById<TextView>(R.id.tv_temp).setTextColor(Color.parseColor("#ff5900"))
                else -> {}
            }
            if (event.sensor?.type == Sensor.TYPE_LIGHT) {
                findViewById<TextView>(R.id.tv_lux).append(event.values[0].toString() + "lux" + "\n")
                findViewById<ScrollView>(R.id.sv_lux).fullScroll(View.FOCUS_DOWN)
            }
            if (event.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                findViewById<TextView>(R.id.tv_temp).append(event.values[0].toString() + "°C" + "\n")
                findViewById<ScrollView>(R.id.sv_temp).fullScroll(View.FOCUS_DOWN)
            }

            if (event.sensor?.type == Sensor.TYPE_PRESSURE) {
                findViewById<TextView>(R.id.tv_pressure).text = event.values[0].toString()
            }


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL )
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}