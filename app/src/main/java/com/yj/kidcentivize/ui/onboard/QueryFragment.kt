package com.yj.kidcentivize.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yj.kidcentivize.R
import com.yj.kidcentivize.databinding.FragmentQueryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QueryFragment : Fragment() {

    private val onboardViewModel: OnboardViewModel by activityViewModels()
    private lateinit var binding: FragmentQueryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQueryBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        binding.parentButton.setOnClickListener {
            onboardViewModel.setIdentity(OnboardViewModel.Identity.PARENT)
            navController.navigate(R.id.action_queryFragment_to_inputDetailsFragment)
        }

        binding.childButton.setOnClickListener {
            onboardViewModel.setIdentity(OnboardViewModel.Identity.CHILD)
            navController.navigate(R.id.action_queryFragment_to_inputDetailsFragment)
        }
    }

}