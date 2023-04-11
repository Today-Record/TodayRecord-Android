package com.todayrecord.todayrecord.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.todayrecord.todayrecord.R


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        if (!isValidContextForGlide(view.context)) return

        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.color.color_474a54)
            .error(R.color.color_474a54)
            .into(view)
    }
}

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean?) {
    if (isGone == null || isGone) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

private fun isValidContextForGlide(context: Context): Boolean {
    if (context is Activity) {
        return !context.isFinishing
    }
    return true
}