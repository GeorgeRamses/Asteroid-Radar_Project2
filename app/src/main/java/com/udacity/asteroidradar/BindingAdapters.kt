package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AstroidAdapter
import java.net.URL

@BindingAdapter("listData")
fun setData(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AstroidAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageDayUrl")
fun bindImageofDay(imageView: ImageView, pictureofDay: PictureOfDay?) {

    pictureofDay.let {
        if (it != null) {
            if (it.mediaType == "image") {
                val imgUri = it.url.toUri().buildUpon().scheme("https").build()
                Picasso.get().load(imgUri).placeholder(R.drawable.loading_animation)
                    .error(R.drawable.placeholder_error)
                    .into(imageView)
                imageView.contentDescription =
                    imageView.context.getString(R.string.nasa_picture_of_day_content_description_format)
            } else {
                imageView.setImageResource(R.drawable.image_not_available)
                imageView.contentDescription =
                    imageView.context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
            }
        }
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("nameText")
fun bindTextViewToName(textView: TextView, text: String) {
    val context = textView.context
    textView.text = text
}

@BindingAdapter("dateText")
fun bindTextViewToDate(textView: TextView, date: String) {
    val context = textView.context
    textView.text = date
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
