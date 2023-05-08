package com.todayrecord.todayrecord.adapter.record

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.widget.ViewPager2
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.ItemRecordBinding
import com.todayrecord.todayrecord.model.record.Record
import com.todayrecord.todayrecord.screen.record.RecordClickListener
import com.todayrecord.todayrecord.util.executeAfter
import com.todayrecord.todayrecord.util.listener.setOnSingleClickListener

class RecordAdapter(
    private val recordClickListener: RecordClickListener
) : PagingDataAdapter<Record, RecordViewHolder>(
    object : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecordViewHolder(ItemRecordBinding.inflate(inflater, parent, false))
            .also {
                it.binding.vpImage.apply {
                    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            it.binding.tbRecordImage.apply {
                                selectTab(getTabAt(position), true)
                            }
                        }
                    })
                }
            }
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.binding.executeAfter {
            record = getItem(position)
            vpImage.adapter = RecordImageAdapter().apply {
                submitList(record?.images)
            }

            root.setOnSingleClickListener {
                record?.let { recordClickListener.onRecordClick(it.id) }
            }

            setViewPagerTouchEvent(this)
            calculateTabIndicator(this, getItem(position)?.images?.size ?: 0)
        }
    }

    private fun calculateTabIndicator(binding: ItemRecordBinding, indicatorSize: Int) {
        binding.tbRecordImage.let {
            it.removeAllTabs()
            for (index in 0 until indicatorSize) {
                it.addTab(it.newTab())
            }

            it.getChildAt(0).isEnabled = false
            for (index in 0..(it.getChildAt(0) as LinearLayout).childCount) {
                (it.getChildAt(0) as? LinearLayout)?.getChildAt(index)?.isClickable = false
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setViewPagerTouchEvent(binding: ItemRecordBinding) {
        binding.apply {
            vpImage.getChildAt(0).setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> {
                        if (vpImage.scrollState == ViewPager2.SCROLL_STATE_IDLE) root.onTouchEvent(event)
                    }
                }
                v.onTouchEvent(event)
                return@setOnTouchListener true
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_record
}