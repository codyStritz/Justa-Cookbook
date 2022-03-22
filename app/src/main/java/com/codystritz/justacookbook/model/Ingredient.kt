package com.codystritz.justacookbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "recipe_id")
    val recipeId: Int,
    @ColumnInfo(name = "name")
    val ingredientName: String,
    @ColumnInfo(name = "amount")
    val ingredientAmount: String = "",
    @ColumnInfo(name = "isOnShoppingList")
    val isOnShoppingList: Boolean,
    @ColumnInfo(name = "isChecked")
    val isChecked: Boolean
)
