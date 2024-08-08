package com.fakhri.products.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fakhri.products.R
import com.fakhri.products.data.local.db.user.UsersEntity
import com.fakhri.products.databinding.FragmentProfileBinding
import com.fakhri.products.data.utils.handleCollect
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentProfile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = 2
        val action = {action: ProfileAction -> viewModel.processAction(action)}
        action(ProfileAction.FetchUser(id))
        val userFlow = viewModel.state.map { it.user }
        viewLifecycleOwner.lifecycleScope.launch {
            userFlow.handleCollect(
                onSuccess = {
                    setDataUser(it.data!!)
                    binding.layoutUser.isVisible = true
                    binding.pbUser.visibility = View.GONE
                },
                onLoading = {
                    binding.pbUser.visibility = View.VISIBLE
                    binding.layoutUser.isVisible = false
                },
                onError = {
                    binding.pbUser.visibility = View.GONE
                    binding.layoutUser.isVisible = false
                    binding.btnEditProfile.visibility = View.GONE
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.error))
                        .setMessage(resources.getString(R.string.failed_to_fetch_genres))
                        .setNeutralButton(resources.getString(R.string.close)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(resources.getString(R.string.try_again)) { _, _ ->
                            action(ProfileAction.FetchUser(id))
                        }
                        .show()
                }
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collect{
                when(it){
                    is ProfileEffect.NavigateToEditProfile->{
                        val move = FragmentProfileDirections.actionFragmentProfileToFragmentEditProfile(it.userId)
                        findNavController().navigate(move)
                    }
                }
            }
        }

        binding.btnEditProfile.setOnClickListener {
            action(ProfileAction.ButtonEditProfilePress(id))
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