package api

import android.telecom.Call
import model.Plato
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PlatoApi {

    @GET("api/plato")
    fun getPlatos(): Call<List<Plato>>

    @GET("api/plato/{id}")
    fun getPlato(@Path("id") id: Int): Call<Plato>

    @POST("api/plato")
    fun createPlato(@Body plato: Plato): Call<Plato>

    @PUT("api/plato/{id}")
    fun updatePlato(@Path("id") id: Int, @Body plato: Plato): Call<Void>

    @DELETE("api/plato/{id}")
    fun deletePlato(@Path("id") id: Int): Call<Void>
}