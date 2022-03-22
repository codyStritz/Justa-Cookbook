package com.codystritz.justacookbook.viewmodel

import androidx.lifecycle.*
import com.codystritz.justacookbook.model.Ingredient
import com.codystritz.justacookbook.model.IngredientDao
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    private val ingredientDao: IngredientDao
) : ViewModel() {

    val shoppingList: LiveData<List<Ingredient>> = ingredientDao.getShoppingList().asLiveData()

    fun setIsChecked(id: Int, isChecked: Boolean) {
        viewModelScope.launch {
            ingredientDao.setIsChecked(id, isChecked)
        }
    }
}

class ShoppingListViewModelFactory(
    private val ingredientDao: IngredientDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingListViewModel(ingredientDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

