import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.foodrecipesdemokotlin.util.Resource
import com.example.foodrecipesdemokotlin.util.ResourceStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Response

// CacheObject: Type for the Resource data.
// RequestObject: Type for the API response or ApiObject

abstract class NetworkBoundResource<CacheObject, RequestObject>(loadFromInternet: Boolean) {

    private val result = MediatorLiveData<Resource<CacheObject>>()

    private lateinit var coroutineScope: CoroutineScope
    protected var job: CompletableJob = Job()
    private val startTime = System.currentTimeMillis()

    init {
        initJob()
        Log.i("NetworkBoundResource", "start time: ${timeElapsed()} ")
        setResult(Resource.loading(null))
        @Suppress("LeakingThis")
        val cache = loadFromDb()
        result.addSource(cache) { cachedData: CacheObject? ->
            setResult(Resource.loading(cachedData))
            Log.i("NetworkBoundResource", "load cache time: ${timeElapsed()}")
            result.removeSource(cache)
            if (loadFromInternet) {
                if (shouldFetch(cachedData)) fetchFromNetwork(cache)
            } else {
                loadFromDatabaseAndReturn(cache)
            }
        }
    }

    //For debug
    protected fun timeElapsed() = System.currentTimeMillis() - startTime

    private fun loadFromDatabaseAndReturn(cache: LiveData<CacheObject>) {
        coroutineScope.launch {
            Log.i("NetworkBoundResource", "setResult: ${timeElapsed()}")
            withContext(Main) {
                result.addSource(cache) {
                    setResult(Resource.success(it))
                    result.removeSource(cache)
                }
            }
        }
    }

    private fun initJob() {
        if (job.isActive) {
            job.cancel()
        }
        job = Job()
        coroutineScope = CoroutineScope(IO + job)

    }

    @MainThread
    private fun setResult(newValue: Resource<CacheObject>) {
        Log.i("NetworkBoundResource", "setResult: ${newValue.status} ${timeElapsed()}")
        if (result.value != newValue) {
            result.value = newValue
        }

        if (newValue.status == ResourceStatus.SUCCESS || newValue.status == ResourceStatus.ERROR) {
            job.complete()
        }
    }


    private fun fetchFromNetwork(cache: LiveData<CacheObject>) {
        Log.i("NetworkBoundResource", "fetchFromNetwork: called  ${timeElapsed()}")
        coroutineScope.launch(IO) {
            val apiResponse = createCall()

            withContext(Main) {
                result.addSource(apiResponse) {
                    Log.i("NetworkBoundResource", "response: ${timeElapsed()}")
                    result.removeSource(apiResponse)
                    checkResponse(apiResponse)
                }
            }
        }
    }

    private fun checkResponse(request: LiveData<Response<RequestObject>>) {
        Log.i("NetworkBoundResource", "checkCallStatus: called ${timeElapsed()}")
        //Needs to be executed on the Main Thread
        coroutineScope.launch(Main) {
            result.addSource(request) { response ->
                result.removeSource(request)

                if (response.isSuccessful) {
                    Log.i("NetworkBoundResource", "checkCallStatus: success  ${timeElapsed()}")
                    val body = response.body()
                    if (body == null || response.code() == 204) {
                        Log.i(
                            "NetworkBoundResource",
                            "checkCallStatus: empty result  ${timeElapsed()}"
                        )
                        //EMPTY RESPONSE
                        result.addSource(loadFromDb()) {
                            setResult(Resource.success(it))
                        }
                    } else {
                        Log.i(
                            "NetworkBoundResource",
                            "checkCallStatus: Content delivered  ${timeElapsed()}"
                        )
                        /*
                        RESPONSE WITH CONTENT SAVE TO DATABASE
                       */
                        this.launch(IO) {
                            Log.i(
                                "NetworkBoundResource",
                                "checkCallStatus: saving results...  ${timeElapsed()}"
                            )
                            saveCallResult(body)
                            withContext(Main) {
                                result.addSource(loadFromDb()) {
                                    Log.i(
                                        "NetworkBoundResource",
                                        "checkCallStatus: load new recipes... ${timeElapsed()}"
                                    )
                                    result.removeSource(loadFromDb())
                                    setResult(Resource.success(it))
                                }
                            }
                        }
                    }
                } else {
                    Log.i(
                        "NetworkBoundResource",
                        "checkCallStatus: error ${timeElapsed()}"
                    )
                    val msg = response.errorBody()?.string()
                    val errorMessage = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        msg
                    }
                    Log.i(
                        "NetworkBoundResource",
                        "checkCallStatus: $errorMessage ${timeElapsed()}"
                    )
                    result.addSource(loadFromDb()) {
                        setResult(Resource.error(msg!!, it))
                    }
                }
            }
        }
    }

    protected fun cancelJob(reason: String) {
        job.cancel()
        result.addSource(loadFromDb()) {
            result.removeSource(loadFromDb())
            setResult(Resource.error(reason, it))
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
    protected open fun onFetchFailed() {}


    // Returns a LiveData object that represents the resource that's implemented
// in the base class.
    fun asLiveData() = result as LiveData<Resource<CacheObject>>
}