package com.raywenderlich.galacticon

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup

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
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            //5. add a key for easy reference to the item launching the RecyclerView
            private val PHOTO_KEY = "PHOTO"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.PhotoHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(holder: RecyclerAdapter.PhotoHolder, position: Int) {
        TODO("Not yet implemented")
    }
}