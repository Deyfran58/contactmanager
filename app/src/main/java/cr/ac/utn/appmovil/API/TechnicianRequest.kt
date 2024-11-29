import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class TechnicianRequest(val id: String, val password: String)

data class TechnicianResponse(
    val data: TechnicianData?,
    val responseCode: Int,
    val message: String
)

data class TechnicianData(
    val id: String,
    val name: String,
    val lastName: String,
    val isActive: Boolean,
    val password: String,
    val isTemporary: Boolean
)

data class DogImage(val url: String)


data class ContactResponse(val message: Map<String, List<String>>, val status: String)
data class DogImagesResponse(val message: List<String>, val status: String)

interface ApiService {
    @POST("technicians/validateAuth")
    fun validateAuth(@Body request: TechnicianRequest): Call<TechnicianResponse>

    @GET("technicians")
    fun getTechnicians(): Call<List<TechnicianData>>

    @GET("https://movil-vaccine-api.azurewebsites.net/people/")
    fun getContact(): Call<ContactResponse>

    @GET("https://dog.ceo/api/breed/{breed}/images")
    fun getDogImages(@Path("breed") breed: String): Call<DogImagesResponse>
}


