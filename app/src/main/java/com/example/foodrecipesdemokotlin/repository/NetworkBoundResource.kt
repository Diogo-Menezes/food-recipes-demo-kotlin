import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipesdemokotlin.network.ApiSuccessResponse
import com.example.foodrecipesdemokotlin.network.NetworkRecipesContainer
import com.example.foodrecipesdemokotlin.repository.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

// CacheObject: Type for the Resource data.
// RequestObject: Type for the API response.
abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private val result = MediatorLiveData<CacheObject>()
    private val _requestResult = MutableLiveData<RequestObject>()
    val requestResult: LiveData<RequestObject>
        get() = _requestResult


    init {
        result.value = null
        @Suppress("LeakingThis")
        val cache = loadFromDb()
        result.addSource(cache) { data ->
            result.removeSource(cache)
            if (shouldFetch(data)) {
                fetchFromNetwork(cache)
            } else {
                result.addSource(cache) {
                    result.postValue(cache.value)
                }
            }
        }
    }

    @MainThread
    private fun setStatus(newValue: Resource<CacheObject>) {
        if (result.value != newValue) {
            result.value = newValue.data
        }
    }


    private fun fetchFromNetwork(cache: LiveData<CacheObject>) {
        Log.i("NetworkBoundResource", "fetchFromNetwork: called")
        GlobalScope.launch(IO) {
            val response = createCall()
            withContext(Main) {
                result.addSource(cache) {
                    Log.i("NetworkBoundResource", "cacheObject: $it")
                    setStatus(Resource.loading(it))
                    result.postValue(it)
                }
                val request = checkCallStatus(response)
                delay(1000L)
                result.addSource(request) {
                    result.removeSource(request)
                    result.removeSource(cache)
                }
                result.addSource(loadFromDb()) {
                    Log.i("NetworkBoundResource", "fetchFromNetwork: $it")
                    setStatus(Resource.success(it))
                    it?.let {result.postValue(it)}
                    result.removeSource(loadFromDb())
                }
            }
        }
    }

    private fun checkCallStatus(response: Response<RequestObject>): LiveData<RequestObject> {
        Log.i("NetworkBoundResource", "checkCallStatus: called")

        //Needs to be executed on the Main Thread
        GlobalScope.launch(Main) {
            if (response.isSuccessful) {
                Log.i("NetworkBoundResource", "checkCallStatus: success")
                val body = response.body()
                if (body == null || response.code() == 204) {
                    _requestResult.value = null
                    Log.i("NetworkBoundResource", "checkCallStatus: empty result")
                    //EMPTY RESPONSE
                } else {
//                    Log.i("NetworkBoundResource", "checkCallStatus: $body")
                    _requestResult.value = body
                    saveCallResult(body)
                }
            } else {
                Log.i("NetworkBoundResource", "checkCallStatus: error")
                val msg = response.errorBody()?.string()
                val errorMessage = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                Log.i("NetworkBoundResource", "checkCallStatus: $errorMessage")
                //ERROR MESSAGE
            }
        }
        return requestResult
    }


    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract suspend fun saveCallResult(item: RequestObject)

    // Called with the data in the database to decide whether to fetch
// potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: CacheObject?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<CacheObject>

    // Called to create the API call.
    @MainThread
    protected abstract suspend fun createCall(): Response<RequestObject>

    // Called when the fetch fails. The child class may want to reset components
// like rate limiter.
    protected open fun onFetchFailed() {}

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestObject>) = response.body

    // Returns a LiveData object that represents the resource that's implemented
// in the base class.
    fun asLiveData() = result as LiveData<CacheObject>
}