package com.todayrecord.presentation.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.todayrecord.presentation.R

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    if (imageUrl.isNullOrEmpty()) return

    imageView.load(imageUrl) {
        placeholder(R.color.color_d9d9d9)
        error(R.color.color_474a54)
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