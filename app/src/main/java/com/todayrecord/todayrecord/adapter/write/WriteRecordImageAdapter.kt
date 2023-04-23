package com.todayrecord.todayrecord.adapter.write

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.ItemWriteRecordImageBinding
import com.todayrecord.todayrecord.screen.write.WriteRecordClickListener
import com.todayrecord.todayrecord.util.executeAfter
import com.todayrecord.todayrecord.util.setOnSingleClickListener

class WriteRecordImageAdapter(
    private val writeRecordClickListener: WriteRecordClickListener
) : ListAdapter<String, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WriteRecordImageViewHolder(ItemWriteRecordImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WriteRecordImageViewHolder).binding.executeAfter {
            imageUrl = getItem(position)
            clWriteRecordImage.setOnSingleClickListener {
                writeRecordClickListener.onRecordImageDeletedListener(imageUrl.toString())
            }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_write_record_image
}