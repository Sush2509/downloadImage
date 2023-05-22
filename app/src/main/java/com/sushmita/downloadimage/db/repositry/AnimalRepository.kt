package com.sushmita.downloadimage.db.repositry

import com.sushmita.downloadimage.db.dao.AnimalDao
import com.sushmita.downloadimage.db.dto.Animal


class AnimalRepository(private val animalDao: AnimalDao) {

    // get all the animal
    fun getAllAnimals(): List<Animal> = animalDao.getAllAnimal()

    fun getRowCount(): Int = animalDao.getRowCount()

    // adds an animal to our database.
    suspend fun insertAnimal(animal: Animal) {
        animalDao.insertAnimal(animal)
    }

    // deletes an animal from database.
    suspend fun deleteAnimal(animal: Animal) {
        animalDao.deleteAnimal(animal)
    }

    // updates an animal from database.
    suspend fun updateAnimal(animal: Animal) {
        animalDao.updateAnimal(animal)
    }

    //delete an animal by id.
    suspend fun deleteAnimalById(id: Int) = animalDao.deleteAnimalById(id)

    // delete all animal
    suspend fun clearAnimal() = animalDao.clearAnimal()
}