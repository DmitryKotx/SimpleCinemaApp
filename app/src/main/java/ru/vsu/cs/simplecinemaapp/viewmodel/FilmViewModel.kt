import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.vsu.cs.simplecinemaapp.model.FullFilm
import ru.vsu.cs.simplecinemaapp.networking.ApiConfig

class FilmViewModel : ViewModel() {

    private val _film = MutableLiveData<FullFilm>()
    val filmData: LiveData<FullFilm> get() = _film

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""
        private set

    fun getFilmById(id: Int = 1115471) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getFilmById(id)

        client.enqueue(object : Callback<FullFilm> {
            override fun onResponse(call: Call<FullFilm>, response: Response<FullFilm>) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    onError("Data Processing Error")
                    return
                }

                _isLoading.value = false
                _film.postValue(responseBody)
            }

            override fun onFailure(call: Call<FullFilm>, t: Throwable) {
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
