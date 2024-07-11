package com.rj.poc.nextclick.view.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rj.poc.nextclick.R
import com.rj.poc.nextclick.databinding.ActivityShowPdfBinding
import com.rj.poc.nextclick.viewmodel.utils.hide
import com.rj.poc.nextclick.viewmodel.utils.show

class ShowPdfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowPdfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.new_back)
        // Set status bar color
        window.statusBarColor = resources.getColor(android.R.color.black, theme)

        val pdfName = intent.getStringExtra("documentName")
        if (!pdfName.isNullOrEmpty()) {
            binding.fileError.hide()
            binding.pdfView.fromAsset(pdfName).load()
        } else {
            binding.fileError.show()
            binding.pdfView.hide()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
