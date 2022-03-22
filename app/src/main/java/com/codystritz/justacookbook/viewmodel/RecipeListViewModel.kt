package com.codystritz.justacookbook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.codystritz.justacookbook.model.Recipe
import com.codystritz.justacookbook.model.RecipeDao

class RecipeListViewModel(private val recipeDao: RecipeDao) : ViewModel() {

    val allRecipes: LiveData<List<Recipe>> = recipeDao.getRecipes().asLiveData()

    fun getSearchResults(searchTerm: String?): LiveData<List<Recipe>> {
        return recipeDao.getSearchResults(searchTerm ?: "").asLiveData()
    }
}

class RecipeListViewModelFactory(
    private val recipeDao: RecipeDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeListViewModel(recipeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}