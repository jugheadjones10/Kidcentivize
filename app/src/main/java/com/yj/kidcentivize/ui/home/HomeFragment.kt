package com.yj.kidcentivize.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.yj.kidcentivize.R
import com.yj.kidcentivize.databinding.FragmentHomeBinding
import com.yj.kidcentivize.ui.onboard.OnboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val onboardViewModel: OnboardViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        onboardViewModel.users.observe(viewLifecycleOwner, Observer { users ->
            Log.d("hey", "HomeFragment" + users.toString())
            if (users.isNotEmpty()) {

                //User is child
                if(users.first().kids == null){
                    navController.navigate(R.id.action_navigation_home_to_childHomeFragment)

                //User is parent
                }else{
                    navController.navigate(R.id.action_navigation_home_to_parentHomeFragment)
                }

            } else {
                navController.navigate(R.id.action_navigation_home_to_queryFragment)
            }
        })
    }


}