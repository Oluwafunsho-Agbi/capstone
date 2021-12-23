package com.spamdetector.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.View
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.spamdetector.constants.Constants
import okhttp3.*
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateEncodingException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Helpers {


    fun getDate(milliSeconds: Long): String {
        val dateFormat = "dd/MM/yyyy"
        val formatter = SimpleDateFormat(dateFormat)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun makePostRequest(sms: String?, position: Int, imageView: ImageView, activity: Activity) {
        val payload = "test payload"

        val okHttpClient = OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val sms_data = JsonObject()
        val gson = Gson()
        sms_data.addProperty("sms", sms)
        val json = gson.toJson(sms_data)
        val body: RequestBody = RequestBody.create(JSON, json)
        val request = Request.Builder()
                .method("POST", body)
                .url("https://agbi.loca.lt/sms")
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
                println("failure")
                println(e)

            }

            override fun onResponse(call: Call, response: Response) {
                // Handle this
                if (response.isSuccessful) {
                    val response = response.body()?.string()
                    var obj = JSONObject(response)
                    var isSpam = obj.get("class").equals("spam");

                    activity.runOnUiThread(Runnable {
                        // Stuff that updates the UI
                        if (isSpam)
                            imageView.setVisibility(View.VISIBLE);

                    })

                }

            }
        })
    }
}