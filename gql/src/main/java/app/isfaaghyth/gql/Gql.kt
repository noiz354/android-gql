package app.isfaaghyth.gql

import app.isfaaghyth.gql.network.OkhttpBuilder
import app.isfaaghyth.gql.network.RequestBuilder
import app.isfaaghyth.gql.parser.ParameterParser
import app.isfaaghyth.gql.util.GqlException
import com.google.gson.Gson
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON

open class Gql {

    lateinit var url: String
    lateinit var gqlQuery: String
    val network by lazy { OkhttpBuilder() }
    val requestBuilder by lazy { RequestBuilder(gqlQuery) }
    private val params = mutableMapOf<String, Any>()

    var onError: ((Throwable) -> Unit) = {}

    init {
        System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
    }

    fun setUrl(mainUrl: String) = apply {
        url = mainUrl
    }

    fun query(rawQuery: String) = apply {
        gqlQuery = rawQuery
        collectParameters(rawQuery)
    }

    fun parameters(gqlParams: Map<String, Any>) = apply {
        if (gqlParams.size != params.size) return GqlException(ArrayIndexOutOfBoundsException)

        // validate query keys
        if (!gqlParams.keys.containsAll(params.keys)) {
            return GqlException("$ObjectNotFoundException a few parameters not found")
        }

        // validate variable data type
        params.keys.forEach {
            if (Class.forName(params[it] as String) != gqlParams[it]?.javaClass) {
                return GqlException("$IllegalArgumentException ${gqlParams[it]} is not ${params[it]}")
            }
        }

        // put parameters into gql request builder
        requestBuilder.parameters(gqlParams)
    }

    fun requestValidation() = apply {
        if (!::url.isInitialized) {
            return GqlException("$UrlNotFoundException you haven't set main url yet.")
        }
        if (!::gqlQuery.isInitialized) {
            return GqlException("$QueryNotFoundException you haven't set gql query yet.")
        }
    }

    suspend fun request(response: (String?) -> Unit) = apply {
        requestValidation()
        try {
            val result = network.post(url, requestBuilder.build()).body?.string()
            response(result)
        } catch (e: Throwable) {
            onError(e)
        }
    }

    suspend inline fun <reified T> requestTo(response: (T) -> Unit) = apply {
        requestValidation()
        try {
            val result = network.post(url, requestBuilder.build()).body?.string()
            val fromJson = Gson().fromJson<T>(result, T::class.java)
            response(fromJson)
        } catch (e: Throwable) {
            onError(e)
        }
    }

    fun error(error: (Throwable) -> Unit) = apply {
        onError = error
    }

    private fun collectParameters(query: String) = apply {
        params.clear()
        params.putAll(ParameterParser.parse(query))
    }

    companion object {
        private const val ArrayIndexOutOfBoundsException = "ArrayIndexOutOfBoundsException:"
        private const val IllegalArgumentException = "IllegalArgumentException:"
        private const val ObjectNotFoundException = "ObjectNotFoundException:"
        private const val QueryNotFoundException = "QueryNotFoundException:"
        private const val UrlNotFoundException = "UrlNotFoundException:"
    }

}