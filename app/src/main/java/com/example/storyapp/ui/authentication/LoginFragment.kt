package com.example.storyapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.ui.feed.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    private val binding: FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponent()
        observeViewModel()



    }

    private fun observeViewModel() {
       lifecycleScope.launchWhenStarted {
           viewModel.resultUser.collectLatest {
               if (it != null){
                   Toast.makeText(requireContext(), "Wellcome ${it.name}", Toast.LENGTH_SHORT).show()
                   startActivity(Intent(requireContext(), MainActivity::class.java))
                   activity?.finish()
               }
           }
       }
        lifecycleScope.launchWhenStarted {
            viewModel.errorResult.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.loadingState.collect {
                with(binding){
                    if (it){
                      loadingState.visibility = View.VISIBLE
                      linearLayout4.visibility = View.GONE
                    } else {
                        loadingState.visibility = View.GONE
                        linearLayout4.visibility = View.VISIBLE
                    }
                }

            }
        }
    }


    private fun initComponent() {
        changeButton()
        with(binding){
            toRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            btnLogin.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                viewModel.loginUser(email, password)
            }

        }
    }

    private fun changeButton() {
        with(binding){
            edtEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0?.isNotEmpty() == true){
                        btnLogin.isEnabled = true
                        btnLogin.isClickable = true
                        btnLogin.text = getString(R.string.login)
                    } else {
                        btnLogin.isEnabled = false
                        btnLogin.isClickable = false
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    //
                }

            })

        }
    }

}