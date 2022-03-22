package com.codystritz.justacookbook.viewmodel

import androidx.lifecycle.*
import com.codystritz.justacookbook.model.IngredientDao
import com.codystritz.justacookbook.model.Recipe
import com.codystritz.justacookbook.model.RecipeDao
import kotlinx.coroutines.launch

class FilteredRecipeListViewModel(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao
) : ViewModel() {

    val mealPlan: LiveData<List<Recipe>> = recipeDao.getMealPlan().asLiveData()

    fun getFilteredRecipes(category: String): LiveData<List<Recipe>> {
        return recipeDao.getFilteredRecipes(category).asLiveData()
    }

    fun clearMealPlan() {
        viewModelScope.launch {
            recipeDao.clearMealPlan()
            ingredientDao.clearShoppingList()
        }
    }
}

class FilteredRecipeListViewModelFactory(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilteredRecipeListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilteredRecipeListViewModel(recipeDao, ingredientDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}