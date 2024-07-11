package com.rj.poc.nextclick.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rj.poc.nextclick.R
import com.rj.poc.nextclick.viewmodel.utils.DocumentData
import com.rj.poc.nextclick.viewmodel.utils.toMB

class FilesAdapter(
    private val context: Context,
    private val documents: ArrayList<DocumentData>,
    private val onItemClick: (Int) -> Unit,
    private val onDownloadClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<FilesAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pdf, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = documents[position]
        holder.bindData(currentItem,position)
    }

    override fun getItemCount(): Int {
        return documents.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val fileNameTextView: TextView = itemView.findViewById(R.id.textFileName)
        private val filesizeTextView: TextView = itemView.findViewById(R.id.textFileSize)
        private val imageViewMain: ImageView = itemView.findViewById(R.id.imageViewMain)
        private val buttonDownload: ImageView = itemView.findViewById(R.id.buttonDownload)
        private val buttonDelete: ImageView = itemView.findViewById(R.id.buttonDelete)
        private val cardView: CardView = itemView.findViewById(R.id.cardViewItem)

        fun bindData(item: DocumentData, position: Int) {
            fileNameTextView.text = item.fileName
        filesizeTextView.text = "${item.fileSize.toMB().toString()} MB"

            cardView.setOnClickListener {
                onItemClick(position)
            }
            buttonDownload.setOnClickListener {
                onDownloadClick(position)
            }
            buttonDelete.setOnClickListener {
                onDeleteClick(position)
            }
        }

    }
}

