package com.example.practica3uno.ui.viewer

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practica3uno.R

class ImageViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        val imageView: ImageView = findViewById(R.id.imageView)

        val imageUri: Uri? = intent.data
        if (imageUri != null) {
            imageView.setImageURI(imageUri)
        } else {
            Toast.makeText(this, "No se pudo abrir la imagen", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
