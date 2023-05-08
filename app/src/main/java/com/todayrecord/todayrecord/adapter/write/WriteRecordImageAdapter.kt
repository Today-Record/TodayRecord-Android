package com.todayrecord.todayrecord.adapter.write

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.ItemSelectedImageBinding
import com.todayrecord.todayrecord.screen.write.WriteRecordClickListener
import com.todayrecord.todayrecord.util.executeAfter
import com.todayrecord.todayrecord.util.listener.setOnSingleClickListener

class WriteRecordImageAdapter(
    private val writeRecordClickListener: WriteRecordClickListener,
    private val selectedImagePaths: MutableList<String> = mutableListOf()
) : RecyclerView.Adapter<WriteRecordImageViewHolder>() {

    private val diffCallback = WriteRecordImageDiffCallback(selectedImagePaths, emptyList())

    fun submitList(updatedList: List<String>) {
        diffCallback.newList = updatedList
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        selectedImagePaths.clear()
        selectedImagePaths.addAll(updatedList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WriteRecordImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WriteRecordImageViewHolder(ItemSelectedImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: WriteRecordImageViewHolder, position: Int) {
        holder.binding.executeAfter {
            imageUrl = selectedImagePaths[position]
            clSelectedImage.setOnSingleClickListener {
                writeRecordClickListener.onRecordImageDeletedListener(imageUrl.toString())
            }
        }
    }

    override fun getItemCount(): Int = selectedImagePaths.size

    override fun getItemViewType(position: Int) = R.layout.item_selected_image

    inner class WriteRecordImageDiffCallback(
        private val oldList: List<String>,
        var newList: List<String>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}