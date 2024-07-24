package com.fakhri.products.ui.fragment.profile

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fakhri.products.R
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.data.network.response.user.Users
import com.fakhri.products.data.utils.Result
import com.fakhri.products.databinding.FragmentProfileBinding
import com.fakhri.products.data.network.api.ApiConfig
import com.fakhri.products.data.repository.UserRepository
import com.fakhri.products.domain.usecase.GetUserUseCase
import kotlinx.coroutines.launch

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

        val repository = UserRepository(ApiConfig.instance,requireContext())
        val getUserUseCase = GetUserUseCase(repository)
        val factory = FragmentProfileViewModelFactory(getUserUseCase)
        viewModel = ViewModelProvider(this,factory).get(FragmentProfileViewModel::class.java)

        val id = 2
        viewModel.getUser(id)
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

        binding.btnEditProfile.setOnClickListener {
            val action = FragmentProfileDirections.actionFragmentProfileToFragmentEditProfile(id)
            findNavController().navigate(action)
        }
    }

    private fun setDataUser(data: UsersEntity){
        Glide.with(requireActivity())
            .load(data.image)
            .transform(RoundedCorners(20))
            .placeholder(R.drawable.ic_launcher_background)
            .transform(CenterCrop())
            .into(binding.imgUser)
        binding.tvName.text = "${data.firstName} ${data.lastName}"
        binding.tvEmail.text = data.email
        binding.tvPhone.text = data.phone
        binding.tvBirthday.text = data.birthDate
        binding.tvRole.text = data.role
        binding.tvUsername.text = data.username
        binding.tvAddress.text = data.address
        binding.tvUniversity.text = data.university
        binding.tvBloodType.text = data.bloodGroup
        binding.tvCreditCard.text = data.cardNumber
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}