package com.todayrecord.todayrecord.adapter.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.ItemMediaBinding
import com.todayrecord.todayrecord.model.media.Media
import com.todayrecord.todayrecord.screen.mediapicker.MediaPickerClickListener
import com.todayrecord.todayrecord.util.executeAfter
import com.todayrecord.todayrecord.util.setOnSingleClickListener

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