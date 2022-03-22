package com.codystritz.justacookbook.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe): Long

    @Update
    suspend fun update(recipe: Recipe)

    @Query("UPDATE recipe SET isInMealPlan = :isInMealPlan WHERE id = :id")
    suspend fun update(id: Int, isInMealPlan: Boolean)

    @Query("UPDATE recipe SET name = :name, category = :category, servings = :servings WHERE id = :id")
    suspend fun update(id: Int, name: String, category: String, servings: String)

    @Query("UPDATE recipe SET note = :note WHERE id = :id")
    suspend fun updateNote(id: Int, note: String)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("DELETE from recipe WHERE id = :id")
    suspend fun deleteRecipeById(id: Int)

    @Query("SELECT * from recipe WHERE id = :id")
    fun getRecipe(id: Int): Flow<Recipe>

    @Query("SELECT * from recipe ORDER BY name ASC")
    fun getRecipes(): Flow<List<Recipe>>

    @Query("SELECT * from recipe WHERE category = :category ORDER BY name ASC")
    fun getFilteredRecipes(category: String): Flow<List<Recipe>>

    @Query("SELECT id from recipe WHERE isInMealPlan = 1 ORDER BY name ASC")
    fun getMealPlanIds(): List<Int>

    @Query("SELECT * from recipe WHERE isInMealPlan = 1 ORDER BY name ASC")
    fun getMealPlan(): Flow<List<Recipe>>

    @Query("SELECT MAX(id) from recipe")
    fun getNewestRecipeId(): Flow<Int>

    @Query("UPDATE recipe SET isInMealPlan = 0")
    suspend fun clearMealPlan()

    @Query("SELECT * FROM recipe WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name ASC")
    fun getSearchResults(searchTerm: String): Flow<List<Recipe>>

}