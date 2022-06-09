package com.darkshandev.freshcam.utils

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.darkshandev.freshcam.R


fun ImageView.loadCircleImage(url: String, isExpand: Boolean = false) {
    val defaultOption = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .error(R.drawable.ic_baseline_broken_image_24)
    val requestBuilder: RequestBuilder<Drawable> = Glide.with(context)
        .asDrawable().sizeMultiplier(0.5f)
    if (url.isEmpty()) {
        Glide.with(context)
            .setDefaultRequestOptions(defaultOption)
            .load(
                ColorDrawable(ContextCompat.getColor(context, R.color.secondary))
            ).thumbnail(requestBuilder)
            .into(this)
    } else {
        val builder = Glide.with(context)
            .setDefaultRequestOptions(defaultOption)
            .load(
                url
            ).thumbnail(requestBuilder)

        if (isExpand) {
            builder.into(this)
        } else {
            builder.centerCrop().into(this)

        }
    }
}

