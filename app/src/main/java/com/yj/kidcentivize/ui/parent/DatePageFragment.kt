package com.yj.kidcentivize.ui.parent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.yj.kidcentivize.R
import com.yj.kidcentivize.databinding.FragmentDatePageBinding
import com.yj.kidcentivize.databinding.ItemBlockBinding
import com.yj.kidcentivize.databinding.ItemTaskBinding
import com.yj.kidcentivize.db.BlockWithFoodTasks
import com.yj.kidcentivize.db.FoodTask
import java.io.Serializable
import java.time.LocalDateTime

private const val ARG_DATE = "DATE"

class DatePageFragment : Fragment() {

    private var date: LocalDateTime? = null
    private var blocks: List<BlockWithFoodTasks>? = null
    private var blocksAdapter: BlocksAdapter? = null

    private val parentViewModel: ParentViewModel by activityViewModels()
    private lateinit var binding: FragmentDatePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getSerializable(ARG_DATE) as LocalDateTime
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDatePageBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()

        blocksAdapter = BlocksAdapter(FoodTaskClick {
            //Insert here what happens when a single Food Task is clicked
        })
        binding.blocksList.adapter = blocksAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        date?.let{
            val dayOfMonth = it.toLocalDate().dayOfMonth
            val month = it.toLocalDate().month.toString().toLowerCase().capitalize()
            binding.dateText.text = "$month $dayOfMonth"
        }

        //Change to blocks with food tasks instead of just blocks
        parentViewModel.datePages.observe(viewLifecycleOwner, Observer { datePages ->
            blocks = datePages[date]
            blocks?.let{
                blocksAdapter?.blocks = it
            }
            Log.d("hey", "Blocks from within DatePageFra : $blocks")
        })

    }

    companion object {
        @JvmStatic
        fun newInstance(date: LocalDateTime) =
            DatePageFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_DATE, date as Serializable)
                }
            }
    }
}

class FoodTaskClick(val block: (FoodTask) -> Unit) {
    fun onClick(foodTask: FoodTask) = block(foodTask)
}

class BlocksAdapter(val callback: FoodTaskClick) : RecyclerView.Adapter<BlockViewHolder>() {

    var blocks: List<BlockWithFoodTasks> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        val withDataBinding: ItemBlockBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            BlockViewHolder.LAYOUT,
            parent,
            false)
        return BlockViewHolder(withDataBinding)
    }

    override fun getItemCount() = blocks.size

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.block = blocks[position].block

            val foodTasks = blocks[position].foodTasks
            val foodTasksAdapter = FoodTasksAdapter(callback)
            foodTasksAdapter.foodTasks = foodTasks

            it.tasksList.adapter = foodTasksAdapter
        }
    }

}

class BlockViewHolder(val viewDataBinding: ItemBlockBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_block
    }
}

class FoodTasksAdapter(val callback: FoodTaskClick) : RecyclerView.Adapter<FoodTaskViewHolder>() {

    var foodTasks: List<FoodTask> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTaskViewHolder {
        val withDataBinding: ItemTaskBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            FoodTaskViewHolder.LAYOUT,
            parent,
            false)
        return FoodTaskViewHolder(withDataBinding)
    }

    override fun getItemCount() = foodTasks.size

    override fun onBindViewHolder(holder: FoodTaskViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.foodTask = foodTasks[position]
            it.foodTaskCallback = callback
        }
    }

}

class FoodTaskViewHolder(val viewDataBinding: ItemTaskBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_task
    }
}