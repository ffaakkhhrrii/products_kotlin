package com.fakhri.products.ui.fragment.intent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fakhri.products.databinding.FragmentIntentBinding

class FragmentIntent : Fragment() {

    private var _binding: FragmentIntentBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CODE_PERMISSION_CAMERA = 101
    private val REQUEST_CODE_PERMISSION_GALERY = 102
    private var currentRequestCode : Int = 0
    private lateinit var imageView: ImageView
    private lateinit var btnGalery: Button
    private lateinit var btnCamera: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIntentBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = binding.resultImg
        btnGalery = binding.btnGalery
        btnCamera = binding.btnCamera

        btnGalery.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                currentRequestCode = REQUEST_CODE_PERMISSION_GALERY
                requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                currentRequestCode = REQUEST_CODE_PERMISSION_GALERY
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        btnCamera.setOnClickListener {
            currentRequestCode = REQUEST_CODE_PERMISSION_CAMERA
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            when (currentRequestCode) {
                REQUEST_CODE_PERMISSION_CAMERA -> openCamera()
                REQUEST_CODE_PERMISSION_GALERY -> openGallery()
            }
        } else {
            showPermissionDeniedDialog()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private fun showPermissionDeniedDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Permission Denied")
        builder.setMessage("App requires permission to work properly. Please grant the required permissions.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private var galleryLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                imageView.setImageURI(selectedImageUri)
            }
        }
    }

    private var cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            val bitmap = data?.extras?.get("data") as? Bitmap
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    private fun openCamera() {
        val intent  = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}