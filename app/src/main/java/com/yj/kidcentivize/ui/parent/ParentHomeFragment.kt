package com.yj.kidcentivize.ui.parent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yj.kidcentivize.databinding.FragmentParentHomeBinding
import java.time.LocalDateTime

class ParentHomeFragment : Fragment() {
    private val parentViewModel: ParentViewModel by activityViewModels()
    private lateinit var binding: FragmentParentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParentHomeBinding.inflate(inflater, container, false)

//        parentViewModel.listenToBlocks()
        parentViewModel.refreshBlocks()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.datePagesDates.observe(viewLifecycleOwner, Observer { datePagesDates ->

            Log.d("hey", "Date pages in Parent Home Fragment" + datePagesDates)

            val pagerAdapter = DatePagesPagerAdapter(this, datePagesDates)
            binding.datePages.adapter = pagerAdapter
        })

    }

    private inner class DatePagesPagerAdapter(fa: Fragment, val datePagesDates: List<LocalDateTime> = mutableListOf()) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = datePagesDates.size

        override fun createFragment(position: Int): Fragment = DatePageFragment.newInstance(datePagesDates[position])
    }

}