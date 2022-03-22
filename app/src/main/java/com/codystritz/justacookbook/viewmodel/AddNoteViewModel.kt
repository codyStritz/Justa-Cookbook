package com.codystritz.justacookbook.viewmodel

import androidx.lifecycle.*
import com.codystritz.justacookbook.model.Recipe
import com.codystritz.justacookbook.model.RecipeDao
import kotlinx.coroutines.launch

class AddNoteViewModel(
    private val recipeDao: RecipeDao
) : ViewModel() {

    fun updateNote(recipeId: Int, note: String) {
        viewModelScope.launch {
            recipeDao.updateNote(recipeId, note)
        }
    }

    fun retrieveRecipe(id: Int): LiveData<Recipe> {
        return recipeDao.getRecipe(id).asLiveData()
    }
}

class AddNoteViewModelFactory(
    private val recipeDao: RecipeDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNoteViewModel(recipeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}