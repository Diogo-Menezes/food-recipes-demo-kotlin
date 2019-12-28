package com.example.foodrecipesdemokotlin.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type


private const val DATABASE_NAME = "recipe_database"

@Dao
interface RecipeDao {

    @Query("select * from recipes where title like :query limit 30*:page")
    fun getRecipes(query: String, page: Int): LiveData<List<DataBaseRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(vararg recipe: DataBaseRecipe)

}


@Database(entities = [DataBaseRecipe::class], version = 2)
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
                .build()
        }
    }
    return INSTANCE
}


class Convert {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromString(string: String): Array<String> {
        val type: Type = Types.getRawType(Array<String>::class.java)
        val adapter: JsonAdapter<Array<String>> = moshi.adapter(type)
        return adapter.fromJson(string)!!
    }


    @TypeConverter
    fun fromArrayList(array: Array<String>): String {
        val type: Type = Types.getRawType(String::class.java)
        val adapter: JsonAdapter<String> = moshi.adapter(type)
        return adapter.toJson(array.toString())
    }
}
