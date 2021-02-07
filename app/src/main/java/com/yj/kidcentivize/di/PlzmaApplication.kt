package com.yj.kidcentivize.di

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.mongo.MongoCollection
import org.bson.Document
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@HiltAndroidApp
class PlzmaApplication : Application() {

    companion object {
        var mongoKidsCollection: MongoCollection<Document>? = null
        var mongoParentsCollection: MongoCollection<Document>? = null
        var executorService: ExecutorService? = null
    }

    override fun onCreate() {
        super.onCreate()

        executorService = Executors.newFixedThreadPool(6)

        Realm.init(this)
        val appID : String = "plzmom-togub";
        val app = App(
            AppConfiguration.Builder(appID)
            .build())

        val credentials: Credentials = Credentials.anonymous()
        app.loginAsync(credentials) {
            if (it.isSuccess) {
                Log.v("hey", "Successfully authenticated anonymously.")
                val user = app.currentUser()

                instantiateMongoCollectionHandle(user)
            } else {
                Log.e("hey", it.error.toString())
            }
        }
    }

    private fun instantiateMongoCollectionHandle(user: User?){
        val mongoClient = user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
        val mongoDatabase = mongoClient.getDatabase("plzma")
        mongoKidsCollection = mongoDatabase.getCollection("kids")
        mongoParentsCollection = mongoDatabase.getCollection("parents")

        Log.v("EXAMPLE", "Successfully instantiated the MongoDB collection handle")
    }
}
