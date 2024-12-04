package Internet

import data.loginRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterResponse(
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

interface AuthServi {
    @POST("technicians/")
    fun registerTechnician(@Body technicianData: TechnicianData): Call<RegisterResponse>

    @POST("technicians/validateAuth")
    fun validateAuth(@Body credentials: Map<String, String>): Call<loginRes>
}