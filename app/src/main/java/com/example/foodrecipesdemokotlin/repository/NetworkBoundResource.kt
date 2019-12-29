import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipesdemokotlin.network.ApiEmptyResponse
import com.example.foodrecipesdemokotlin.network.ApiErrorResponse
import com.example.foodrecipesdemokotlin.network.ApiResponse
import com.example.foodrecipesdemokotlin.network.ApiSuccessResponse
import com.example.foodrecipesdemokotlin.repository.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// CacheObject: Type for the Resource data.
// RequestObject: Type for the API response.
abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private val result = MediatorLiveData<Resource<CacheObject>>()
    private val response = MutableLiveData<ApiResponse<RequestObject>>()


    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val cache = loadFromDb()
        result.addSource(cache) { data ->
            result.removeSource(cache)
            if (shouldFetch(data)) { fetchFromNetwork(cache) }
        }
    }


    @MainThread
    private fun setStatus(newValue: Resource<CacheObject>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }


    private fun fetchFromNetwork(cache: LiveData<CacheObject>) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val apiResponse = createCall()
                response.value = apiResponse
                result.addSource(cache) { newData ->
                    setStatus(Resource.loading(newData))
                }
                result.addSource(response) { apiResponse ->
                    result.removeSource(response)
                    result.removeSource(cache)
                    when (apiResponse) {
                        is ApiSuccessResponse -> {
                            GlobalScope.launch {
                                withContext(Dispatchers.IO) {
                                    saveCallResult(
                                        processResponse(
                                            apiResponse
                                        )
                                    )
                                }
                                withContext(Dispatchers.Main) {
                                    result.addSource(loadFromDb()) { newData ->
                                        setStatus(Resource.success(newData))
                                    }
                                }
                            }
                        }
                        is ApiEmptyResponse -> {
                            GlobalScope.launch {
                                withContext(Dispatchers.Main) {
                                    result.addSource(loadFromDb()) { newData ->
                                        setStatus(Resource.success(newData))
                                    }
                                }
                            }
                        }

                        is ApiErrorResponse -> {
                            onFetchFailed()
                            result.addSource(cache) { cachedData ->
                                setStatus(Resource.error(apiResponse.errorMessage, cachedData))
                            }
                        }

                    }
                }
            }

        }
    }


    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestObject)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: CacheObject?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<CacheObject>

    // Called to create the API call.
    @MainThread
    protected abstract suspend fun createCall(): ApiResponse<RequestObject>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestObject>) = response.body

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    fun asLiveData() = result as LiveData<Resource<CacheObject>>
}