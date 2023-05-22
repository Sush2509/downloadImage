package com.sushmita.downloadimage.module

import android.app.Application
import android.content.Context
import com.sushmita.downloadimage.db.AnimalDatabase
import com.sushmita.downloadimage.db.dao.AnimalDao
import com.sushmita.downloadimage.db.repositry.AnimalRepository
import com.sushmita.downloadimage.viewmodel.GridFragmentVM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class GridFragmentModule {

    @Provides
    fun provideGridFragmentVM(application: Application): GridFragmentVM {
        return GridFragmentVM(application)
    }

    @Provides
    fun provideAnimalRepository(animalDao: AnimalDao): AnimalRepository {
        return AnimalRepository(animalDao)
    }

    @Provides
    fun provideAnimalDao(database: AnimalDatabase): AnimalDao {
        return database.getAnimalDao()
    }

    @Provides
    fun provideAnimalDatabase(@ApplicationContext appContext: Context): AnimalDatabase {
        return AnimalDatabase.getDatabase(appContext)
    }
}