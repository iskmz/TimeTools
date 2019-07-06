package com.iskandar.timetools

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_clock.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class LocalTime(val timezone:String, val abbr:String, val date:String, val time:String, val utc_offset:String)

class ClockActivity : AppCompatActivity() {

    val context = this@ClockActivity as Context

    val localTimeURL = "http://worldtimeapi.org/api/ip"
    val timezonesURL = "http://worldtimeapi.org/api/timezone"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)

        loadLocalTime()
        //loadTimezones()
    }

    @SuppressLint("StaticFieldLeak")
    private fun loadLocalTime() {

        var localTime:LocalTime? = null

        object : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg params: Void?): String? {

                // check for Internet connection first ! //
                if(!hasInternet()) return null

                // get JSON data
                val inSt = URL(localTimeURL).content as InputStream
                return inSt.bufferedReader().use { it.readText() }
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)

                // parse JSON data // if NOT null
                result?.let{
                    val jsonObj = JSONObject(it)
                        localTime = LocalTime(
                            jsonObj.getString("timezone"),
                            jsonObj.getString("abbreviation"),
                            jsonObj.getString("datetime").split("T")[0],
                            jsonObj.getString("datetime").split("T")[1].split(".")[0],
                            jsonObj.getString("utc_offset")
                        )
                }

                // set TextView with new data // if localTime NOT null
                localTime?.let{
                    txtLocalTime.text = StringBuilder(" timezone: ${it.timezone} , ${it.abbr}" +
                            "\n\n date: ${it.date} \n\n time: ${it.time} , ${it.utc_offset}")
                }
            }

        }.execute()


    }

    /*
    // LOCAL TIME json example //
    {"week_number":27,"utc_offset":"+03:00","utc_datetime":"2019-07-06T20:22:12.351816+00:00","unixtime":1562444532,"timezone":"Asia/Jerusalem","raw_offset":7200,"dst_until":"2019-10-26T23:00:00+00:00","dst_offset":3600,"dst_from":"2019-03-29T00:00:00+00:00","dst":true,"day_of_year":187,"day_of_week":6,"datetime":"2019-07-06T23:22:12.351816+03:00","client_ip":"85.130.211.80","abbreviation":"IDT"}
     */

    private fun loadTimezones() {

    }


    private fun hasInternet():Boolean {
        // if we have internet >> true , otherwise>> false
        val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}
