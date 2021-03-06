package com.karine.retrieve.ui

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jama.carouselview.CarouselView
import com.karine.retrieve.R

class Carousel : BaseActivity() {

    companion object {
        fun carousel(carouselView: CarouselView, photoList: MutableList<Uri>) {
            carouselView.apply {
                size = photoList.size
                resource = R.layout.centered_carousel
                setCarouselViewListener { view, position ->
                    val imageView = view.findViewById<ImageView>(R.id.imageView)
                    imageView.setImageURI(photoList[position])
                }
                show()
            }
        }

        fun carouselFromUrl(carouselView: CarouselView, photoList: MutableList<String>) {
            carouselView.apply {
                size = photoList.size
                resource = R.layout.centered_carousel
                setCarouselViewListener { view, position ->
                    val imageView = view.findViewById<ImageView>(R.id.imageView)
                    Glide.with(imageView.context).load(photoList[position]).into(imageView)
                }
                show()
            }
        }
    }
}