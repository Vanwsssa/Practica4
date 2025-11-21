// NotificationsFragment.kt
package com.example.practica3uno.ui.notifications

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.practica3uno.MainActivity // Importar MainActivity para acceder a los datos estáticos
import com.example.practica3uno.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    // Handler y Runnable para actualizar la UI cada medio segundo
    private val handler = Handler(Looper.getMainLooper())
    private val sensorUpdateRunnable = object : Runnable {
        override fun run() {
            // 1. Obtener datos de la MainActivity
            val lux = MainActivity.currentLuxValue
            val (x, y, z) = MainActivity.currentAccValue

            // 2. Actualizar visualización según el estado del Switch
            if (binding.switchLight.isChecked) {
                binding.textLightData.text = String.format("Luz Ambiental (Lux): %.2f", lux)
            } else {
                binding.textLightData.text = "Luz Ambiental (Lux): Deshabilitado"
            }

            if (binding.switchAccel.isChecked) {
                binding.textAccelData.text = String.format("Acelerómetro (X, Y, Z): (%.2f, %.2f, %.2f)", x, y, z)
            } else {
                binding.textAccelData.text = "Acelerómetro (X, Y, Z): Deshabilitado"
            }

            // 3. Repetir la actualización
            handler.postDelayed(this, 500) // Actualiza cada 500 ms
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        // Cargar el estado previo de los switches (usamos SharedPreferences)
        val prefs = requireActivity().getSharedPreferences("sensor_prefs", Context.MODE_PRIVATE)
        binding.switchLight.isChecked = prefs.getBoolean("sensor_light_enabled", true)
        binding.switchAccel.isChecked = prefs.getBoolean("sensor_accel_enabled", true)

        // --- Listeners de Tema ---
        binding.btnGuinda.setOnClickListener { setAppTheme("guinda") }
        binding.btnAzul.setOnClickListener { setAppTheme("azul") }
        binding.btnSystem.setOnClickListener { setAppTheme("system") }

        // --- Listeners de Sensores ---
        binding.switchLight.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sensor_light_enabled", isChecked).apply()
            Toast.makeText(context, "Visualización Luz: $isChecked", Toast.LENGTH_SHORT).show()
        }

        binding.switchAccel.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sensor_accel_enabled", isChecked).apply()
            Toast.makeText(context, "Visualización Acelerómetro: $isChecked", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun setAppTheme(color: String) {
        val prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("selected_theme", color).apply()

        val newMode = when (color) {
            "guinda" -> AppCompatDelegate.MODE_NIGHT_NO // Forzar tema claro
            "azul" -> AppCompatDelegate.MODE_NIGHT_YES  // Forzar tema oscuro
            "system" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM // Adaptación automática
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(newMode)
        requireActivity().recreate()
        Toast.makeText(context, "Tema cambiado a $color", Toast.LENGTH_SHORT).show()
    }

    // --- Gestión del Ciclo de Vida para la visualización en tiempo real ---
    override fun onResume() {
        super.onResume()
        // Empezar a actualizar la UI del sensor cuando el fragmento es visible
        handler.post(sensorUpdateRunnable)
    }

    override fun onPause() {
        super.onPause()
        // Detener la actualización de la UI del sensor para ahorrar recursos
        handler.removeCallbacks(sensorUpdateRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}