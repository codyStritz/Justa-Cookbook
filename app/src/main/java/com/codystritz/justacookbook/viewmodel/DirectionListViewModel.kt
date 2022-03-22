package com.codystritz.justacookbook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.codystritz.justacookbook.model.Direction
import com.codystritz.justacookbook.model.DirectionDao

class DirectionListViewModel(
    private val directionDao: DirectionDao
) : ViewModel() {

    fun getRecipeDirections(recipeId: Int): LiveData<List<Direction>> {
        return directionDao.getRecipeDirections(recipeId).asLiveData()
    }
}

class DirectionListViewModelFactory(
    private val directionDao: DirectionDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DirectionListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DirectionListViewModel(directionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}