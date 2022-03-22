package com.codystritz.justacookbook.viewmodel

import androidx.lifecycle.*
import com.codystritz.justacookbook.model.*
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val directionDao: DirectionDao
) : ViewModel() {

    fun retrieveRecipe(id: Int): LiveData<Recipe> {
        return recipeDao.getRecipe(id).asLiveData()
    }

    fun getRecipeIngredients(recipeId: Int?): LiveData<List<Ingredient>> {
        return ingredientDao.getRecipeIngredients(recipeId ?: 0).asLiveData()
    }

    fun getRecipeDirections(recipeId: Int): LiveData<List<Direction>> {
        return directionDao.getRecipeDirections(recipeId).asLiveData()
    }

    fun toggleInMealPlan(recipe: Recipe) {
        viewModelScope.launch {
            recipeDao.update(recipe.id, !recipe.isInMealPlan)
        }
    }

    fun setRecipeOnShoppingList(recipeId: Int, isOnShoppingList: Boolean) {
        viewModelScope.launch {
            ingredientDao.setRecipeOnShoppingList(recipeId, isOnShoppingList)
        }
    }

    fun uncheckRecipeIngredients(recipeId: Int) {
        viewModelScope.launch {
            ingredientDao.uncheckRecipeIngredients(recipeId)
        }
    }

    fun deleteRecipe(id: Int?) {
        if (id != null) {
            viewModelScope.launch {
                recipeDao.deleteRecipeById(id)
                ingredientDao.deleteRecipeIngredients(id)
                directionDao.deleteRecipeDirections(id)
            }
        }
    }
}

class RecipeDetailViewModelFactory(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val directionDao: DirectionDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailViewModel(recipeDao, ingredientDao, directionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}