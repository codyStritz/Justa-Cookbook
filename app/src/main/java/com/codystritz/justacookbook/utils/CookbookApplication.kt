package com.codystritz.justacookbook.utils

import android.app.Application
import com.codystritz.justacookbook.model.RecipeRoomDatabase

class CookbookApplication : Application() {
    val database: RecipeRoomDatabase by lazy { RecipeRoomDatabase.getDatabase(this) }
}