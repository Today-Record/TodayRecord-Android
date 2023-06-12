package com.todayrecord.presentation.adapter.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.todayrecord.presentation.R
import com.todayrecord.presentation.databinding.ItemSelectedImageBinding
import com.todayrecord.presentation.screen.mediapicker.MediaPickerClickListener
import com.todayrecord.presentation.util.executeAfter
import com.todayrecord.presentation.util.listener.setOnSingleClickListener

class SelectedMediaPathAdapter(
    private val mediaPickerClickListener: MediaPickerClickListener
) : ListAdapter<String, SelectedMediaPathViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMediaPathViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SelectedMediaPathViewHolder(ItemSelectedImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: SelectedMediaPathViewHolder, position: Int) {
        holder.binding.executeAfter {
            imageUrl = getItem(position)
            clSelectedImage.setOnSingleClickListener {
                mediaPickerClickListener.setSelectedMedia(imageUrl.toString())
            }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_media
}