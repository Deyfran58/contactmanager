package Internet
import android.app.Person
import retrofit2.Call
import retrofit2.http.*


interface APIPersona {
    @GET("people/")
    fun getAllPeople(): Call<RespAp<List<Person>>>

    @GET("people/{id}")
    fun getPersonById(@Path("id") id: Long): Call<RespAp<List<Person>>>

    @POST("people/")
    fun createPerson(@Body person: Person): Call<RespAp<Person>>

    @PUT("people/")
    fun updatePerson(@Body person: Person): Call<RespAp<Person>>

    @HTTP(method = "DELETE", path = "people/", hasBody = true)
    fun deletePerson(@Body person: Map<String, String>): Call<RespAp<Void>>
}