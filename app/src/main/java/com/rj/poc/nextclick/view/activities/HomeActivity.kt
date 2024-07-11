package com.rj.poc.nextclick.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.rj.poc.nextclick.R

import com.rj.poc.nextclick.databinding.ActivityHomeBinding
import com.rj.poc.nextclick.view.adapters.FilesAdapter
import com.rj.poc.nextclick.viewmodel.utils.DocumentData
import com.rj.poc.nextclick.viewmodel.utils.getDocumentsFromAssets
import com.rj.poc.nextclick.viewmodel.utils.hide
import com.rj.poc.nextclick.viewmodel.utils.show
import com.rj.poc.nextclick.viewmodel.utils.showToast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var filesAdapter: FilesAdapter
    private var listOfDocuments = arrayListOf<DocumentData>()
    private var selectedFileName = ""
    companion object {
        private const val REQUEST_CODE_READ_STORAGE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.new_back)
        // Set status bar color
        window.statusBarColor = resources.getColor(android.R.color.black, theme)

        listOfDocuments = getDocumentsFromAssets(this) as ArrayList<DocumentData>
        setRV(listOfDocuments)

    }
    private fun setRV(documents : ArrayList<DocumentData>) {
        filesAdapter = FilesAdapter(
            context = this,
            documents = documents,
            onItemClick = {
                val intent = Intent(this, ShowPdfActivity::class.java)
                intent.putExtra("documentName", documents[it].fileName)
                startActivity(intent)
            },
            onDeleteClick = {
                val file = documents[it].fileName
                documents.removeAt(it)
                filesAdapter.notifyDataSetChanged()
                if (documents.isEmpty()) {
                    binding.tvNoDocuments.show()
                }else{
                    binding.tvNoDocuments.hide()
                }
                showToast("$file deleted successfully")
            },
            onDownloadClick = {
                selectedFileName = documents[it].fileName
                checkFileWritePermissions()
            })


        binding.rvDocuments.adapter = filesAdapter
    }
    private fun checkFileWritePermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionsToCheck = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_AUDIO
            )
            val permissionsToRequest = permissionsToCheck.filter {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }.toTypedArray()

            if (permissionsToRequest.isNotEmpty()) {
                requestPermissions(permissionsToRequest, REQUEST_CODE_READ_STORAGE)
            } else {
                saveDocument(selectedFileName)
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }else{
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    saveDocument(selectedFileName)
                }else{
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_CODE_READ_STORAGE)
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE_READ_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    saveDocument(selectedFileName)
                }else{
                    showToast(getString(R.string.permission_denied_to_read_storage))
                }
            }
        }
    }

    private fun saveDocument(name: String) {
        val context = applicationContext
        val folderName = context.getString(R.string.app_name)
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(name)

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val appFolder = File(downloadsDir, folderName)
            if (!appFolder.exists()) {
                appFolder.mkdirs()
            }

            val outFile = File(appFolder, name)
            val outputStream = FileOutputStream(outFile)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            showToast("File saved to ${outFile.absolutePath}")
        } catch (e: IOException) {
            e.printStackTrace()
            showToast("Failed to save file: ${e.message}")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}