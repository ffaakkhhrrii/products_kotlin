package com.fakhri.products.ui.fragment.profile.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fakhri.products.R
import com.fakhri.products.data.network.model.user.Address
import com.fakhri.products.data.network.model.user.Bank
import com.fakhri.products.data.network.model.user.Users
import com.fakhri.products.data.utils.Result
import com.fakhri.products.databinding.FragmentEditProfileBinding
import com.fakhri.products.network.ApiConfig
import com.fakhri.products.repository.user.UserRepository
import com.fakhri.products.ui.fragment.detail.DetailFragmentArgs
import com.fakhri.products.ui.fragment.profile.FragmentProfileViewModel
import com.fakhri.products.ui.fragment.profile.FragmentProfileViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FragmentEditProfile : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FragmentEditProfileViewModel
    private val args: FragmentEditProfileArgs by navArgs()
    private lateinit var selectedDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = UserRepository(ApiConfig.instance,requireContext())
        val factory = FragmentEditProfileViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(FragmentEditProfileViewModel::class.java)

        val userId = args.id
        viewModel.showData(userId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect{
                    result->
                when(result){
                    is Result.Loading ->{
                        binding.pbUser.visibility = View.VISIBLE
                        binding.layoutDetailUser.isVisible = false
                    }
                    is Result.Success->{
                        setDataUser(result.data)
                        binding.layoutDetailUser.isVisible = true
                        binding.pbUser.visibility = View.GONE
                    }
                    is Result.Failure->{
                        Toast.makeText(requireContext(),result.exception.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnUpdate.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.addUser(addData(userId))
                findNavController().navigateUp()
            }
        }

        binding.editTextBirthdate.setOnClickListener {
            showDatePicker()
        }

        binding.btnReset.setOnClickListener{
            showDialogReset(userId)
        }
    }

    private fun showDialogReset(userId: Int){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete")
            .setMessage("Anda yakin ingin mereset profile?")
            .setPositiveButton("Yes"){dialog,_->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.resetUser(userId)
                    findNavController().navigateUp()
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDatePicker(){
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Birth Date")
            .build()
        datePicker.addOnPositiveButtonClickListener {selection->
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = sdf.format(Date(selection))
            binding.editTextBirthdate.setText(selectedDate)
        }
        datePicker.show(parentFragmentManager,"BirthDate")
    }

    private fun addData(id: Int): Users{
        return Users(
            id = id,
            address = Address(address = binding.editTextAddress.text.toString()),
            bank = Bank(cardNumber = binding.editTextCreditNumber.text.toString()),
            birthDate = binding.editTextBirthdate.text.toString(),
            bloodGroup = binding.editTextBloodType.text.toString(),
            email = binding.editTextEmail.text.toString(),
            firstName = binding.edtFirstName.text.toString(),
            image = binding.editTextImgUrl.text.toString(),
            lastName = binding.edtLastName.text.toString(),
            phone = binding.editTextPhoneNumber.text.toString(),
            role = binding.editTextRole.text.toString(),
            university = binding.editTextEducation.text.toString(),
            username = binding.editTextUsername.text.toString()
        )
    }

    private fun setDataUser(users: Users){
        binding.editTextImgUrl.setText(users.image)
        binding.edtFirstName.setText(users.firstName)
        binding.edtLastName.setText(users.lastName)
        binding.editTextEmail.setText(users.email)
        binding.editTextPhoneNumber.setText(users.phone)
        binding.editTextBirthdate.setText(users.birthDate)
        binding.editTextRole.setText(users.role)
        binding.editTextUsername.setText(users.username)
        binding.editTextAddress.setText(users.address.address)
        binding.editTextEducation.setText(users.university)
        binding.editTextBloodType.setText(users.bloodGroup)
        binding.editTextCreditNumber.setText(users.bank.cardNumber)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}