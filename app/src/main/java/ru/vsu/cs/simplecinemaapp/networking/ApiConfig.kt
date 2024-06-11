package ru.vsu.cs.simplecinemaapp.networking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
        fun getApiService(apiKey: String = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"): ApiService {

            // API response interceptor
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            // Client
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(Interceptor { chain ->
                    val requestBuilder: Request.Builder = chain.request().newBuilder()
                    requestBuilder.header("Content-Type", "application/json")
                    requestBuilder.header("X-API-KEY", apiKey)
                    chain.proceed(requestBuilder.build())
                })
                .build()

            // Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl("https://kinopoiskapiunofficial.tech")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}