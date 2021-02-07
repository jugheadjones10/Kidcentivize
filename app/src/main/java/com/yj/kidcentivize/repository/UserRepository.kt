package com.yj.kidcentivize.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.yj.kidcentivize.api.PlzmaService
import com.yj.kidcentivize.api.asDatabaseModel
import com.yj.kidcentivize.db.*
import com.yj.kidcentivize.di.PlzmaApplication
import io.realm.mongodb.mongo.events.BaseChangeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bson.Document
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val blockDao: BlockDao,
    private val taskDao: TaskDao,
    private val foodTaskDao: FoodTaskDao,
    private val service: PlzmaService
) {

    init{
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                userDao.deleteAll()
                blockDao.deleteAll()
                taskDao.deleteAll()
                foodTaskDao.deleteAll()
            }
        }

    }

    val parentCreated: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    val users: LiveData<List<User>> = userDao.getAll()
    val kidCode: LiveData<String?> = Transformations.map(userDao.getOne()) {
        it?.code
    }

    suspend fun getKidCode(name: String) {
        withContext(Dispatchers.IO) {
            val kid = service.getKidCode(name)
            userDao.insertUser(kid.asDatabaseModel())
        }
    }

    suspend fun submitKidCode(code: String, name: String) {
        withContext(Dispatchers.IO) {
            val parent = service.submitKidCode(code, name)
            userDao.insertUser(parent.asDatabaseModel())
        }
    }

    fun listenForParent(kidCode: String){
        val watcher = PlzmaApplication.mongoParentsCollection?.watchWithFilterAsync(Document("fullDocument.kidCode", kidCode))
        watcher?.let{
            it[{ result ->
                if (result.isSuccess) {
                    Log.v("hey", "Event type: ${result.get().operationType} full document: ${result.get().fullDocument}")
                    if(result.get().operationType == BaseChangeEvent.OperationType.INSERT){
                        Log.v("hey", "Operation type is INSERT")

                        if(result.get().fullDocument?.get("kidCode") == kidCode){
                            parentCreated.postValue(true)
                        }
                    }
                } else {
                    Log.e("hey", "failed to subscribe to changes in the collection with : ${result.error}")
                }
            }]
        }
    }

}