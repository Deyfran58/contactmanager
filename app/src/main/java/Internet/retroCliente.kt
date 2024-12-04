package Internet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object retroCliente {

    private const val BASE_URL = "https://apicontainers.azurewebsites.net/"
    val authInstance: AuthServi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthServi::class.java)
    }
}