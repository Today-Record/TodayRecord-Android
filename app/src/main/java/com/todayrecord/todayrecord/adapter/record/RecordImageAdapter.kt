package com.todayrecord.todayrecord.adapter.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.ItemRecordImageBinding
import com.todayrecord.todayrecord.util.executeAfter

class RecordImageAdapter : ListAdapter<String, RecordImageViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordImageViewHolder {
        return RecordImageViewHolder(
            ItemRecordImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecordImageViewHolder, position: Int) {
        holder.binding.executeAfter {
            imageUrl = getItem(position)
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_record_image

}