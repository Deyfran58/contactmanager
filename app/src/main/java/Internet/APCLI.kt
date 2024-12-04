package Internet
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object APCLI {
    private const val BASE_URL = "https://movil-vaccine-api.azurewebsites.net/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}