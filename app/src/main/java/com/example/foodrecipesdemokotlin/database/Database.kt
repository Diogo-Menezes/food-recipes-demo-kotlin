package com.example.foodrecipesdemokotlin.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import java.lang.reflect.Type


private const val DATABASE_NAME = "recipe_database"

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRecipes(vararg recipe: DataBaseRecipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: DataBaseRecipe)

    @Query("select * from recipes")
    fun getAllRecipes(): LiveData<List<DataBaseRecipe>>


    @Query(
        "select * from recipes where title like '%' or ingredients like '%' ||:query|| '%' order by social_rank desc limit (30*:page)"
    )
    fun getRecipes(query: String, page: Int): LiveData<List<DataBaseRecipe>>

    @Query("update recipes set title=:title, publisher=:publisher, image_url=:imageUrl, social_rank=:socialRank where recipe_id=:recipeId")
    fun updateRecipe(
        recipeId: String,
        title: String,
        publisher: String,
        imageUrl: String,
        socialRank: Float
    )

    @Query("select * from recipes where recipe_id = :recipeId")
    fun getRecipe(recipeId: String): LiveData<DataBaseRecipe>
}


@Database(entities = [DataBaseRecipe::class], version = 1)
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
        val type: Type = Types.getRawType(Array<String>::class.java)
        val adapter = moshi.adapter<Array<String>>(type)
        return adapter.toJson(array)
    }
}