package com.sushmita.downloadimage.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sushmita.downloadimage.R
import com.sushmita.downloadimage.databinding.ActivityMainBinding
import com.sushmita.downloadimage.utils.AppCons
import com.sushmita.downloadimage.utils.AppUtils
import com.sushmita.downloadimage.view.fragment.GridFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Add fragment
        supportFragmentManager.beginTransaction().add(R.id.mainContainer, GridFragment()).commit()

        // Add the onBackPressedCallback to the activity's onBackPressedDispatcher
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        if(!AppUtils.checkAppPermission(this)){
            AppUtils.showPermissionIssuePopup(this)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // If you want to allow the default back button behavior, call this:
             isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the onBackPressedCallback when the view is destroyed
        onBackPressedCallback.remove()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppCons.DOWNLOAD_PERMISSION_REQUEST_CODE) {
            if (AppUtils.checkAppPermission(this)) {
                // Notification permission granted
            } else {
                // Notification permission denied
                AppUtils.showPermissionIssuePopup(this)
            }
        }
    }


}
