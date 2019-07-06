package com.iskandar.timetools

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import kotlinx.android.synthetic.main.activity_stopwatch.*

class StopwatchActivity : AppCompatActivity() {


    val  TICK_TIME_MSEC = TimeDisplayMSec.MSEC_EIGHTH_TICK

    var stopwatch = TimeDisplayMSec(TICK_TIME_MSEC)
    var lapsCounter = 0
    var playON = false
    var cancelAndRestart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)

        setListeners()
    }

    private fun setListeners() {

        btnStopwatchPlayPause.setOnClickListener {
            playON = !playON
            cancelAndRestart = false
            btnStopwatchPlayPause
                .setImageResource(if (playON) R.drawable.ic_stopwatch_pause else R.drawable.ic_stopwatch_play)
            playPause()
        }

        btnStopwatchFlag.setOnClickListener {
                if(stopwatch != TimeDisplayMSec(TICK_TIME_MSEC)){
                    lapsCounter+=1
                    val previous = txtStopwatchLaps.text.toString()
                    txtStopwatchLaps.text = StringBuilder(lapsCounter.toString()
                            + " " + stopwatch.toString() + "\n" + previous)
                }
        }

        btnStopwatchRestart.setOnClickListener {
            // CANCEL & RESTART option in postExecute //
            cancelAndRestart = true
            // in case clicked when PAUSED
            // i.e. no access to AsyncTask above ! ~ playOn=false //
            clearAllTimeFields()
            btnStopwatchPlayPause.setImageResource(R.drawable.ic_stopwatch_play)
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun playPause() {

        if(playON && !cancelAndRestart){
            object : AsyncTask<TimeDisplayMSec,TimeDisplayMSec,TimeDisplayMSec>(){
                override fun doInBackground(vararg params: TimeDisplayMSec?): TimeDisplayMSec {
                    while (true) {
                        SystemClock.sleep(TICK_TIME_MSEC.toLong())
                        stopwatch.tick()
                        publishProgress(stopwatch)

                        // both of the following lines , will direct to postExecute
                        // for different tasks ! // one for RESET & one for PAUSE //
                        if (cancelAndRestart) break
                        if (!playON) break
                    }

                    return stopwatch
                }

                override fun onPostExecute(timeDisplay: TimeDisplayMSec) {
                    if (!cancelAndRestart) { // on PAUSE //
                        val tmp = TimeDisplayMSec(timeDisplay)
                        stopwatch = tmp // to keep time record when paused //

                    } else
                    // on CANCEL & RESTART //
                    {
                        clearAllTimeFields()
                    }
                    // reset value of booleans
                    cancelAndRestart = false // make sure it become TRUE on next RESTART click //
                    playON = false // to make sure it becomes TRUE on next PLAY click //
                }

                override fun onProgressUpdate(vararg values: TimeDisplayMSec) {
                    if (!cancelAndRestart) {
                        txtStopwatchTime.text =  values[0].toString()
                    }
                }

            }.execute(stopwatch)
        }
    }

    private fun clearAllTimeFields() {
        // clear values
        stopwatch = TimeDisplayMSec(TICK_TIME_MSEC)
        lapsCounter = 0
        // clear fields
        txtStopwatchLaps.text = ""
        txtStopwatchTime.text = "00:00:00"
    }


}


open class TimeDisplay(var sec: Int, var min: Int, var hour: Int) {

    constructor() : this(0, 0, 0)
    constructor(other: TimeDisplay) : this(other.sec, other.min, other.hour)

    open fun tick() {
        this.sec += 1 // tick tock tick tock tick tock
        if (this.sec > 59) {
            this.sec = 0
            this.min += 1
            if (this.min > 59) {
                this.min = 0
                this.hour += 1
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

        other as TimeDisplay

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


class TimeDisplayMSec : TimeDisplay {

    private var msec: Int = 0
    private var tickValue: Int = 0

    constructor(tickValue: Int) : super() {
        this.msec = 0
        this.tickValue = tickValue
    }

    constructor(other: TimeDisplayMSec) : super(other){
        this.msec = other.msec
        this.tickValue = other.tickValue
    }

    // i.e. static members //
    companion object {
        val MSEC_FULL_TICK = 1000
        val MSEC_HALF_TICK = 500
        val MSEC_QUARTER_TICK = 250
        val MSEC_FIFTH_TICK = 200
        val MSEC_EIGHTH_TICK = 125
        val MSEC_TENTH_TICK = 100
    }


    override fun tick() {
        msec += tickValue
        if (msec > 999) {
            msec = 0
            sec += 1
            if (sec > 59) {
                sec = 0
                min += 1
                if (min > 59) {
                    min = 0
                    hour += 1
                }
            }
        }
    }

    override fun toString(): String {
        return super.toString() +
                "." + (if (msec < 100) if (msec < 10) "00" else "0" else "") + msec
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as TimeDisplayMSec

        if (msec != other.msec) return false
        if (tickValue != other.tickValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + msec
        result = 31 * result + tickValue
        return result
    }
}
