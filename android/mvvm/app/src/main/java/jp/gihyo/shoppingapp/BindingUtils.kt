package jp.gihyo.shoppingapp

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

@BindingAdapter("android:src")
fun setImageUrl(image: ImageView, imageUrl: String) {
    Picasso.get().load(imageUrl).into(image)
}