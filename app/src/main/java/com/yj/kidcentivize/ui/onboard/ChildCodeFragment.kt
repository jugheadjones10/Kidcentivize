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
import com.yj.kidcentivize.databinding.FragmentChildCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChildCodeFragment : Fragment() {
    private val onboardViewModel: OnboardViewModel by activityViewModels()
    private lateinit var binding: FragmentChildCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildCodeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = onboardViewModel

        onboardViewModel.getKidCode()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        onboardViewModel.kidCode.observe(viewLifecycleOwner, Observer<String?> { kidCode ->
            kidCode?.let {
                binding.kidCode.setText(it)

                //Bottom should only be called once
                onboardViewModel.listenForParent(it)
            }
        })

        onboardViewModel.parentCreated.observe(viewLifecycleOwner, Observer<Boolean> { parentCreated ->
            if(parentCreated == true){
                navController.navigate(R.id.action_childCodeFragment_to_setupDevicePermissionsFragment)
            }
        })

    }

}