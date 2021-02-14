package com.yj.kidcentivize.di

import android.content.Context
import androidx.room.Room
import com.yj.kidcentivize.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "plzma.db"
        ).build()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideBlockDao(appDatabase: AppDatabase): BlockDao {
        return appDatabase.blockDao()
    }

    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao()
    }

    @Provides
    fun provideFoodTaskDao(appDatabase: AppDatabase): FoodTaskDao {
        return appDatabase.foodTaskDao()
    }

    @Provides
    fun provideTimeDao(appDatabase: AppDatabase): TimeDao {
        return appDatabase.timeDao()
    }
}
