package com.example.foodrecipesdemokotlin.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types


private const val DATABASE_NAME = "recipe_database"

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipes(recipe: DataBaseRecipe): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: DataBaseRecipe)

    @Query("select * from recipes")
    fun getAllRecipes(): LiveData<List<DataBaseRecipe>>

    @Query("select * from recipes WHERE recipe_id = :recipeId")
    suspend fun checkRecipe(recipeId: String): DataBaseRecipe

    @Query("SELECT * FROM recipes WHERE title LIKE '%'||:query||'%' ORDER BY social_rank DESC LIMIT (:page*30)")
    fun getRecipes(query: String, page: Int): LiveData<List<DataBaseRecipe>>

    @Query("update recipes set title=:title, publisher=:publisher, image_url=:imageUrl, social_rank=:socialRank where recipe_id=:recipeId")
    fun updateRecipes(recipeId: String, title: String, publisher: String, imageUrl: String, socialRank: Float)

    @Query("SELECT * FROM recipes WHERE favorite =:favorite")
    fun getAllFavoritesRecipes(favorite: Boolean = true): LiveData<DataBaseRecipe>

    @Query("UPDATE recipes SET favorite=:favorite WHERE recipe_id=:id")
    suspend fun setRecipeFavorite(id: String, favorite: Boolean)

    @Query("SELECT * FROM recipes WHERE recipe_id = :recipeId")
    fun getRecipe(recipeId: String): LiveData<DataBaseRecipe>
}


@Database(entities = [DataBaseRecipe::class], version = 1, exportSchema = false)
@TypeConverters(Convert::class)
abstract class RecipesDatabase() : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}

private lateinit var INSTANCE: RecipesDatabase

fun getDatabase(context: Context): RecipesDatabase {
    synchronized(RecipesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                RecipesDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}


class Convert {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    @FromJson
    fun fromString(string: String): Array<String>? {
        val type = Types.getRawType(Array<String>::class.java)
        val adapter = moshi.adapter<Array<String>>(type)
        return adapter.fromJson(string)
    }

    @TypeConverter
    @ToJson
    fun fromArray(array: Array<String>): String? {
        val type = Types.getRawType(Array<String>::class.java)
        val adapter = moshi.adapter<Array<String>>(type)
        return adapter.toJson(array)
    }
}