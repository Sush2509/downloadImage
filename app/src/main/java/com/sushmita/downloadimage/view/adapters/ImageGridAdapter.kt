package com.sushmita.downloadimage.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sushmita.downloadimage.R
import com.sushmita.downloadimage.databinding.ImagesItemBinding
import com.sushmita.downloadimage.model.helperclasses.GridItemModel

class ImageGridAdapter(
    context: Context,
    _imageItemList: List<GridItemModel>, _selectedItems: MutableLiveData<MutableList<GridItemModel>>?
) : RecyclerView.Adapter<ImageGridAdapter.ImagesItemViewHolder>() {

    private var imageItemList = _imageItemList
    private var mContext = context
    private var selectedItems = _selectedItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesItemViewHolder {
        val binding = ImagesItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagesItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageItemList.size
    }

    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ImagesItemViewHolder, position: Int) {
        val item = imageItemList[position]
        holder.imagesItemBinding.adapterItemModel = item
        //If image is not loaded, add the placeholder for the meantime
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.placeholder_image)
        holder.imagesItemBinding.breedIV.setOnLongClickListener {
            item.isCheckboxChecked.set(!item.isCheckboxChecked.get()!!)
            if(item.isCheckboxChecked.get() == true){
                selectedItems?.value?.add(item)
            }else{
                selectedItems?.value?.remove(item)
            }
            selectedItems?.value = selectedItems?.value
            true
        }
        try {
            //Set Image to imageView
            if (!item.imgURL.isNullOrBlank()) {
                Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(item.imgURL)
                    .into(holder.imagesItemBinding.breedIV)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    //Viewholder
    inner class ImagesItemViewHolder(_imagesItemBinding: ImagesItemBinding) :
        RecyclerView.ViewHolder(_imagesItemBinding.root) {
        val imagesItemBinding = _imagesItemBinding
    }

}
