package com.fakhri.products.ui.fragment.profile

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fakhri.products.R
import com.fakhri.products.data.ProductRepository
import com.fakhri.products.data.model.user.GetUserResponse
import com.fakhri.products.data.utils.Result
import com.fakhri.products.databinding.FragmentProfileBinding
import com.fakhri.products.network.ApiConfig
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class FragmentProfile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FragmentProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = ProductRepository(ApiConfig.instance)
        val factory = FragmentProfileViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(FragmentProfileViewModel::class.java)

        viewModel.getUser(2)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dataUser.collect{
                result->
                when(result){
                    is Result.Loading ->{
                        binding.pbUser.visibility = View.VISIBLE
                        binding.layoutUser.isVisible = false
                    }
                    is Result.Success->{
                        setDataUser(result.data)
                        binding.layoutUser.isVisible = true
                        binding.pbUser.visibility = View.GONE
                    }
                    is Result.Failure->{
                        Toast.makeText(requireContext(),result.exception.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setDataUser(data: GetUserResponse){
        Glide.with(requireActivity())
            .load(data.image)
            .transform(RoundedCorners(20))
            .into(binding.imgUser)
        binding.tvName.text = "${data.firstName} ${data.lastName}"
        binding.tvEmail.text = data.email
        binding.tvPhone.text = data.phone
        binding.tvBirthday.text = data.birthDate
        binding.tvRole.text = data.role
        binding.tvUsername.text = data.username
        binding.tvAddress.text = data.address.address
        binding.tvUniversity.text = data.university
        binding.tvBloodType.text = data.bloodGroup
        binding.tvCreditCard.text = data.bank.cardNumber
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}