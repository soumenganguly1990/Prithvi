package com.soumen.prithvi.rest

import com.soumen.prithvi.extras.AppCommonValues
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

/**
 * Created by Soumen on 04-12-2017.
 */
class ApiClient {
    companion object {
        val BASE_URL = AppCommonValues.BASEURL
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit!!
        }
    }
}