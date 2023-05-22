package com.sushmita.downloadimage.model.helperclasses

class DownloadAdapterItemModel(img: String) : java.io.Serializable{

    var imgURL: String? = null

    init {
        this.imgURL = img
    }

}