package com.sushmita.downloadimage.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sushmita.downloadimage.databinding.GridFragmentBinding
import com.sushmita.downloadimage.view.adapters.ImageGridAdapter
import com.sushmita.downloadimage.viewmodel.GridFragmentVM

class GridFragment : BaseFragment() {

    private lateinit var binding: GridFragmentBinding
    private var imageGridAdapter: ImageGridAdapter? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private val gridFragmentVM: GridFragmentVM by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GridFragmentBinding.inflate(inflater, container, false)
        binding.gridFragmentVM = gridFragmentVM

        applyUseCase()
        setImageListAdapter()
        handleImageListResponse()

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleImageListResponse() {
        gridFragmentVM.getLiveDataAdapterItemList()
            .observe(viewLifecycleOwner) {
                setImageListAdapter()
            }
    }

    //Show images on the screen using recycler view
    private fun setImageListAdapter() {
        imageGridAdapter = ImageGridAdapter(
            requireContext(), gridFragmentVM.getAdapterItemList(), gridFragmentVM.getSelectedItems()
        )
        gridLayoutManager = GridLayoutManager(requireContext(), 3)
        binding.imagesRv.layoutManager = gridLayoutManager
        binding.imagesRv.adapter = imageGridAdapter

    }

    //Method to make the continue button visible
    //Display Continue button when 2 or more than 2 items are selected
    private fun applyUseCase(){
        gridFragmentVM.getSelectedItems().observe(viewLifecycleOwner) {
            gridFragmentVM.visibleContinue.set(it.size >= 2)
        }
    }
}
