package com.iskandar.timetools

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_countdown.*
import org.w3c.dom.Text

class CountdownActivity : AppCompatActivity() {


    val HOURS_MAX_VALUE = 99

    companion object {
        val HOURS = "h"
        val MINUTES = "m"
        val SECONDS = "s"
    }

    val context = this@CountdownActivity as Context

    lateinit var timeCountdown:TimeCountdown
    lateinit var countdownService: CountdownService
    var countdownOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        initialize()
    }

    private fun initialize() {
        timeCountdown = TimeCountdown()
        // countdownService = CountdownService(timeCountdown,txtCountdown)
        //countdownService = CountdownService()
        // countdownService.setTxtViewRef(txtCountdown)
        setNumberPickers()
        setButtons()
    }



    private fun setNumberPickers() {
        // min , max values //
        pickerHours.minValue = 0
        pickerHours.maxValue = HOURS_MAX_VALUE
        pickerMinutes.minValue = 0
        pickerMinutes.maxValue = 59
        pickerSeconds.minValue = 0
        pickerSeconds.maxValue = 59

        // initial values //
        pickerHours.value = 0
        pickerMinutes.value = 0
        pickerSeconds.value = 0
    }

    private fun setButtons() {
        btnCountdownReset.setOnClickListener {
            stopService(Intent(context,CountdownService::class.java))
            setNumberPickers()
            txtCountdown.text = getString(R.string.countdown_placeholder)
            btnCountdownStart.setImageResource(R.drawable.ic_stopwatch_play)
            countdownOn = false
        }

        btnCountdownStart.setOnClickListener {
            if(countdownOn){
                // STOP // PAUSE ! ... NOT RESET !! //
                stopService(Intent(context,CountdownService::class.java))
                countdownOn=false
                btnCountdownStart.setImageResource(R.drawable.ic_stopwatch_play)
            }
            else{
                // START / RESUME //
                txtCountdown.text = getTimeFromPickers()
                with(txtCountdown.text.split(":")){
                    timeCountdown = TimeCountdown(this[2].toInt(),this[1].toInt(),this[0].toInt())
                }

                intent = Intent(context,CountdownService::class.java)
                intent.putExtra(HOURS,timeCountdown.hour)
                intent.putExtra(MINUTES,timeCountdown.min)
                intent.putExtra(SECONDS,timeCountdown.sec)

                startService(intent)

                countdownOn=true
                btnCountdownStart.setImageResource(R.drawable.ic_stopwatch_pause)
            }

        }
    }

    private fun getTimeFromPickers(): CharSequence {
        return TimeCountdown(pickerSeconds.value, pickerMinutes.value, pickerHours.value).toString()
    }
}



class TimeCountdown(var sec: Int, var min: Int, var hour: Int) {

    constructor() : this(0, 0, 0)
    constructor(other: TimeCountdown) : this(other.sec, other.min, other.hour)

    fun tickDown() {
        this.sec -= 1 // tick tock tick tock tick tock
        if (this.sec < 0) {
            this.sec = 59
            this.min -= 1
            if (this.min < 0) {
                this.min = 59
                this.hour -= 1
            }
        }
    }

    override fun toString(): String {
        return (if (hour < 10) "0" else "") + hour + ":" +
                (if (min < 10) "0" else "") + min + ":" +
                (if (sec < 10) "0" else "") + sec
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeCountdown

        if (sec != other.sec) return false
        if (min != other.min) return false
        if (hour != other.hour) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sec
        result = 31 * result + min
        result = 31 * result + hour
        return result
    }


}



class CountdownService : Service {
    private val startTime: TimeCountdown
    private var tv:TextView? = null
    private var isServiceOn = false

    private lateinit var currentTime:TimeCountdown

    private constructor(startTime: TimeCountdown, txtV:TextView?) : super() {
        this.startTime = startTime
        this.tv = txtV
    }

    constructor() : this(TimeCountdown(0,0,0),null) // default c'tor for the service

    fun setTxtViewRef(txtV:TextView){
        this.tv = txtV
    }

    val ONE_SECOND = 1000

    override fun onBind(intent: Intent?): IBinder? = null


    override fun onCreate() {
        super.onCreate()

        currentTime = startTime
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let{

            val seconds = it.getIntExtra(CountdownActivity.SECONDS,0)
            val minutes = it.getIntExtra(CountdownActivity.MINUTES,0)
            val hours = it.getIntExtra(CountdownActivity.HOURS,0)

            currentTime = TimeCountdown(seconds,minutes,hours)

            isServiceOn=true
            resumeCountdown(currentTime)
        }

        return START_NOT_STICKY
    }


    @SuppressLint("StaticFieldLeak")
    private fun resumeCountdown(currentTime: TimeCountdown) {

        object : AsyncTask<TimeCountdown,TimeCountdown,TimeCountdown>(){
            override fun doInBackground(vararg params: TimeCountdown): TimeCountdown {

                while(isServiceOn && params[0] != TimeCountdown(0,0,0)){
                    SystemClock.sleep(ONE_SECOND.toLong())
                    currentTime.tickDown()
                    publishProgress(currentTime)

                    Log.e("countdown: ",currentTime.toString()) // working ok ! //
                }

                return params[0]
            }

            override fun onProgressUpdate(vararg values: TimeCountdown) {
                Log.e("is tv null again?", (tv==null).toString()) // NULL !  WHY ?!?!?! //
                tv?.let{ it.text = values[0].toString() }
            }

            override fun onPostExecute(result: TimeCountdown) {
                    tv?.let { it.text = result.toString() }
                    if (isServiceOn) playTune()
            }

            private fun playTune() {
                val mp = MediaPlayer.create(applicationContext,R.raw.countdown_done)
                mp.start()

                mp.setOnCompletionListener {
                    mp.release()
                    this@CountdownService.stopSelf()
                }
            }

        }.execute(currentTime)
    }

    override fun onDestroy() {
        isServiceOn = false
        tv = null
    }


}
