package com.todayrecord.presentation.adapter.recorddetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.todayrecord.presentation.R
import com.todayrecord.presentation.databinding.ItemRecordDetailImageBinding
import com.todayrecord.presentation.databinding.ItemRecordDetailTextBinding
import com.todayrecord.presentation.screen.recorddetail.RecordDetailItemUiModel
import com.todayrecord.presentation.util.executeAfter

class RecordDetailAdapter : ListAdapter<RecordDetailItemUiModel, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<RecordDetailItemUiModel>() {
        override fun areItemsTheSame(oldItem: RecordDetailItemUiModel, newItem: RecordDetailItemUiModel): Boolean {
            return (oldItem is RecordDetailItemUiModel.RecordDetailImageItem && newItem is RecordDetailItemUiModel.RecordDetailImageItem && oldItem.imageUrl == newItem.imageUrl) ||
                    (oldItem is RecordDetailItemUiModel.RecordDetailTextItem && newItem is RecordDetailItemUiModel.RecordDetailTextItem && oldItem.text == newItem.text)
        }

        override fun areContentsTheSame(oldItem: RecordDetailItemUiModel, newItem: RecordDetailItemUiModel): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.item_record_detail_image -> RecordDetailImageViewHolder(ItemRecordDetailImageBinding.inflate(inflater, parent, false))
            R.layout.item_record_detail_text -> RecordDetailTextViewHolder(ItemRecordDetailTextBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val uiModel = getItem(position)) {
            is RecordDetailItemUiModel.RecordDetailImageItem -> {
                (holder as RecordDetailImageViewHolder).binding.executeAfter {
                    imageUrl = uiModel.imageUrl
                }
            }

            is RecordDetailItemUiModel.RecordDetailTextItem -> {
                (holder as RecordDetailTextViewHolder).binding.executeAfter {
                    tvRecordDetail.text = uiModel.text
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecordDetailItemUiModel.RecordDetailImageItem -> R.layout.item_record_detail_image
            is RecordDetailItemUiModel.RecordDetailTextItem -> R.layout.item_record_detail_text
        }
    }
}