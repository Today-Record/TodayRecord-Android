package com.todayrecord.todayrecord.adapter.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.widget.ViewPager2
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.ItemRecordBinding
import com.todayrecord.todayrecord.model.record.Record
import com.todayrecord.todayrecord.screen.record.RecordClickListener
import com.todayrecord.todayrecord.util.executeAfter
import com.todayrecord.todayrecord.util.listener.setOnSingleClickListener
import com.todayrecord.todayrecord.util.toPx
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 *  Record 목록 adapter
 *
 *  @Author JK Lee
 *  on 2023/04/26
 */
class RecordsAdapter(
    private val recordClickListener: RecordClickListener
): PagingDataAdapter<Record, RecordsViewHolder>(
    object : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecordsViewHolder(ItemRecordBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecordsViewHolder, position: Int) {
        holder.binding.executeAfter {
            getItem(position)?.apply {
                tvPreview.text = content

                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\na hh:mm")
                val parsedTime = ZonedDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)

                tvDatetime.text = parsedTime.format(dateFormatter)

                if (images.isNotEmpty()) {
                    vpImage.visibility = View.VISIBLE



                    val sliderAdapter = SliderAdapter()
                    sliderAdapter.submitList(images)

                    vpImage.adapter = sliderAdapter

                    vpImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)

                            setCurrentIndicator(this@executeAfter, position)
                        }
                    })

                    if (images.size > 1) {
                        vpIndicator.visibility = View.VISIBLE
                        initIndicator(this@executeAfter, images.size)

                        vpIndicator.getChildAt(0).isSelected = true
                    }
                }

                tvPreview.setOnSingleClickListener {
                    recordClickListener.onRecordClick(id)
                }
            }
        }
    }
    private fun initIndicator(binding: ItemRecordBinding, count: Int) {
        val params = LinearLayout.LayoutParams(8.toPx(binding.root.context), 8.toPx(binding.root.context))
        val context = binding.root.context

        params.setMargins(3.toPx(binding.root.context), 0, 3.toPx(binding.root.context), 0)

        for (i in 0 until count) {
            val indicatorImage = ImageView(context)
            indicatorImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.selected_image_indicator))
            indicatorImage.layoutParams = params

            binding.vpIndicator.addView(indicatorImage)
        }
    }

    private fun setCurrentIndicator(binding: ItemRecordBinding, position: Int) {
        for (i in 0 until binding.vpIndicator.childCount) {
            binding.vpIndicator.getChildAt(i).isSelected = (i == position)
        }
    }
}