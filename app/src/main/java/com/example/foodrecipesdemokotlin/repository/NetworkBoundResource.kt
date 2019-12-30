import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipesdemokotlin.repository.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

// CacheObject: Type for the Resource data.
// RequestObject: Type for the API response.
abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private val result = MediatorLiveData<Resource<CacheObject>>()
    private val _cache = MutableLiveData<CacheObject>()
    private val cache: LiveData<CacheObject>
        get() = _cache

    init {
        setStatus(Resource.loading(null))
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()

        result.addSource(dbSource) { data: CacheObject? ->
            data?.let {
                _cache.value = data
                setStatus(Resource.loading(data))
                result.removeSource(dbSource)
                if (shouldFetch(data)) {
                    fetchFromNetwork(cache)
                } else {
                    result.addSource(dbSource) {
                        _cache.value = it
                        setStatus(Resource.success(it))
                    }
                }
            }
        }
    }

    @MainThread
    private fun setStatus(newValue: Resource<CacheObject>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }


    private fun fetchFromNetwork(cache: LiveData<CacheObject>) {
        Log.i("NetworkBoundResource", "fetchFromNetwork: called")
        GlobalScope.launch(IO) {
            val apiResponse = createCall()
            withContext(Main) {
                //Check Response Status
                result.addSource(cache) {
                    it as List<CacheObject>
                    Log.i("NetworkBoundResource", "cacheSize: ${it.size}")
                    if (it.size > 0) {
                        setStatus(Resource.loading(it))
                    }
                }
                result.addSource(apiResponse) { response ->
                    result.removeSource(apiResponse)
                    result.removeSource(cache)
                    checkCallStatus(apiResponse)

                    GlobalScope.launch(Main) {
                        result.addSource(loadFromDb()) { newData ->
                            setStatus(Resource.success(newData))
                        }
                    }
                }
            }
        }
    }

    private fun checkCallStatus(request: LiveData<Response<RequestObject>>) {
        Log.i("NetworkBoundResource", "checkCallStatus: called")
        //Needs to be executed on the Main Thread
        GlobalScope.launch(Main) {
            result.addSource(request) { response ->
                if (response.isSuccessful) {
                    Log.i("NetworkBoundResource", "checkCallStatus: success")
                    val body = response.body()
                    if (body == null || response.code() == 204) {
                        Log.i("NetworkBoundResource", "checkCallStatus: empty result")
                        //EMPTY RESPONSE
                        result.addSource(loadFromDb()) {
                            setStatus(Resource.success(it))
                        }
                    } else {
                        Log.i("NetworkBoundResource", "checkCallStatus: Content delivered")
                        /*
                        RESPONSE WITH CONTENT SAVE TO DATABASE
                       */
                        this.launch(IO) {
                            Log.i("NetworkBoundResource", "checkCallStatus: saving results...")
                            saveCallResult(body)
                        }
                        result.addSource(loadFromDb()) {
                            Log.i("NetworkBoundResource", "checkCallStatus: load new recipes...")
                            setStatus(Resource.success(it))
                        }
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
                    result.addSource(loadFromDb()) {
                        setStatus(Resource.error(msg!!, it))
                    }
                }
            }
        }
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
    protected abstract suspend fun createCall(): LiveData<Response<RequestObject>>

    // Called when the fetch fails. The child class may want to reset components
// like rate limiter.
    protected open fun onFetchFailed() {}

    @WorkerThread
    protected open fun processResponse(response: Response<RequestObject>) = response.body()

    // Returns a LiveData object that represents the resource that's implemented
// in the base class.
    fun asLiveData() = result as LiveData<Resource<CacheObject>>
}