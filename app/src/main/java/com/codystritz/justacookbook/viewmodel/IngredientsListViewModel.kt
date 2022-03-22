package com.codystritz.justacookbook.viewmodel

import androidx.lifecycle.*
import com.codystritz.justacookbook.model.DirectionDao
import com.codystritz.justacookbook.model.Ingredient
import com.codystritz.justacookbook.model.IngredientDao
import com.codystritz.justacookbook.model.RecipeDao
import kotlinx.coroutines.launch

class IngredientsListViewModel(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val directionDao: DirectionDao
) : ViewModel() {

    val newestRecipeId: LiveData<Int> = recipeDao.getNewestRecipeId().asLiveData()

    fun getRecipeIngredients(recipeId: Int?): LiveData<List<Ingredient>> {
        return ingredientDao.getRecipeIngredients(recipeId ?: 0).asLiveData()
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

class IngredientsListViewModelFactory(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val directionDao: DirectionDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngredientsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngredientsListViewModel(recipeDao, ingredientDao, directionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}