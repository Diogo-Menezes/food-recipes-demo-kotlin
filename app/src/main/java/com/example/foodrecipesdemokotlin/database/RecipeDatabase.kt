//package com.example.foodrecipesdemokotlin.database
//
//import android.content.Context
//import androidx.lifecycle.LiveData
//import androidx.room.*
//
//private const val DATABASE_NAME = "recipe_database"
//
//@Database(entities = [DataBaseRecipe::class], version = 1)
//abstract class RecipeDatabase() : RoomDatabase() {
//    abstract val recipeDao: RecipeDao
//}
//
//private lateinit var INSTANCE: RecipeDatabase
//
//fun getDatabase(context: Context): RecipeDatabase {
//    synchronized(RecipeDatabase::class.java) {
//        if (!::INSTANCE.isInitialized) {
//            INSTANCE = Room.databaseBuilder(
//                context.applicationContext,
//                RecipeDatabase::class.java,
//                DATABASE_NAME
//            )
//                .build()
//        }
//    }
//    return INSTANCE
//}
//
//@Dao
//interface RecipeDao {
//
//    @Query("select * from recipes where title like :query limit 10*:page")
//    fun getRecipes(query: String, page: Int): LiveData<List<DataBaseRecipe>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertRecipes(vararg recipe: DataBaseRecipe)
//
//}