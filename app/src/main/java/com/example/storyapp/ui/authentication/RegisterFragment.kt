package com.example.storyapp.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.storyapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    private val binding: FragmentRegisterBinding by lazy {
        FragmentRegisterBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )

        initComponent()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.loadingRegisterState.collect {
                if (it){
                    binding.loadingState.visibility = View.VISIBLE
                    binding.linearLayout5.visibility = View.GONE
                } else {
                    binding.loadingState.visibility = View.GONE
                    binding.linearLayout5.visibility = View.VISIBLE
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resultRegisterUser.collect {
                if (it == null) {
                    Toast.makeText(requireContext(), "Akun Sudah terdaaftar", Toast.LENGTH_SHORT).show()
                } else {
                    if (it.error == false){
                        Toast.makeText(requireContext(), "Akun Berhasil terdaftar", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.errorRegisterResult.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initComponent() {
        with(binding){
            btnRegister.setOnClickListener {
                val name = edtName.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                viewModel.register(name, email, password)
            }
        }
    }


}