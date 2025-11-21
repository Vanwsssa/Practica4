package com.example.practica3uno

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practica3uno.databinding.ActivityMainBinding
// Esta es la importación clave que faltaba para la barra de navegación:
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var accelerometer: Sensor? = null

    // Este bloque 'companion object' permite que NotificationsFragment
    // lea los datos de los sensores sin errores.
    companion object {
        var currentLuxValue: Float = 0f
        var currentAccValue: Triple<Float, Float, Float> = Triple(0f, 0f, 0f)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Carga del Tema (Antes de super.onCreate)
        val themePrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val selectedTheme = themePrefs.getString("selected_theme", "system")

        when (selectedTheme) {
            "guinda" -> setTheme(R.style.Theme_Practica3Uno_Guinda)
            "azul" -> setTheme(R.style.Theme_Practica3Uno_Azul)
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // 2. Inicialización de Sensores
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        // Solo registramos los sensores si el usuario los quiere ver (Ahorro de batería)
        val sensorPrefs = getSharedPreferences("sensor_prefs", Context.MODE_PRIVATE)
        val isLightEnabled = sensorPrefs.getBoolean("sensor_light_enabled", true)
        val isAccelEnabled = sensorPrefs.getBoolean("sensor_accel_enabled", true)

        if (isLightEnabled) {
            lightSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }

        if (isAccelEnabled) {
            accelerometer?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Siempre dejamos de escuchar sensores al salir de la app
        sensorManager.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        when (event.sensor.type) {
            Sensor.TYPE_LIGHT -> {
                currentLuxValue = event.values[0]
            }
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                currentAccValue = Triple(x, y, z)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesario
    }
}