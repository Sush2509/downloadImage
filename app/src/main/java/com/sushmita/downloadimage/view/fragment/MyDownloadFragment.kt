package com.sushmita.downloadimage.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sushmita.downloadimage.databinding.DownloadFragmentBinding
import com.sushmita.downloadimage.view.adapters.ImageListAdapter
import com.sushmita.downloadimage.model.helperclasses.DownloadAdapterItemModel
import com.sushmita.downloadimage.viewmodel.DownloadFragmentVM
import com.sushmita.downloadimage.model.helperclasses.GridItemModel
import com.sushmita.downloadimage.utils.AppCons


class MyDownloadFragment : Fragment() {

    private lateinit var binding: DownloadFragmentBinding
    private var viewModel: DownloadFragmentVM? = null
    private var imageListAdapter: ImageListAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DownloadFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[DownloadFragmentVM::class.java]
        binding.downloadFragmentVM = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedItems = arguments?.getSerializable(AppCons.SELECTED_ITEMS) as List<*>
        setImageListAdapter(selectedItems as List<GridItemModel>)
    }

    //Show selected images on the screen using recycler view
    private fun setImageListAdapter(selectedItems:List<GridItemModel>) {
        viewModel?.getAdapterItemList()?.clear()
        for(gridItemViewModel in selectedItems){
            viewModel?.getAdapterItemList()?.add(DownloadAdapterItemModel(gridItemViewModel.imgURL!!))
        }

        imageListAdapter = viewModel?.let {
            viewModel?.getAdapterItemList()?.let { it1 ->
                ImageListAdapter(
                    requireContext(), it1
                )
            }
        }
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager?.orientation = LinearLayoutManager.VERTICAL
        binding.imagesRv.layoutManager = linearLayoutManager
        binding.imagesRv.adapter = imageListAdapter
    }

}