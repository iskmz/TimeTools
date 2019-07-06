package com.iskandar.timetools

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ClockActivity : AppCompatActivity() {


    val localTimeStr = "http://worldtimeapi.org/api/ip"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)

        loadLocalTime()
        loadTimezones()
    }


    private fun loadLocalTime() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun loadTimezones() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


/*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        setPointer();

        setWeatherURL("En"); // "English is default" for now //
        getDataJSON();
    }

    private void setWeatherURL(String langCode) {
        for(String city: cityNames)
            myWeather.add(new ClsWeather(city,UtilsWeather.buildWeatherURL(city,langCode),context));
    }


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
