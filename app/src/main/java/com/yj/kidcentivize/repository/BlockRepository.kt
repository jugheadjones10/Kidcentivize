package com.yj.kidcentivize.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.yj.kidcentivize.api.PlzmaService
import com.yj.kidcentivize.dateToLocalDateTime
import com.yj.kidcentivize.db.*
import com.yj.kidcentivize.di.PlzmaApplication
import org.bson.Document
import org.bson.types.ObjectId
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BlockRepository @Inject constructor(
    private val userDao: UserDao,
    private val blockDao: BlockDao,
    private val taskDao: TaskDao,
    private val foodTaskDao: FoodTaskDao,
    private val timeDao: TimeDao,
    private val service: PlzmaService
) {

    val time: LiveData<Time> = timeDao.getTime()
    val blocks: LiveData<List<Block>> = blockDao.getAll()
    val blocksWithFoodTasks: LiveData<List<BlockWithFoodTasks>> = blockDao.getBlocksWithFoodTasks()

    suspend fun refreshBlocksForParent(){
        val parent = userDao.getOneAsync()
        val queryFilter = Document("_id", ObjectId(parent.kids))

        val mongoCollection = PlzmaApplication.mongoKidsCollection
        mongoCollection?.let {
            it.findOne(queryFilter)
                .getAsync { task ->
                    if (task.isSuccess) {
                        val result: Document = task.get()
                        Log.v("hey", "successfully found a document: $result")
                        Log.v("hey", "Tasks :  "  + result.get("tasks"))
                        Log.v("hey", "Blocks :  "  + result.get("blocks"))


                        val executor: Executor = PlzmaApplication.executorService as Executor
                        executor.execute {
                            val tasks = result.get("tasks") as List<Document>
                            for (task in tasks) {
                                taskDao.insertTask(
                                    Task(
                                        id = task.get("_id").toString(),
                                        title = task.get("title") as String,
                                        fileUrl = task.get("fileUrl") as String,
                                        instructions = task.get("instructions") as String,
                                        theKidShould = task.get("theKidShould") as String
                                    )
                                )
                            }

                            val blocks = result.get("blocks") as List<Document>
                            for (block in blocks) {

                                blockDao.insertBlock(
                                    Block(
                                        id = block.get("_id").toString(),
                                        expiryTime = block.get("expiryTime") as Int,
                                        date = dateToLocalDateTime(block.get("date") as Date),
                                        reward = block.get("reward") as Int
                                    )
                                )

                                val foodTasks = block.get("foodTasks") as List<Document>
                                for (foodTask in foodTasks) {

                                    val task = foodTask.get("task") as Document
                                    val embeddedTask = EmbeddedTask(
                                        taskId = task.get("_id").toString(),
                                        title = task.get("title") as String,
                                        fileUrl = task.get("fileUrl") as String,
                                        instructions = task.get("instructions") as String,
                                        theKidShould = task.get("theKidShould") as String
                                    )

                                    foodTaskDao.insertFoodTask(
                                        FoodTask(
                                            id = foodTask.get("_id").toString(),
                                            blockId = block.get("_id").toString(),
                                            done = foodTask.get("done") as Boolean,
                                            submissionUrl = foodTask.get("submissionUrl") as String,
                                            task = embeddedTask
                                        )
                                    )
                                }
                            }
                        }

                    } else {
                        Log.e("hey", "failed to find document with: ${task.error}")
                    }
                }
        }

    }

    suspend fun refreshBlocksForChild(){
        val child = userDao.getOneAsync()
        val queryFilter = Document("kidCode", child.code)

        val mongoCollection = PlzmaApplication.mongoKidsCollection
        mongoCollection?.let {
            it.findOne(queryFilter)
                    .getAsync { task ->
                        if (task.isSuccess) {
                            val result: Document = task.get()
                            Log.v("hey", "successfully found a document: $result")
                            Log.v("hey", "Tasks :  "  + result.get("tasks"))
                            Log.v("hey", "Blocks :  "  + result.get("blocks"))


                            val executor: Executor = PlzmaApplication.executorService as Executor
                            executor.execute {
                                val tasks = result.get("tasks") as List<Document>
                                for (task in tasks) {
                                    taskDao.insertTask(
                                            Task(
                                                    id = task.get("_id").toString(),
                                                    title = task.get("title") as String,
                                                    fileUrl = task.get("fileUrl") as String,
                                                    instructions = task.get("instructions") as String,
                                                    theKidShould = task.get("theKidShould") as String
                                            )
                                    )
                                }

                                val blocks = result.get("blocks") as List<Document>
                                for (block in blocks) {

                                    blockDao.insertBlock(
                                            Block(
                                                    id = block.get("_id").toString(),
                                                    expiryTime = block.get("expiryTime") as Int,
                                                    date = dateToLocalDateTime(block.get("date") as Date),
                                                    reward = block.get("reward") as Int
                                            )
                                    )

                                    val foodTasks = block.get("foodTasks") as List<Document>
                                    for (foodTask in foodTasks) {

                                        val task = foodTask.get("task") as Document
                                        val embeddedTask = EmbeddedTask(
                                                taskId = task.get("_id").toString(),
                                                title = task.get("title") as String,
                                                fileUrl = task.get("fileUrl") as String,
                                                instructions = task.get("instructions") as String,
                                                theKidShould = task.get("theKidShould") as String
                                        )

                                        foodTaskDao.insertFoodTask(
                                                FoodTask(
                                                        id = foodTask.get("_id").toString(),
                                                        blockId = block.get("_id").toString(),
                                                        done = foodTask.get("done") as Boolean,
                                                        submissionUrl = foodTask.get("submissionUrl") as String,
                                                        task = embeddedTask
                                                )
                                        )
                                    }
                                }
                            }

                        } else {
                            Log.e("hey", "failed to find document with: ${task.error}")
                        }
                    }
        }

    }

    fun listenToBlocks(){
        val watcher = PlzmaApplication.mongoKidsCollection?.watchAsync()
        Log.d("hey", watcher.toString())
        watcher?.let{
            it[{ result ->
                if (result.isSuccess) {
                    Log.v("hey", "Event type: ${result.get().operationType} full document: ${result.get().fullDocument}")
                } else {
                    Log.e("hey", "failed to subscribe to changes in the collection with : ${result.error}")
                }
            }]
        }
    }
}