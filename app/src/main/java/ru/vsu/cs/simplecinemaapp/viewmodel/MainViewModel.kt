import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.vsu.cs.simplecinemaapp.model.Film
import ru.vsu.cs.simplecinemaapp.model.PageFilmsResponse
import ru.vsu.cs.simplecinemaapp.networking.ApiConfig

class MainViewModel : ViewModel() {

    private val _filmData = MutableLiveData<PageFilmsResponse>()
    val filmData: LiveData<PageFilmsResponse> get() = _filmData

    private val _foundFilmData = MutableLiveData<PageFilmsResponse>()
    val foundFilmData: LiveData<PageFilmsResponse> get() = _foundFilmData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""
        private set

    fun getTopFilms(page: Int = 1) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getTopFilms(page)

        client.enqueue(object : Callback<PageFilmsResponse> {
            override fun onResponse(call: Call<PageFilmsResponse>, response: Response<PageFilmsResponse>) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    onError("Data Processing Error")
                    return
                }

                _isLoading.value = false
                _filmData.postValue(responseBody)
            }

            override fun onFailure(call: Call<PageFilmsResponse>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    fun findByKeyword(keyword: String, page: Int) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().findByKeyword(keyword, page)

        client.enqueue(object : Callback<PageFilmsResponse> {
            override fun onResponse(call: Call<PageFilmsResponse>, response: Response<PageFilmsResponse>) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    onError("Data Processing Error")
                    return
                }
                _isLoading.value = false
                _foundFilmData.postValue(responseBody) // Assuming the response body has a list of films
            }

            override fun onFailure(call: Call<PageFilmsResponse>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    private fun onError(inputMessage: String?) {
        val message = inputMessage ?: "Unknown Error"
        errorMessage = "ERROR: $message some data may not be displayed properly"

        _isError.value = true
        _isLoading.value = false
    }
}
