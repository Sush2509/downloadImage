package com.sushmita.downloadimage.model.helperclasses

import androidx.databinding.ObservableField

class GridItemModel(img: String) : java.io.Serializable {

    var imgURL: String? = null
    val isCheckboxChecked = ObservableField(false)

    init {
        this.imgURL = img
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as GridItemModel
        if (imgURL != other.imgURL) return false
        return true
    }

    override fun hashCode(): Int {
        return imgURL?.hashCode() ?: 0
    }

}