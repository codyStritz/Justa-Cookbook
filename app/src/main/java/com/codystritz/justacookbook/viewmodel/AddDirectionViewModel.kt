package com.codystritz.justacookbook.viewmodel

import androidx.lifecycle.*
import com.codystritz.justacookbook.model.Direction
import com.codystritz.justacookbook.model.DirectionDao
import kotlinx.coroutines.launch

class AddDirectionViewModel(
    private val directionDao: DirectionDao
) : ViewModel() {

    fun getDirection(directionId: Int): LiveData<Direction> {
        return directionDao.getDirection(directionId).asLiveData()
    }

    fun deleteDirection(directionId: Int) {
        viewModelScope.launch {
            directionDao.deleteDirection(directionId)
        }
    }

    private fun getUpdatedDirectionEntry(
        directionId: Int,
        recipeId: Int,
        text: String
    ): Direction {
        return Direction(
            id = directionId,
            recipeId = recipeId,
            text = text
        )
    }

    private fun updateDirection(direction: Direction) {
        viewModelScope.launch {
            directionDao.update(direction)
        }
    }

    fun updateDirection(
        directionId: Int,
        recipeId: Int,
        text: String
    ) {
        val updatedDirection = getUpdatedDirectionEntry(directionId, recipeId, text)
        updateDirection(updatedDirection)
    }

    fun isDirectionValid(text: String): Boolean {
        return text.isNotBlank()
    }

    private fun getNewDirectionEntry(recipeId: Int, text: String): Direction {
        return Direction(
            recipeId = recipeId,
            text = text
        )
    }

    private fun insertDirection(direction: Direction) {
        viewModelScope.launch {
            directionDao.insert(direction)
        }
    }

    fun addNewDirection(recipeId: Int, text: String) {
        val newDirection = getNewDirectionEntry(recipeId, text)
        insertDirection(newDirection)
    }
}

class AddDirectionViewModelFactory(
    private val directionDao: DirectionDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddDirectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddDirectionViewModel(directionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}