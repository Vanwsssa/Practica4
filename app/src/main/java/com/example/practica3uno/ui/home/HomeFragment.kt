package com.example.practica3uno.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.documentfile.provider.DocumentFile
import com.example.practica3uno.data.AppDatabase
import com.example.practica3uno.data.FileEntity
import com.example.practica3uno.databinding.FragmentHomeBinding
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FileAdapter
    private var currentFolderUri: Uri? = null

    // Variable para controlar si está en vista cuadrícula o lista
    private var isGrid = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Inicializar RecyclerView con lista por defecto
        binding.recyclerViewFiles.layoutManager = LinearLayoutManager(context)
        adapter = FileAdapter(emptyList(),
            onClick = { file -> onFileClicked(file) },
            onFavorite = { file -> toggleFavorite(file) }
        )
        binding.recyclerViewFiles.adapter = adapter

        // Botón para cambiar entre vista lista/cuadrícula
        binding.btnToggleView.setOnClickListener {
            isGrid = !isGrid
            binding.recyclerViewFiles.layoutManager =
                if (isGrid) GridLayoutManager(context, 2)
                else LinearLayoutManager(context)
        }

        // Abrir selector de carpeta al iniciar
        openFolderPicker()

        return binding.root
    }

    private fun openFolderPicker() {
        val launcher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree()
        ) { uri ->
            if (uri != null) {
                requireContext().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                currentFolderUri = uri
                loadFiles(uri)
                Toast.makeText(context, "Carpeta seleccionada ✅", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No se seleccionó carpeta", Toast.LENGTH_SHORT).show()
            }
        }
        launcher.launch(null)
    }

    private fun loadFiles(uri: Uri) {
        val folder = DocumentFile.fromTreeUri(requireContext(), uri)
        if (folder != null && folder.isDirectory) {
            val files = folder.listFiles().sortedBy { it.name?.lowercase() }
            adapter.updateData(files)
            if (files.isEmpty()) {
                Toast.makeText(context, "La carpeta está vacía", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "No se puede acceder a la carpeta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onFileClicked(file: DocumentFile) {
        addToRecent(file)
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(file.uri, getMimeType(file.name ?: ""))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No se pudo abrir el archivo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleFavorite(file: DocumentFile) {
        val db = AppDatabase.getDatabase(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            val entity = FileEntity(
                uri = file.uri.toString(),
                name = file.name ?: "unknown",
                type = "favorite",
                timestamp = System.currentTimeMillis()
            )
            db.fileDao().insertFile(entity)
        }
        Toast.makeText(context, "Añadido a favoritos ⭐", Toast.LENGTH_SHORT).show()
    }

    private fun addToRecent(file: DocumentFile) {
        val db = AppDatabase.getDatabase(requireContext())
        val entity = FileEntity(
            uri = file.uri.toString(),
            name = file.name ?: "unknown",
            type = "recent",
            timestamp = System.currentTimeMillis()
        )
        CoroutineScope(Dispatchers.IO).launch {
            db.fileDao().insertFile(entity)
        }
    }

    private fun getMimeType(name: String): String {
        return when {
            name.endsWith(".txt", true) -> "text/plain"
            name.endsWith(".md", true) -> "text/plain"
            name.endsWith(".log", true) -> "text/plain"
            name.endsWith(".json", true) -> "application/json"
            name.endsWith(".xml", true) -> "text/xml"
            name.endsWith(".jpg", true) -> "image/jpeg"
            name.endsWith(".jpeg", true) -> "image/jpeg"
            name.endsWith(".png", true) -> "image/png"
            else -> "*/*"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
