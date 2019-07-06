package com.iskandar.timetools

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setListeners()
    }

    private fun setListeners() {

        btnQuit.setOnClickListener { finish() }
        btnInfo.setOnClickListener { showInfoDialog() }

        btnStopwatch.setOnClickListener { startActivity(Intent(this@MainActivity,StopwatchActivity::class.java)) }
        btnClocks.setOnClickListener { startActivity(Intent(this@MainActivity,ClockActivity::class.java)) }
        btnCountdown.setOnClickListener { startActivity(Intent(this@MainActivity,CountdownActivity::class.java)) }
        btnAlarm.setOnClickListener { startActivity(Intent(this@MainActivity,AlarmActivity::class.java)) }
    }

    private fun showInfoDialog() {
        val info = AlertDialog.Builder(this@MainActivity)
            .setTitle("Time Tools ...")
            .setMessage("by Iskandar Mazzawi \u00a9")
            .setIcon(R.drawable.ic_info_outline)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        info.setCanceledOnTouchOutside(false)
        // force LTR //
        info.window?.let{
            it.decorView.textDirection = View.TEXT_DIRECTION_LTR
            it.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        info.show()
    }
}
