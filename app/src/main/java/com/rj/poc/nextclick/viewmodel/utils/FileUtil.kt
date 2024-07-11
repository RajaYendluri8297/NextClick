package com.rj.poc.nextclick.viewmodel.utils

import android.content.Context
import java.io.IOException

data class DocumentData(
    val fileName: String,
    val fileSize: Long
)

fun getDocumentsFromAssets(context: Context): List<DocumentData> {
    val documentsList = mutableListOf<DocumentData>()
    val assetManager = context.assets
    try {
        val files = assetManager.list("")
        files?.forEach { fileName ->
            try {
                val inputStream = assetManager.open(fileName)
                val fileSize = inputStream.available().toLong()
                inputStream.close()
                val document = DocumentData(fileName, fileSize)
                documentsList.add(document)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return documentsList
}
