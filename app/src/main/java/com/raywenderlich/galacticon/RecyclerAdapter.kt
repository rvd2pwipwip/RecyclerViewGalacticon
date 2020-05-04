package com.raywenderlich.galacticon

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

//add a variable photos in the primary constructor to hold your photos
class RecyclerAdapter(private val photos: ArrayList<Photo>): RecyclerView.Adapter<RecyclerAdapter.PhotoHolder>() {

    //1. make the class extend RecyclerView.ViewHolder, allowing the adapter to use it as as a ViewHolder.
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2. add a reference to the view you’ve inflated to allow the ViewHolder to access the ImageView and TextView as an extension property.
        //Kotlin Android Extensions plugin adds hidden caching functions and fields to prevent the constant querying of views.
        private var view: View = v
        private var photo: Photo? = null
        //3. initialize the View.OnClickListener
        init {
            v.setOnClickListener(this)
        }
        //4. implement the required method for View.OnClickListener since ViewHolders are responsible for their own event handling
        override fun onClick(v: View) {
//            Log.d("RecyclerView", "CLICK!")
            val context = itemView.context
            val showPhotoIntent = Intent(context, PhotoActivity::class.java)
            showPhotoIntent.putExtra(PHOTO_KEY, photo)
            context.startActivity(showPhotoIntent)

        }

        companion object {
            //5. add a key for easy reference to the item launching the RecyclerView
            private val PHOTO_KEY = "PHOTO"
        }

        //This binds the photo to the PhotoHolder, giving your item the data it needs to work out what it should show.
        fun bindPhoto(photo: Photo) {
            this.photo = photo
            Picasso.with(view.context).load(photo.url).into(view.itemImage)
            view.itemDate.text = photo.humanDate
            view.itemDescription.text = photo.explanation
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.PhotoHolder {
        // use inflate extension function from Extensions.kt to inflate layout and return view holder
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row, false)
        return PhotoHolder(inflatedView)
    }

    override fun getItemCount() = photos.size

    //show the right photo at the right moment
    override fun onBindViewHolder(holder: RecyclerAdapter.PhotoHolder, position: Int) {
        val itemPhoto = photos[position]
        holder.bindPhoto(itemPhoto)
    }
}