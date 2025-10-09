package api

import com.example.loloytar.model.Plato
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface PlatoApi{

    @GET("Plato")
    suspend fun getPlatos(): List<Plato>

    @GET("Plato/{id}")
    suspend fun getPlato(@Path("id") id: Int): Plato

    @POST("Plato")
    suspend fun postPlato(@Body plato: Plato): Plato

    @PUT("Plato/{id}")
    suspend fun putPlato(@Path("id") id: Int, @Body plato: Plato): Plato

    @DELETE("Plato/{id}")
    suspend fun deletePlato(@Path("id") id: Int)
}

