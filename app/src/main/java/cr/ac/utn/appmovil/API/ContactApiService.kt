import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ContactApiService {
    @GET("technicians")
    fun getContacts(): Call<List><ContactResponse>
}

data class DogImageResponse(val message: List<String>, val status: String)

