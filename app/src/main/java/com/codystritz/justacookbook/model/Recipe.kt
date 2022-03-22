package com.codystritz.justacookbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val recipeName: String,
    @ColumnInfo(name = "category")
    val recipeCategory: String,
    @ColumnInfo(name = "servings")
    val servings: String,
    @ColumnInfo(name = "isInMealPlan")
    val isInMealPlan: Boolean,
    @ColumnInfo(name = "note")
    val recipeNote: String
)