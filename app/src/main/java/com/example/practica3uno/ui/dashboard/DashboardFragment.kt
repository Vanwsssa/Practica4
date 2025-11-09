package com.example.practica3uno.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // RecyclerView archivos recientes
        adapterRecent = FileAdapter(emptyList(), onClick = { file -> openFile(file) })
        binding.recyclerRecent.layoutManager = LinearLayoutManager(context)
        binding.recyclerRecent.adapter = adapterRecent

        // RecyclerView archivos favoritos
        adapterFavorite = FileAdapter(emptyList(), onClick = { file -> openFile(file) })
        binding.recyclerFavorite.layoutManager = LinearLayoutManager(context)
        binding.recyclerFavorite.adapter = adapterFavorite

        loadData()

        return binding.root
    }

    private fun loadData() {
        val db = AppDatabase.getDatabase(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            val recentFiles = db.fileDao().getRecentFiles()
            val favoriteFiles = db.fileDao().getFavoriteFiles()

            withContext(Dispatchers.Main) {
                adapterRecent.updateData(recentFiles.map { toDocumentFile(it) })
                adapterFavorite.updateData(favoriteFiles.map { toDocumentFile(it) })
            }
        }
    }

    private fun toDocumentFile(file: FileEntity): DocumentFile {
        // Convertimos URI de String a DocumentFile para abrirlo
        val uri = Uri.parse(file.uri)
        return DocumentFile.fromSingleUri(requireContext(), uri)!!
    }

    private fun openFile(file: DocumentFile) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(file.uri, getMimeType(file.name ?: ""))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No se pudo abrir el archivo", Toast.LENGTH_SHORT).show()
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
