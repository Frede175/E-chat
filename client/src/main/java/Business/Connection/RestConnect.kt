package Business.Connection


import Acquaintence.IToMap
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.ibm.icu.impl.Trie2
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.util.ArrayList
import java.util.HashMap

import org.apache.http.protocol.HTTP.USER_AGENT

class RestConnect {

    private val url = "https://localhost:5001"
    private val formType = "application/x-www-form-urlencoded"
    //private final String url = "https://ptsv2.com";
    private val gson = Gson()
    // /api/values

    fun login(username: String, password: String): String? {

        try {

            val client = HttpClientBuilder.create().build()

            val request = HttpPost("$url/connect/token/")

            val nvps = ArrayList<NameValuePair>()
            nvps.add(BasicNameValuePair("grant_type", "password"))
            nvps.add(BasicNameValuePair("username", username))
            nvps.add(BasicNameValuePair("password", password))
            request.entity = UrlEncodedFormEntity(nvps)
            val response = client.execute(request)



            println("Response Code Login: " + response.statusLine.statusCode)

            val rd = BufferedReader(
                    InputStreamReader(response.entity.content))

            val result = StringBuffer()
            var line = ""
            while ((line = rd.readLine()) != null) {
                result.append(line)
            }

            println(result.toString())

            val json = JsonParser().parse(result.toString()).asJsonObject

            return json?.get("access_token")?.asString

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null

    }

    operator fun <T, T1> get(path: PathEnum, route: T1, param: HashMap<String, String>, token: String): T? {
        run {
            try {

                val client = HttpClientBuilder.create().build()

                val request = request<T1, HttpGet>(path, route, param, token)

                var response: HttpResponse? = null



                response = client.execute(request)

                println("Response Code Get : " + response!!.statusLine.statusCode)

                val rd = BufferedReader(
                        InputStreamReader(response!!.entity.content))

                val result = StringBuffer()
                var line = ""
                while ((line = rd.readLine()) != null) {
                    result.append(line)
                }

                println(result.toString())

                val type = path.resultType

                return gson.fromJson<T>(result.toString(), type)

            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
        return null
    }

    fun <T, T1, T2> post(path: PathEnum, param: T, content: T1, token: String): T2? {
        try {

            val client = HttpClientBuilder.create().build()

            val request = request<T, HttpPost>(path, param, null, token)
            val postingString = StringEntity(gson.toJson(content))
            request.entity = postingString

            request.addHeader("Content-type", "application/json")
            var response: HttpResponse? = null
            println(request.entity.toString())

            response = client.execute(request)
            println("Response Code Post: " + response!!.statusLine.statusCode)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }


    fun <T, T1, T2> delete(path: PathEnum, route: T, token: String): T2? {
        try {

            val client = HttpClientBuilder.create().build()

            val request = request<T, HttpDelete>(path, route, null, token)


            request.addHeader("Content-type", "application/json")
            var response: HttpResponse? = null

            response = client.execute(request)
            println("Response Code Post: " + response!!.statusLine.statusCode)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    //TODO implement
    fun <T, T1, T2> put(path: PathEnum, route: T, content: T1, token: String): T2? {
        try {
            val client = HttpClientBuilder.create().build()

            val request = request<T, HttpPut>(path, route, null, token)
            val postingString = StringEntity(gson.toJson(content))


            request.entity = postingString

            request.addHeader("Content-type", "application/json")
            var response: HttpResponse? = null
            println(request.entity.toString())

            response = client.execute(request)
            println("Response Code Post: " + response!!.statusLine.statusCode)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null

    }

    /***
     *
     * @param path Which call from client
     * @param route The route variable
     * @param param Params for get method (After the ?)
     * @param token The login token
     * @param <T> Generic type of the route parameter
     * @param <T1> The respective request type
     * @return The respective request
    </T1></T> */
    fun <T, T1> request(path: PathEnum, route: T?, param: HashMap<String, String>?, token: String): T1 {
        var request: HttpRequestBase? = null
        var url = this.url + path.path
        if (route != null) {
            url = url + route.toString()
        }
        if (param != null) {
            url = "$url?"
            for ((key, value) in param) {
                url = url + (key + "=" + value)
            }
        }
        //TODO delete this line
        //url = url.concat("?departmenId=1");


        println(url)
        when (path.type) {
            ConnectionType.POST -> {

                request = HttpPost(url)
            }
            ConnectionType.GET -> {
                request = HttpGet(url)
            }

            ConnectionType.DELETE -> {
                request = HttpDelete(url)
            }

            ConnectionType.PUT -> {
                request = HttpPut(url)
            }
        }
        request.addHeader("User-Agent", USER_AGENT)
        request.addHeader("Authorization", "Bearer $token")


        return request as T1?
    }


}
