package com.sushmita.downloadimage.viewmodel

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sushmita.downloadimage.R
import com.sushmita.downloadimage.db.AnimalDatabase
import com.sushmita.downloadimage.db.dto.Animal
import com.sushmita.downloadimage.db.repositry.AnimalRepository
import com.sushmita.downloadimage.model.helperclasses.GridItemModel
import com.sushmita.downloadimage.model.ws.ImageListResponse
import com.sushmita.downloadimage.network.ApiCallListener
import com.sushmita.downloadimage.network.ApiClient
import com.sushmita.downloadimage.network.ApiConstants
import com.sushmita.downloadimage.network.RetrofitApiCall
import com.sushmita.downloadimage.utils.AppCons
import com.sushmita.downloadimage.utils.AppLogger
import com.sushmita.downloadimage.view.fragment.MyDownloadFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class GridFragmentVM @Inject constructor(application: Application) : AndroidViewModel(application),
    ApiCallListener {

    private var animalRepository: AnimalRepository? = null
    var visibleContinue = ObservableBoolean(true) // /To maintain the visibility of "Continue" button
    var isVisible = ObservableBoolean(false) //To maintain the visibility of loader

    private var adapterItemList = MutableLiveData<MutableList<GridItemModel>>(arrayListOf())
    private var selectedItems = MutableLiveData<MutableList<GridItemModel>>(arrayListOf())
    private val handler: Handler = Handler(Looper.getMainLooper())

    init {
        animalRepository = AnimalRepository(AnimalDatabase.getDatabase(application).getAnimalDao())
        hitImageList()
    }

    //Get categories as per the brands
    private fun hitImageList() {
        isVisible.set(true)
        RetrofitApiCall.hitApi(
            ApiClient.apiInterFace.getImageList(),
            this,
            ApiConstants.API_END_POINT.IMAGES
        )
    }

    override fun onSuccess(response: String, requestUrl: String) {
        if (requestUrl == ApiConstants.API_END_POINT.IMAGES) {
            processImageListResponse(response)
        }
    }

    override fun onError(response: String, requestUrl: String) {
        if (requestUrl == ApiConstants.API_END_POINT.IMAGES) {
            isVisible.set(false)
            AppLogger.d("onError : ","Error in fetching list")
            loadFromDataBase()
        }
    }

    // Handle image list response and insert data in database
    private fun processImageListResponse(response: String) {
        try {
            Log.d("TAG", response)
            val gson = Gson()
            val type: Type = object : TypeToken<ImageListResponse>() {}.type
            val res: ImageListResponse = gson.fromJson(response, type)
            val list: MutableList<GridItemModel> = arrayListOf()
            for (msg in res.message) {
                list.add(GridItemModel(msg))
            }
            insertAnimals(list)

        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

//Fetch data from database
    private fun loadFromDataBase() =
        viewModelScope.launch(Dispatchers.IO) {
            val list: MutableList<GridItemModel> = arrayListOf()
            val animals = animalRepository?.getAllAnimals()
            if (!animals.isNullOrEmpty()) {
                for (animal in animals) {
                    if (!animal.image_path.isNullOrEmpty()) {
                        list.add(GridItemModel(animal.image_path))
                    }
                }
            }
            handler.post { adapterItemList.value = list }
        }

    //Method to insert data in the datbase
    private fun insertAnimals(list: MutableList<GridItemModel>) =
        viewModelScope.launch(Dispatchers.IO) {
            animalRepository?.clearAnimal()
            for (gridItemModel in list) {
                animalRepository?.insertAnimal(Animal(null, gridItemModel.imgURL))
            }
            handler.post {
                adapterItemList.value = list
                isVisible.set(false)
            }
        }


    fun getLiveDataAdapterItemList(): MutableLiveData<MutableList<GridItemModel>> {
        return adapterItemList
    }

    //Get list of all images
    fun getAdapterItemList(): MutableList<GridItemModel> {
        return adapterItemList.value!!
    }

    //Get list of all selected images
    fun getSelectedItems(): MutableLiveData<MutableList<GridItemModel>> {
        return selectedItems
    }

    //Continue button click after selecting 2 or more than 2 images
    fun onContinueClick(view: View?) {
        if (!getSelectedItems().value.isNullOrEmpty()) {
            val selectedAdapterItems = getSelectedItems().value!!.stream()
                .filter { t -> t.isCheckboxChecked.get() == true }
                .collect(Collectors.toList())
            openDownloadFragment(view, selectedAdapterItems)
        }
    }

    //Open next fragment to display all the selected images
    private fun openDownloadFragment(view: View?, selectedItems: List<GridItemModel>) {
        val fragmentActivity = view?.context as? FragmentActivity
        val downloadFragment = MyDownloadFragment()
        val bundle = Bundle()
        bundle.putSerializable(AppCons.SELECTED_ITEMS, selectedItems as java.io.Serializable)
        downloadFragment.arguments = bundle
        fragmentActivity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.mainContainer, downloadFragment)?.addToBackStack(AppCons.BACK)?.commit()
    }

}