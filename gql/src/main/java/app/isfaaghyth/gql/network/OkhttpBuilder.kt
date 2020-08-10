package app.isfaaghyth.gql.network

import app.isfaaghyth.gql.util.await
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class OkhttpBuilder {

    private val mediaType = "application/json; charset=utf-8".toMediaType()
    private val okHttpClient by lazy { OkHttpClient() }

    suspend fun post(
        url: String,
        json: String
    ): Response {
        val requestBody = json.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        return okHttpClient
            .newCall(request)
            .await()
    }

}