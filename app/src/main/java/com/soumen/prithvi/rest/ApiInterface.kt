package com.soumen.prithvi.rest

import com.soumen.prithvi.extras.AppCommonValues
import com.soumen.prithvi.models.Country
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Soumen on 04-12-2017.
 */
interface ApiInterface {
    @GET(AppCommonValues.COUNTRYURL)
    fun getAllCountryList(): Call<List<Country>>
}