package com.fakhri.products.ui.fragment.profile.editprofile

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.fakhri.products.R
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.databinding.FragmentEditProfileBinding
import com.fakhri.products.data.utils.handleCollect
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class FragmentEditProfile : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FragmentEditProfileViewModel by viewModels()
    private val args: FragmentEditProfileArgs by navArgs()
    private lateinit var selectedDate: String
    private var currentSelectedImage: Uri? = null
    private var currentRequestCode: Int = 0
    private val REQUEST_CODE_CAMERA = 101
    private var REQUEST_CODE_GALERY = 102
    private val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(
        Date()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = args.id
        val action = {action: EditProfileUIAction-> viewModel.processAction(action)}
        action(EditProfileUIAction.FetchUser(userId))
        val userFlow = viewModel.state.map { it.user }
        viewLifecycleOwner.lifecycleScope.launch {
            userFlow.handleCollect(
                onSuccess = {
                    Log.i("FragmentEditProfile","Load Data Success")
                    setDataUser(it.data!!)
                    binding.layoutDetailUser.isVisible = true
                    binding.pbUser.visibility = View.GONE
                },
                onLoading = {
                    Log.i("FragmentEditProfile","Load Data...")
                    binding.pbUser.visibility = View.VISIBLE
                    binding.layoutDetailUser.isVisible = false
                },
                onError = {
                    Log.e("FragmentEditProfile","Error")
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.error))
                        .setMessage(resources.getString(R.string.failed_to_fetch_genres))
                        .setNeutralButton(resources.getString(R.string.close)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(resources.getString(R.string.try_again)) { _, _ ->
                            action(EditProfileUIAction.FetchUser(userId))
                        }
                        .show()
                }
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collectLatest {
                when(it){
                    is EditProfileUIEffect.BackToProfile->{
                        findNavController().navigateUp()
                    }
                    is EditProfileUIEffect.ShowGallery->{
                        currentRequestCode = REQUEST_CODE_GALERY
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                    is EditProfileUIEffect.ShowDatePicker->{
                        showDatePicker()
                    }
                    is EditProfileUIEffect.ShowCamera->{
                        currentRequestCode = REQUEST_CODE_CAMERA
                        requestPermission.launch(Manifest.permission.CAMERA)
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            action(EditProfileUIAction.BackButtonPress)
        }

        binding.btnUpdate.setOnClickListener {
            action(EditProfileUIAction.ChangeUser(addData(userId)))
        }

        binding.editTextBirthdate.setOnClickListener {
            action(EditProfileUIAction.ShowDatePickerButtonPressed)
        }

        binding.btnReset.setOnClickListener {
            showDialogReset(userId){
                action(EditProfileUIAction.ResetUser(it))
            }
        }

        binding.btnPickGalery.setOnClickListener {
            action(EditProfileUIAction.ShowGalleryButtonPressed)
        }

        binding.btnPickPhoto.setOnClickListener {
            action(EditProfileUIAction.ShowCameraButtonPressed)
        }
    }

    private fun showDialogReset(userId: Int,onAccept:(Int)-> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete")
            .setMessage("Anda yakin ingin mereset profile?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                onAccept(userId)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Birth Date")
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = sdf.format(Date(selection))
            binding.editTextBirthdate.setText(selectedDate)
        }
        datePicker.show(parentFragmentManager, "BirthDate")
    }

    private fun addData(id: Int): UsersEntity {
        return UsersEntity(
            id = id,
            address = binding.editTextAddress.text.toString(),
            cardNumber = binding.editTextCreditNumber.text.toString(),
            birthDate = binding.editTextBirthdate.text.toString(),
            bloodGroup = binding.editTextBloodType.text.toString(),
            email = binding.editTextEmail.text.toString(),
            firstName = binding.edtFirstName.text.toString(),
            image = currentSelectedImage.toString(),
            lastName = binding.edtLastName.text.toString(),
            phone = binding.editTextPhoneNumber.text.toString(),
            role = binding.editTextRole.text.toString(),
            university = binding.editTextEducation.text.toString(),
            username = binding.editTextUsername.text.toString()
        )
    }

    private fun setDataUser(users: UsersEntity) {
        binding.edtFirstName.setText(users.firstName)
        binding.edtLastName.setText(users.lastName)
        binding.editTextEmail.setText(users.email)
        binding.editTextPhoneNumber.setText(users.phone)
        binding.editTextBirthdate.setText(users.birthDate)
        binding.editTextRole.setText(users.role)
        binding.editTextUsername.setText(users.username)
        binding.editTextAddress.setText(users.address)
        binding.editTextEducation.setText(users.university)
        binding.editTextBloodType.setText(users.bloodGroup)
        binding.editTextCreditNumber.setText(users.cardNumber)
        Glide.with(requireActivity())
            .load(users.image)
            .placeholder(R.drawable.ic_launcher_background)
            .transform(CenterCrop())
            .into(binding.imgProfile)
        currentSelectedImage = users.image.toUri()
    }

    private var galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                currentSelectedImage = data?.data
                if (currentSelectedImage != null) {
                    binding.imgProfile.setImageURI(currentSelectedImage)
                }

            }
        }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private var requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                when (currentRequestCode) {
                    REQUEST_CODE_CAMERA -> openCamera()
                    REQUEST_CODE_GALERY -> openGallery()
                }
            } else {
                showDialogDenied()
            }
        }

    private fun openCamera() {
        currentSelectedImage = savePhoto(requireContext())
        cameraLauncher.launch(currentSelectedImage)
    }

    private var cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            binding.imgProfile.setImageURI(currentSelectedImage)
        }
    }

    private fun savePhoto(context: Context): Uri {
        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "Pictures/${resources.getString(R.string.app_name)}"
                )
            }
            uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        } else {
            getImageUriForPreQ(requireContext())
        }
        return uri!!
    }

    private fun getImageUriForPreQ(context: Context): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "/Products/$timeStamp.jpg")
        if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
        return FileProvider.getUriForFile(
            context,
            "${requireContext().packageName}.fileprovider",
            imageFile
        )
        //content://com.dicoding.picodiploma.mycamera.fileprovider/my_images/MyCamera/20230825_133659.jpg
    }

    private fun showDialogDenied() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Info")
            .setMessage("App requires permission to work properly. Please grant the required permissions.")
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}