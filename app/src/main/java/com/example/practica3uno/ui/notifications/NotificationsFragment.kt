package com.example.practica3uno.ui.notifications



import android.os.Bundle

import android.view.*

import android.widget.Toast

import androidx.appcompat.app.AppCompatDelegate

import androidx.fragment.app.Fragment

import com.example.practica3uno.databinding.FragmentNotificationsBinding



class NotificationsFragment : Fragment() {



    private var _binding: FragmentNotificationsBinding? = null

    private val binding get() = _binding!!



    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?

    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)



// Botones para cambiar tema

        binding.btnGuinda.setOnClickListener {

            setTheme("guinda")

        }

        binding.btnAzul.setOnClickListener {

            setTheme("azul")

        }



        return binding.root

    }



    private fun setTheme(color: String) {

// Usamos MODE_NIGHT_NO para forzar el tema claro (Guinda)

// y MODE_NIGHT_YES para forzar el tema oscuro (Azul).

        val newMode = when (color) {

            "guinda" -> AppCompatDelegate.MODE_NIGHT_NO

            "azul" -> AppCompatDelegate.MODE_NIGHT_YES

            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

        }



// 1. Aplicar el nuevo modo de noche/día

        AppCompatDelegate.setDefaultNightMode(newMode)



// 2. FORZAR LA RECREACIÓN de la Activity para que el nuevo tema

// afecte a la barra de estado, barra de navegación inferior y ActionBar.

        requireActivity().recreate()



        Toast.makeText(context, "Tema cambiado a $color", Toast.LENGTH_SHORT).show()

    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null

    }

}