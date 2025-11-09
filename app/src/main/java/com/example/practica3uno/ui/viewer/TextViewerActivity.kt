package com.example.practica3uno.ui.viewer

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practica3uno.R
import java.io.BufferedReader
import java.io.InputStreamReader
class TextViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_viewer)

        val textView: TextView = findViewById(R.id.textView)

        val textUri: Uri? = intent.data
        if (textUri != null) {
            try {
                val inputStream = contentResolver.openInputStream(textUri)
                val reader = BufferedReader(InputStreamReader(inputStream))
                val text = reader.readText()
                reader.close()
                textView.text = text
            } catch (e: Exception) {
                Toast.makeText(this, "No se pudo abrir el archivo de texto", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
