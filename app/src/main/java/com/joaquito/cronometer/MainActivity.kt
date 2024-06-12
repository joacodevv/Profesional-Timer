package com.joaquito.cronometer

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.NumberPicker
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joaquito.cronometer.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private val partials = mutableListOf<PartialModel>()

    private lateinit var rvPartial: RecyclerView
    private lateinit var partialAdapter: PartialAdapter

    private lateinit var binding: ActivityMainBinding

    private var isRunning = false
    private var timerSecs: Int = 0
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            timerSecs++
            val hrs = timerSecs / 3600
            val min = (timerSecs % 3600) / 60
            val secs = timerSecs % 60

            val time = String.format("%02d:%02d:%02d", hrs, min, secs)
            binding.tvTimer.text = time
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        initUI()

    }

    private fun initUI() {
        rvPartial = findViewById(R.id.rvPartial)
        partialAdapter = PartialAdapter(partials)
        rvPartial.adapter = partialAdapter
        rvPartial.layoutManager = LinearLayoutManager(this)

    }




    private fun initListeners() {
        binding.btnStart.setOnClickListener { startTimer() }
        binding.btnStop.setOnClickListener { stopTimer() }
        binding.btnRestart.setOnClickListener { restartTimer() }
        binding.btnCountDown.setOnClickListener { countDown() }
        binding.btnPartial.setOnClickListener { takePartial() }
        binding.btnResetPartial.setOnClickListener { resetPartial() }
    }

    private fun resetPartial() {
        partials.clear()
        restartTimer()
        rvPartial.isVisible = false
        binding.btnResetPartial.isVisible = false
    }

    private fun startTimer() {
        if (!isRunning){
            handler.postDelayed(runnable, 1000)
            isRunning = true
            binding.btnStart.isEnabled = false
            binding.btnStop.isEnabled = true
            binding.btnRestart.isEnabled = true
            binding.btnCountDown.isEnabled = false
            binding.btnPartial.isEnabled = true
        }
    }

    private fun stopTimer(){
        if (isRunning){
            handler.removeCallbacks(runnable)
            isRunning = false
            binding.btnStart.isEnabled = true
            binding.btnStart.text = "RESUME"
            binding.btnStop.isEnabled = false
            binding.btnRestart.isEnabled = true
        }
    }

    private fun restartTimer(){
        handler.removeCallbacks(runnable)
        timerSecs = 0
        isRunning = false
        binding.tvTimer.text = "00:00:00"
        binding.btnStart.isEnabled = true
        binding.btnStop.isEnabled = false
        binding.btnRestart.isEnabled = false
        binding.btnStart.text = "START"
        binding.btnCountDown.isEnabled = true

    }

    private fun countDown(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_countdown)
        val npHours: NumberPicker = dialog.findViewById(R.id.npHours)
        val npMins: NumberPicker = dialog.findViewById(R.id.npMins)
        val npSecs: NumberPicker = dialog.findViewById(R.id.npSecs)
        val btnStartCountDown: Button = dialog.findViewById(R.id.btnStartCountDown)
        val btnStopCountDown: Button = findViewById(R.id.btnStopCountDown)
        val btnCancelCountDown: Button = findViewById(R.id.btnCancelCountDown)

        npHours.minValue = 0
        npHours.maxValue = 23
        npMins.minValue = 0
        npMins.maxValue = 59
        npSecs.minValue = 0
        npSecs.maxValue = 59

        btnStartCountDown.setOnClickListener {
            var npHour = npHours.value * 3600
            var npMin = npMins.value * 60
            var npSec = npSecs.value
            var totalSecs = npHour + npMin + npSec
            timerSecs = totalSecs
            val countDown = object : Runnable{
                override fun run() {
                    timerSecs--
                    val hrs = timerSecs / 3600
                    val min = (timerSecs % 3600) / 60
                    val secs = timerSecs % 60

                    val time = String.format("%02d:%02d:%02d", hrs, min, secs)
                    binding.tvTimer.text = time
                    handler.postDelayed(this, 1000)
                }

            }
            dialog.hide()
            handler.postDelayed(countDown, 1000)
            if (timerSecs == 0){
                handler.removeCallbacks(countDown)
                binding.tvTimer.text = "00:00:00"
                btnCancelCountDown.isVisible = false
                btnStopCountDown.isVisible = false
                binding.btnResumeCountDown.isVisible = false
                binding.btnCountDown.isVisible = true
                binding.btnCountDown.isEnabled = true
            }
            isRunning = true
            binding.btnStart.isEnabled = false
            btnStopCountDown.isVisible = true
            btnCancelCountDown.isVisible = true
            binding.btnCountDown.isVisible = false
            binding.btnResumeCountDown.isVisible = true
            binding.btnResumeCountDown.isEnabled = false

            binding.btnResumeCountDown.setOnClickListener {
                if (!isRunning){
                    handler.postDelayed(countDown, 1000)
                    isRunning = true
                    binding.btnResumeCountDown.isVisible = true
                    binding.btnResumeCountDown.isEnabled = false
                    btnStopCountDown.isEnabled = true
            }
            }

            btnStopCountDown.setOnClickListener {
                if (isRunning){
                    handler.removeCallbacks(countDown)
                    isRunning = false
                    binding.btnResumeCountDown.isEnabled = true
                    binding.btnStopCountDown.isEnabled = true
                    binding.btnCountDown.isEnabled = false
                    binding.btnStopCountDown.isEnabled = false
                }
            }

            btnCancelCountDown.setOnClickListener {
                    handler.removeCallbacks(countDown)
                    timerSecs = 0
                    binding.tvTimer.text = "00:00:00"
                    binding.btnStart.isEnabled = true
                    binding.btnStop.isEnabled = false
                    binding.btnRestart.isEnabled = false
                    btnStopCountDown.isVisible = false
                    btnCancelCountDown.isVisible = false
                    binding.btnCountDown.isVisible = true
                    binding.btnResumeCountDown.isVisible = false
                    binding.btnCountDown.isEnabled = true

                    }
            }

        dialog.show()
        }

    private fun takePartial(){
        val hrs = timerSecs / 3600
        val min = (timerSecs % 3600) / 60
        val secs = timerSecs % 60

        val time = String.format("%02d:%02d:%02d", hrs, min, secs)
        if (isRunning){
            partials.add(PartialModel(time))
            rvPartial.isVisible = true
            partialAdapter.notifyDataSetChanged()
            binding.btnResetPartial.isVisible = true
        }


    }

}
