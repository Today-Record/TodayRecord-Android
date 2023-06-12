package com.todayrecord.presentation.adapter.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.todayrecord.presentation.R
import com.todayrecord.presentation.databinding.ItemMediaBinding
import com.todayrecord.presentation.model.media.Media
import com.todayrecord.presentation.screen.mediapicker.MediaPickerClickListener
import com.todayrecord.presentation.util.executeAfter
import com.todayrecord.presentation.util.listener.setOnSingleClickListener

class MediaPickerAdapter(
    private val mediaPickerClickListener: MediaPickerClickListener
) : PagingDataAdapter<Media, MediaPickerViewHolder>(
    object : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
            return (oldItem.uri == newItem.uri || oldItem.selectedPosition == newItem.selectedPosition)
        }

        override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPickerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MediaPickerViewHolder(ItemMediaBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: MediaPickerViewHolder, position: Int) {
        holder.binding.executeAfter {
            media = getItem(position)
            lifecycleOwner = root.findViewTreeLifecycleOwner()

            ivMedia.setOnSingleClickListener {
                mediaPickerClickListener.setSelectedMedia(getItem(position)?.uri.toString())
            }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_media
}