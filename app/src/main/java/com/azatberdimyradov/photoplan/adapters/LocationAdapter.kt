package com.azatberdimyradov.photoplan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azatberdimyradov.photoplan.databinding.ItemRecyclerViewBinding
import com.azatberdimyradov.photoplan.models.Image
import com.azatberdimyradov.photoplan.models.Location

class LocationAdapter(private val listener: ItemListener) :
    ListAdapter<Location, LocationAdapter.LocationViewHolder>(LocationDiffCallBack) {

    private var deleteList = listOf<Image>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding =
            ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(location = currentItem)
        }
    }

    inner class LocationViewHolder(private val binding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root), ImageAdapter.OnItemClickListener {

        init {
            binding.apply {
                locationNameEditText.addTextChangedListener { text ->
                    getLocation(adapterPosition) { location ->
                        listener.onTextChanged(text.toString(), location)
                    }
                }
                addPhotoButton.setOnClickListener {
                    getLocation(adapterPosition) { location ->
                        listener.addPhoto(location)
                    }
                }
                deleteButton.setOnClickListener {
                    getLocation(adapterPosition) {
                        listener.onClickDeleteButton(deleteList, it)
                    }
                }
            }
        }

        fun bind(location: Location) {
            binding.apply {
                locationNameEditText.setText(location.locationName)
                locationNameEditText.setSelection(locationNameEditText.text.toString().length)
                val imageAdapter = ImageAdapter(this@LocationViewHolder)
                imagesRecyclerView.apply {
                    adapter = imageAdapter
                    layoutManager = GridLayoutManager(context, 3)
                    setHasFixedSize(true)
                }
                imagesRecyclerView.isVisible = location.imageList?.isNotEmpty() ?: false
                imageAdapter.submitList(location.imageList)
            }
        }

        override fun onImageClicked(image: Image){
            listener.onImageClicked(image)
        }

        override fun onCheckedClicked(imageList: ArrayList<Image>) {
            deleteList = imageList
        }

        override fun onLongClicked() {
            binding.deleteButton.isVisible = true
        }
    }

    companion object {
        val LocationDiffCallBack = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean =
                oldItem == newItem
        }
    }

    private fun getLocation(adapterPosition: Int, function: (Location) -> Unit) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            val location = getItem(adapterPosition)
            location?.let {
                function(location)
            }
        }
    }

    interface ItemListener {
        fun addPhoto(location: Location)
        fun onTextChanged(locationName: String, location: Location)
        fun onImageClicked(image: Image)
        fun onClickDeleteButton(deleteList: List<Image>, location: Location)
    }
}