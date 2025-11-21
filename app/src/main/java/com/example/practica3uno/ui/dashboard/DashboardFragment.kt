package com.example.practica3uno.ui.dashboard

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.documentfile.provider.DocumentFile
import com.example.practica3uno.data.AppDatabase
import com.example.practica3uno.data.FileEntity
import com.example.practica3uno.databinding.FragmentDashboardBinding
import com.example.practica3uno.ui.home.FileAdapter
import kotlinx.coroutines.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterRecent: FileAdapter
    private lateinit var adapterFavorite: FileAdapter

    private var isAuthenticated = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // Configuración segura de Adapters
        adapterRecent = FileAdapter(emptyList(), onClick = { file -> openFile(file) })
        binding.recyclerRecent.layoutManager = LinearLayoutManager(context)
        binding.recyclerRecent.adapter = adapterRecent

        adapterFavorite = FileAdapter(emptyList(), onClick = { file -> openFile(file) })
        binding.recyclerFavorite.layoutManager = LinearLayoutManager(context)
        binding.recyclerFavorite.adapter = adapterFavorite

        hideContent()

        // Verificamos si el contexto es válido antes de checar biometría
        if (context != null) {
            checkBiometricCapability()
        }

        return binding.root
    }

    private fun hideContent() {
        // Verificamos que el binding no sea nulo antes de acceder
        _binding?.let {
            it.recyclerRecent.visibility = View.GONE
            it.recyclerFavorite.visibility = View.GONE
            it.textAccessDenied.visibility = View.VISIBLE
        }
    }

    private fun showContent() {
        _binding?.let {
            it.recyclerRecent.visibility = View.VISIBLE
            it.recyclerFavorite.visibility = View.VISIBLE
            it.textAccessDenied.visibility = View.GONE
            loadData()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isAuthenticated && context != null) {
            checkBiometricCapability()
        }
    }

    private fun openFile(file: DocumentFile) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(file.uri, getMimeType(file.name ?: ""))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Error al abrir archivo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkBiometricCapability() {
        // Envolver en try-catch por si el hardware falla inesperadamente
        try {
            val biometricManager = BiometricManager.from(requireContext())
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    binding.textAccessDenied.text = "🔒 Autenticación Requerida."
                    authenticateUser()
                }
                // Si no hay hardware o huellas, permitimos acceso para que no se bloquee la app
                else -> {
                    isAuthenticated = true
                    showContent()
                }
            }
        } catch (e: Exception) {
            // Si algo falla con la librería biométrica, permitimos acceso por seguridad del flujo
            isAuthenticated = true
            showContent()
        }
    }

    private fun authenticateUser() {
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Si el usuario cancela a propósito, no mostramos error, solo se queda bloqueado
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    isAuthenticated = true
                    showContent()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Carpeta Protegida")
            .setSubtitle("Toca el sensor para ver tus archivos")
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun loadData() {
        val context = context ?: return // Si no hay contexto, no hacemos nada
        val db = AppDatabase.getDatabase(context)

        CoroutineScope(Dispatchers.IO).launch {
            val recentFiles = db.fileDao().getRecentFiles()
            val favoriteFiles = db.fileDao().getFavoriteFiles()

            // Filtramos archivos nulos o inválidos para evitar CRASH
            val validRecents = recentFiles.mapNotNull { toDocumentFile(it) }
            val validFavorites = favoriteFiles.mapNotNull { toDocumentFile(it) }

            withContext(Dispatchers.Main) {
                // Verificamos que el fragmento siga vivo antes de actualizar UI
                if (_binding != null) {
                    adapterRecent.updateData(validRecents)
                    adapterFavorite.updateData(validFavorites)
                }
            }
        }
    }

    // Modificado para ser seguro: Retorna null si falla, en vez de crashear
    private fun toDocumentFile(file: FileEntity): DocumentFile? {
        return try {
            val uri = Uri.parse(file.uri)
            if (uri == null) return null

            val docFile = DocumentFile.fromSingleUri(requireContext(), uri)
            // Verificamos si el archivo realmente existe y tenemos permiso
            if (docFile != null && docFile.exists()) {
                docFile
            } else {
                null // El archivo fue borrado o perdimos permiso
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getMimeType(name: String): String {
        return when {
            name.endsWith(".txt", true) -> "text/plain"
            name.endsWith(".jpg", true) || name.endsWith(".jpeg", true) -> "image/jpeg"
            name.endsWith(".png", true) -> "image/png"
            name.endsWith(".pdf", true) -> "application/pdf"
            else -> "*/*"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}