package com.iskandar.timetools

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ClockActivity : AppCompatActivity() {


    val localTimeURL = "http://worldtimeapi.org/api/ip"
    val timezonesURL = "http://worldtimeapi.org/api/timezone"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)

        //loadLocalTime()
        //loadTimezones()
    }


    private fun loadLocalTime() {

    }

    /*
    {"week_number":27,"utc_offset":"+03:00","utc_datetime":"2019-07-06T20:22:12.351816+00:00","unixtime":1562444532,"timezone":"Asia/Jerusalem","raw_offset":7200,"dst_until":"2019-10-26T23:00:00+00:00","dst_offset":3600,"dst_from":"2019-03-29T00:00:00+00:00","dst":true,"day_of_year":187,"day_of_week":6,"datetime":"2019-07-06T23:22:12.351816+03:00","client_ip":"85.130.211.80","abbreviation":"IDT"}
     */

    private fun loadTimezones() {

    }
}


/*


    @SuppressLint("StaticFieldLeak")
    private void getDataJSON() {
        new AsyncTask<Void, Void, List<ClsWeather>>() {
            @Override
            protected List<ClsWeather> doInBackground(Void... voids) {

                HttpURLConnection connection = null;
                // for each city, make connection, and get its own data //
                for(int i=0; i<myWeather.size(); i+=1) {
                    try {
                        connection = (HttpURLConnection) new URL(
                                myWeather.get(i).getApiURL()).openConnection();
                        BufferedReader buf = new BufferedReader(new InputStreamReader(
                                connection.getInputStream()));
                        String line, jsonStringElement="";
                        while ((line = buf.readLine()) != null) {
                            jsonStringElement += line;
                        }
                        myWeather.get(i).setJsonString(jsonStringElement);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        connection.disconnect();
                    }
                }

                return myWeather;
            }

            @Override
            protected void onPostExecute(List<ClsWeather> s) {
                super.onPostExecute(s);
                parseJson();
            }
        }.execute();
    }

    private void parseJson() {

        for(int i=0; i<myWeather.size(); i+=1) {
            try {

                JSONObject jsonObject = new JSONObject(myWeather.get(i).getJsonString());
                JSONObject currently = jsonObject.getJSONObject("currently");

                //  getting desired data // and putting it in list
                myWeather.get(i).setWeatherData(
                        currently.getLong("time"),
                        currently.getString("summary"),
                        currently.getDouble("temperature"),
                        (int)(currently.getDouble("humidity")*100),
                        currently.getString("icon")
                );


                // Block to check icons appearance

//                myWeather.get(0).setIconTxt("snow");
//                myWeather.get(1).setIconTxt("thunderstorm");
//                myWeather.get(2).setIconTxt("tornado");
//                myWeather.get(3).setIconTxt("tornado");
//                myWeather.get(4).setIconTxt("wind");

                // end of check-block


                /* adapter */
                WeatherAdapter myAdp = new WeatherAdapter(context,myWeather,langCode);
                lstWeather.setAdapter(myAdp);
                txtFetchingData.setText("");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

 */
