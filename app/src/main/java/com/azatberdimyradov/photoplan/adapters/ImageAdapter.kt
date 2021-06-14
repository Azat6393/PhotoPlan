package com.azatberdimyradov.photoplan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azatberdimyradov.photoplan.databinding.ItemImageRecyclerViewBinding
import com.azatberdimyradov.photoplan.models.Image
import com.azatberdimyradov.photoplan.models.Location
import com.bumptech.glide.Glide


class ImageAdapter(val listener: OnItemClickListener) :
    ListAdapter<Image, ImageAdapter.ImageViewHolder>(ImageDiffCallBack) {

    private var isEnable = false
    private var imageList = arrayListOf<Image>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ItemImageRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(currentItem)
        }
    }

    inner class ImageViewHolder(private val binding: ItemImageRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemImageRecyclerViewImage.setOnLongClickListener {
                binding.apply {
                    isEnable = !itemImageRecyclerViewCheckBox.isVisible
                    listener.onLongClicked()
                    notifyDataSetChanged()
                }
                true
            }
            binding.itemImageRecyclerViewCheckBox.setOnCheckedChangeListener { _, isChecked ->
                getImage(adapterPosition){
                    if (isChecked) {
                        imageList.add(it)
                    } else {
                        imageList.remove(it)
                    }
                }
                listener.onCheckedClicked(imageList)
            }
            binding.itemImageRecyclerViewImage.setOnClickListener {
                getImage(adapterPosition){
                    listener.onImageClicked(it)
                }
            }
        }

        fun bind(image: Image) {
            binding.apply {
                itemImageRecyclerViewFrameLayout.isVisible = isEnable
                itemImageRecyclerViewCheckBox.isVisible = isEnable
                Glide.with(itemView)
                    .load(image.imageUrl)
                    .centerCrop()
                    .into(itemImageRecyclerViewImage)
            }
        }
    }

    companion object {
        val ImageDiffCallBack = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem == newItem
        }
    }

    private fun getImage(adapterPosition: Int, function: (Image) -> Unit) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            val image = getItem(adapterPosition)
            image?.let {
                function(image)
            }
        }
    }

    interface OnItemClickListener {
        fun onImageClicked(image: Image)
        fun onCheckedClicked(imageList: ArrayList<Image>)
        fun onLongClicked()
    }
}