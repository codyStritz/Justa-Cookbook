package com.codystritz.justacookbook.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(ingredients: List<Ingredient>)

    @Update
    suspend fun update(ingredient: Ingredient)

    @Query("UPDATE ingredient SET name = :name, amount = :amount WHERE id = :id")
    suspend fun update(id: Int, name: String, amount: String)

    @Delete
    suspend fun delete(ingredient: Ingredient)

    @Query("DELETE from ingredient WHERE id = :id")
    suspend fun deleteIngredient(id: Int)

    @Query("DELETE from ingredient WHERE recipe_id = :recipeId")
    suspend fun deleteRecipeIngredients(recipeId: Int)

    @Query("SELECT * from ingredient WHERE id = :id")
    fun getIngredient(id: Int): Flow<Ingredient>

    @Query("SELECT * from ingredient WHERE recipe_id = :recipeId")
    fun getRecipeIngredients(recipeId: Int): Flow<List<Ingredient>>

    @Query("UPDATE ingredient SET isOnShoppingList = :isOnShoppingList WHERE recipe_id = :recipeId")
    suspend fun setRecipeOnShoppingList(recipeId: Int, isOnShoppingList: Boolean)

    @Query("UPDATE ingredient SET isChecked = 0 WHERE recipe_id = :recipeId")
    suspend fun uncheckRecipeIngredients(recipeId: Int)

    @Query("UPDATE ingredient SET isChecked = :isChecked WHERE id = :id")
    suspend fun setIsChecked(id: Int, isChecked: Boolean)

    @Query("SELECT * from ingredient WHERE isOnShoppingList = 1")
    fun getShoppingList(): Flow<List<Ingredient>>

    @Query("UPDATE ingredient SET isOnShoppingList = 0, isChecked = 0")
    suspend fun clearShoppingList()
}