package com.sushmita.downloadimage.db.dao

import androidx.room.*
import com.sushmita.downloadimage.db.dto.Animal

@Dao
interface AnimalDao {
    @Query("SELECT COUNT(*) FROM animalTable")
    fun getRowCount(): Int

    // adds a new entry to our database.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimal(animal : Animal)

    // deletes an animal
    @Delete
    suspend fun deleteAnimal(animal: Animal)

    // updates an animal.
    @Update
    suspend fun updateAnimal(animal: Animal)

    // read all the animal from animalTable
    @Query("Select * from animalTable order by animal_id ASC")
    fun getAllAnimal(): List<Animal>

    // delete all animal
    @Query("DELETE FROM animalTable")
    suspend fun clearAnimal()

    //you can use this too, to delete an animal by id.
    @Query("DELETE FROM animalTable WHERE animal_id = :id")
    suspend fun deleteAnimalById(id: Int)

}