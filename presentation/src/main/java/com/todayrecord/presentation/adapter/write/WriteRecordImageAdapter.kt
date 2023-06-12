package com.todayrecord.presentation.adapter.write

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.todayrecord.presentation.R
import com.todayrecord.presentation.databinding.ItemSelectedImageBinding
import com.todayrecord.presentation.screen.write.WriteRecordClickListener
import com.todayrecord.presentation.util.executeAfter
import com.todayrecord.presentation.util.listener.setOnSingleClickListener

class WriteRecordImageAdapter(
    private val writeRecordClickListener: WriteRecordClickListener
) : ListAdapter<String, WriteRecordImageViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WriteRecordImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WriteRecordImageViewHolder(ItemSelectedImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: WriteRecordImageViewHolder, position: Int) {
        holder.binding.executeAfter {
            imageUrl = getItem(position)
            clSelectedImage.setOnSingleClickListener {
                writeRecordClickListener.onRecordImageDeletedListener(imageUrl.toString())
            }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_selected_image
}