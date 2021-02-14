package com.yj.kidcentivize.ui.parent

import android.util.Log
import androidx.lifecycle.*
import com.yj.kidcentivize.db.BlockWithFoodTasks
import com.yj.kidcentivize.db.Time
import com.yj.kidcentivize.repository.BlockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class ParentViewModel @Inject constructor(private val blockRepository: BlockRepository) : ViewModel(){

    val time = blockRepository.time
    val blocksWithFoodTasks = blockRepository.blocksWithFoodTasks

    //I don't understand the below...
    private val mediatorLiveData = CombinedMediator(blocksWithFoodTasks, time)

//            by lazy{
//
//        val mediatorLiveData : MediatorLiveData<Pair<List<BlockWithFoodTasks>?, Time?>> = MediatorLiveData()
//
//        mediatorLiveData.addSource(time){ value ->
//            if(mediatorLiveData.value?.first != null && value != null){
//                mediatorLiveData.value = mediatorLiveData.value?.first to value
//            }
//        }
//
//        mediatorLiveData.addSource(blocksWithFoodTasks){ value ->
//            if(mediatorLiveData.value?.second != null && value != null){
//                mediatorLiveData.value = value to mediatorLiveData.value?.second
//            }
//        }
//
//        mediatorLiveData
//    }

    //blockRepository.blocksWithFoodTasks.combine(time)
    val datePages: LiveData<LinkedHashMap<LocalDateTime, List<BlockWithFoodTasks>>> = mediatorLiveData.map { value ->

        val blocks = value.first
        val time = value.second.time
        Log.d("hey", time.toString())

        //Linked map is used because order of insertion is maintained

        if(!blocks.isNullOrEmpty()){

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
            var earliestDate = sortedBlocks.first().block.date
            var latestDate = sortedBlocks.last().block.date

            if(time.isBefore(earliestDate)){
                earliestDate = time
            }else if(time.isAfter(latestDate)){
                latestDate = time
            }
            val daysDiff = ChronoUnit.DAYS.between(earliestDate, latestDate.plusDays(1))


            val dateToBlocksMap = LinkedHashMap<LocalDateTime, List<BlockWithFoodTasks>>()
            for (i in 0..daysDiff) {
                val currentDate = earliestDate.plusDays(i)
                val blocksOnDate = grouped[currentDate.toLocalDate()]

                dateToBlocksMap[currentDate] = blocksOnDate ?: emptyList()
            }

            dateToBlocksMap
        }else{
            linkedMapOf<LocalDateTime, List<BlockWithFoodTasks>>(time to listOf())
        }

    }

    val datePagesDates: LiveData<List<LocalDateTime>> = datePages.map { datePages ->
        datePages.keys.toList()
    }

    fun refreshBlocksForParent(){
        viewModelScope.launch {
            blockRepository.refreshBlocksForParent()
        }
    }

    fun refreshBlocksForChild(){
        viewModelScope.launch {
            blockRepository.refreshBlocksForChild()
        }
    }

    fun listenToBlocks(){
//        viewModelScope.launch {
            blockRepository.listenToBlocks()
//        }
    }

}

class PairLiveData<A, B>(first: LiveData<A>, second: LiveData<B>) : MediatorLiveData<Pair<A?, B?>>() {
    init {
        addSource(first) { value = it to second.value }
        addSource(second) { value = first.value to it }
    }
}

fun <A, B> LiveData<A>.combine(other: LiveData<B>): PairLiveData<A, B> {
    return PairLiveData(this, other)
}


class CombinedMediator(
        liveBlocksList: LiveData<List<BlockWithFoodTasks>>,
        liveTime: LiveData<Time>
) : MediatorLiveData<Pair<List<BlockWithFoodTasks>, Time>>() {

    private var blocksList: List<BlockWithFoodTasks> = emptyList()
    private var time: Time = Time("Now", LocalDateTime.now())

    init {
        value = Pair(blocksList, time)

        addSource(liveBlocksList) {
            if( it != null ) blocksList = it
            value = Pair(blocksList, time)
        }

        addSource(liveTime) {
            if( it != null ) time = it
            value = Pair(blocksList, time)
        }
    }
}