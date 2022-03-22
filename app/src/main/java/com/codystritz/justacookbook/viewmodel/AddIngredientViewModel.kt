package com.codystritz.justacookbook.viewmodel

import androidx.lifecycle.*
import com.codystritz.justacookbook.model.*
import kotlinx.coroutines.launch

class AddIngredientViewModel(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao
) : ViewModel() {

    fun retrieveRecipe(id: Int): LiveData<Recipe> {
        return recipeDao.getRecipe(id).asLiveData()
    }

    fun getIngredient(ingredientId: Int): LiveData<Ingredient> {
        return ingredientDao.getIngredient(ingredientId).asLiveData()
    }

    fun deleteIngredient(ingredientId: Int) {
        viewModelScope.launch {
            ingredientDao.deleteIngredient(ingredientId)
        }
    }

    fun updateIngredient(id: Int, name: String, amount: String) {
        viewModelScope.launch {
            ingredientDao.update(id, name, amount)
        }
    }

    fun isIngredientValid(ingredientName: String): Boolean {
        return ingredientName.isNotBlank()
    }

    fun addNewIngredient(
        recipeId: Int,
        ingredientName: String,
        ingredientAmount: String,
        isOnShoppingList: Boolean
    ) {
        val newIngredient =
            getNewIngredientEntry(recipeId, ingredientName, ingredientAmount, isOnShoppingList)
        insertIngredient(newIngredient)
    }

    private fun insertIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            ingredientDao.insert(ingredient)
        }
    }

    private fun getNewIngredientEntry(
        recipeId: Int, ingredientName: String, ingredientAmount: String, isOnShoppingList: Boolean
    ): Ingredient {
        return Ingredient(
            recipeId = recipeId,
            ingredientName = ingredientName,
            ingredientAmount = ingredientAmount,
            isOnShoppingList = isOnShoppingList,
            isChecked = false
        )
    }
}

class AddIngredientViewModelFactory(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddIngredientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddIngredientViewModel(recipeDao, ingredientDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}