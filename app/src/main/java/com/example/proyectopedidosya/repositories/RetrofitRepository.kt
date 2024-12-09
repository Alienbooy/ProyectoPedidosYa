package com.example.proyectopedidosya.repositories

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRepository {
    fun getRetrofitInstance(context: Context): Retrofit {
        val client = OkHttpClient.Builder().apply {
            val token = PreferencesRepository.getToken(context)
            if (token != null) {
                addInterceptor { chain ->
                    val original: Request = chain.request()
                    val requestBuilder: Request.Builder = original.newBuilder()
                        .header("Authorization", "Bearer $token")
                    val request: Request = requestBuilder.build()
                    chain.proceed(request)
                }
            }
        }.build()

        return Retrofit.Builder()
            .baseUrl("https://proyectodelivery.jmacboy.com/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
