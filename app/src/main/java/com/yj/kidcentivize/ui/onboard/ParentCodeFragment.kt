package com.yj.kidcentivize.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.yj.kidcentivize.R
import com.yj.kidcentivize.api.ApiStatus
import com.yj.kidcentivize.databinding.FragmentParentCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentCodeFragment : Fragment() {
    private val onboardViewModel: OnboardViewModel by activityViewModels()
    private lateinit var binding: FragmentParentCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParentCodeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = onboardViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setOnClickListener {
            onNextClicked()
        }

    }

    private fun onNextClicked(){
        val code = binding.kidCodeInputEditText.text.toString().trim()
        if(code.isBlank()){
            binding.kidCodeInputEditText.error = "Please enter your child's code"
        }else{
            onboardViewModel.submitKidCode(code)
            listenForDaoUpdate()
        }
    }

    private fun listenForDaoUpdate(){
        val navController = findNavController()
        onboardViewModel.users.observe(viewLifecycleOwner, Observer { users ->
            if(users.isNotEmpty()){
                onboardViewModel.setStatus(ApiStatus.DONE)
                navController.navigate(R.id.action_parentCodeFragment_to_navigation_home)
            }else{
                onboardViewModel.setStatus(ApiStatus.LOADING)
            }
        })
    }

}