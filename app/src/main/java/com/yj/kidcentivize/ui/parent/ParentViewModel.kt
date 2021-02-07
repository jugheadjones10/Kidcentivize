package com.yj.kidcentivize.ui.parent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.yj.kidcentivize.db.Block
import com.yj.kidcentivize.db.BlockWithFoodTasks
import com.yj.kidcentivize.repository.BlockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.collections.LinkedHashMap
import kotlin.collections.List
import kotlin.collections.emptyList
import kotlin.collections.first
import kotlin.collections.groupBy
import kotlin.collections.isNotEmpty
import kotlin.collections.last
import kotlin.collections.linkedMapOf
import kotlin.collections.set
import kotlin.collections.sortedWith
import kotlin.collections.toList

@HiltViewModel
class ParentViewModel @Inject constructor(private val blockRepository: BlockRepository) : ViewModel(){

    data class DatePage(
        val date: LocalDateTime,
        val blocks: List<Block>
    )

    val datePages: LiveData<LinkedHashMap<LocalDateTime, List<BlockWithFoodTasks>>> = blockRepository.blocksWithFoodTasks.map { blocks ->

        if(blocks.isNotEmpty()) {

            val sortedBlocks = blocks.sortedWith(Comparator<BlockWithFoodTasks> { a, b ->
                when {
                    a.block.date.isEqual(b.block.date) -> 0
                    a.block.date.isBefore(b.block.date) -> -1
                    a.block.date.isAfter(b.block.date) -> 1
                    else -> 1
                }
            })

            val grouped = sortedBlocks.groupBy { it.block.date.toLocalDate() }
            //One day is added because ChronoUnit between is [x, y)
            val daysDiff = ChronoUnit.DAYS.between(sortedBlocks.first().block.date, sortedBlocks.last().block.date.plusDays(1))
            val earliestDate = sortedBlocks.first().block.date

//            val datePages = mutableListOf<DatePage>()
            val dateToBlocksMap = LinkedHashMap<LocalDateTime, List<BlockWithFoodTasks>>()
            for (i in 0..daysDiff) {
                val currentDate = earliestDate.plusDays(i)
                val blocksOnDate = grouped[currentDate.toLocalDate()]

                dateToBlocksMap[currentDate] = blocksOnDate ?: emptyList()
//                datePages.add(
//                    DatePage(
//                        currentDate,
//                        blocksOnDate ?: emptyList()
//                    )
//                )

            }

            dateToBlocksMap
        }else{
            linkedMapOf<LocalDateTime, List<BlockWithFoodTasks>>()
//            emptyMap<LocalDateTime, List<Block>>() as LinkedHashMap<LocalDateTime, List<Block>>
        }
    }

    val datePagesDates: LiveData<List<LocalDateTime>> = datePages.map { datePages ->
        datePages.keys.toList()
    }

    fun refreshBlocks(){
        viewModelScope.launch {
            blockRepository.refreshBlocks()
        }
    }

    fun listenToBlocks(){
//        viewModelScope.launch {
            blockRepository.listenToBlocks()
//        }
    }

}