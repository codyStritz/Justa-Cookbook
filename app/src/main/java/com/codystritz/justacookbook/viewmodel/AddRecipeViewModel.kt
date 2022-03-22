package com.codystritz.justacookbook.viewmodel

import androidx.lifecycle.*
import com.codystritz.justacookbook.model.Recipe
import com.codystritz.justacookbook.model.RecipeDao
import kotlinx.coroutines.launch

class AddRecipeViewModel(
    private val recipeDao: RecipeDao
) : ViewModel() {

    fun isEntryValid(recipeName: String): Boolean {
        return recipeName.isNotBlank()
    }

    fun retrieveRecipe(id: Int): LiveData<Recipe> {
        return recipeDao.getRecipe(id).asLiveData()
    }

    fun addNewRecipe(recipeName: String, recipeCategory: String, servings: String) {
        val newRecipeEntry = getNewRecipeEntry(recipeName, recipeCategory, servings)
        insertRecipe(newRecipeEntry)
    }

    private fun getNewRecipeEntry(
        recipeName: String,
        recipeCategory: String,
        servings: String
    ): Recipe {
        return Recipe(
            recipeName = recipeName,
            recipeCategory = recipeCategory,
            servings = servings,
            isInMealPlan = false,
            recipeNote = ""
        )
    }

    private fun insertRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeDao.insert(recipe)
        }
    }

    fun updateRecipe(
        recipeId: Int,
        recipeName: String,
        recipeCategory: String,
        servings: String
    ) {
        viewModelScope.launch {
            recipeDao.update(recipeId, recipeName, recipeCategory, servings)
        }
    }
}

class AddRecipeViewModelFactory(
    private val recipeDao: RecipeDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddRecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddRecipeViewModel(recipeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}