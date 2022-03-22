package com.codystritz.justacookbook.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DirectionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(direction: Direction)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(directions: List<Direction>)

    @Update
    suspend fun update(direction: Direction)

    @Delete
    suspend fun delete(direction: Direction)

    @Query("DELETE from direction WHERE id = :id")
    suspend fun deleteDirection(id: Int)

    @Query("DELETE from direction WHERE recipe_id = :recipeId")
    suspend fun deleteRecipeDirections(recipeId: Int)

    @Query("SELECT * from direction WHERE id = :id")
    fun getDirection(id: Int): Flow<Direction>

    @Query("SELECT * from direction WHERE recipe_id = :recipeId")
    fun getRecipeDirections(recipeId: Int): Flow<List<Direction>>
}