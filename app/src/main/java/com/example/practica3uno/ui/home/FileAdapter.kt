package com.example.practica3uno.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.documentfile.provider.DocumentFile
import com.example.practica3uno.R

class FileAdapter(
    private var files: List<DocumentFile>,
    private val onClick: (DocumentFile) -> Unit,
    private val onFavorite: ((DocumentFile) -> Unit)? = null // Opcional
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    fun updateData(newFiles: List<DocumentFile>) {
        files = newFiles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.name.text = file.name
        holder.icon.setImageResource(
            if (file.isDirectory) R.drawable.ic_folder else R.drawable.ic_folder
        )

        holder.itemView.setOnClickListener { onClick(file) }

        // Si el icono de favorito existe y se pasó la función, lo activamos
        holder.favorite?.setOnClickListener {
            onFavorite?.invoke(file)
        }
    }

    override fun getItemCount(): Int = files.size

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.fileIcon)
        val name: TextView = itemView.findViewById(R.id.fileName)
        val favorite: ImageView? = itemView.findViewById(R.id.favoriteIcon) // opcional
    }
}
