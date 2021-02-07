package com.yj.kidcentivize.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yj.kidcentivize.R
import com.yj.kidcentivize.databinding.FragmentInputDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputDetailsFragment : Fragment() {

    private val onboardViewModel: OnboardViewModel by activityViewModels()
    private lateinit var binding: FragmentInputDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setOnClickListener {
            onNextClicked()
        }

    }

    fun onNextClicked(){
        val navController = findNavController()
        val name = binding.nameInputEditText.text.toString().trim()

        if(name.isBlank()){
            binding.nameInputEditText.error = "Please enter a name"
        }else{
            onboardViewModel.setName(name)
            if(onboardViewModel.identity.value == OnboardViewModel.Identity.PARENT){
                navController.navigate(R.id.action_inputDetailsFragment_to_parentCodeFragment)
            }else{
                navController.navigate(R.id.action_inputDetailsFragment_to_childCodeFragment)
            }
        }
    }
}