/*
 * Copyright (c) 2017 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.galacticon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), ImageRequester.ImageRequesterResponse {

    private lateinit var adapter: RecyclerAdapter

    private lateinit var gridLayoutManager: GridLayoutManager

    //add linear layout manager property
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var photosList: ArrayList<Photo> = ArrayList()
    private lateinit var imageRequester: ImageRequester


    ///////// Scroll Support /////////
    //find last visible item position for scroll support
    private val lastVisibleItemPosition: Int
        //Here, you ask the RecyclerView to tell you what its LayoutManager is.
        //Then you ask that LayoutManager to tell you the position of the last visible item.
        get() = if (recyclerView.layoutManager == linearLayoutManager) {
            linearLayoutManager.findLastVisibleItemPosition()
        } else {
            gridLayoutManager.findLastVisibleItemPosition()
        }


    //insert an onScrollListener to RecyclerView, so it can get a callback when the user scrolls
    private fun setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                if (!imageRequester.isLoadingData && totalItemCount == lastVisibleItemPosition + 1) {
                    requestPhoto()
                }
            }
        })
    }

    ///////// Scroll Support /////////


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayoutManager = LinearLayoutManager(this)
        //reference the recycler view in activity_main.xml
        //and assign it the linear layout manager
        recyclerView.layoutManager = linearLayoutManager

        gridLayoutManager = GridLayoutManager(this, 2)

        //connect the recycler view to the adapter
        adapter = RecyclerAdapter(photosList)
        recyclerView.adapter = adapter

        setRecyclerViewScrollListener()

        imageRequester = ImageRequester(this)
    }

  override fun onStart() {
    super.onStart()

        if (photosList.size == 0) {
            requestPhoto()
        }

    }

    // To use the grid layout, make use of the Options menu button that is already available in the app
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_recycler_manager) {
            changeLayoutManager()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun requestPhoto() {
        try {
            imageRequester.getPhoto()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun receivedNewPhoto(newPhoto: Photo) {
        runOnUiThread {
            photosList.add(newPhoto)
            //inform the recycler adapter that you added an item after updating the list of photos
            adapter.notifyItemInserted(photosList.size - 1)
        }
    }

    //This code checks to see what LayoutManager your RecyclerView is using, and then:
    private fun changeLayoutManager() {
        if (recyclerView.layoutManager == linearLayoutManager) {
            //1. If it’s using the LinearLayoutManager, it swaps in the GridLayoutManager.
            recyclerView.layoutManager = gridLayoutManager
            //2. It requests a new photo if your grid layout only has one photo to show.
            if (photosList.size == 1) {
                requestPhoto()
            }
        } else {
            //3. If it’s using the GridLayoutManager, it swaps in the LinearLayoutManager.
            recyclerView.layoutManager = linearLayoutManager
        }
    }

}


