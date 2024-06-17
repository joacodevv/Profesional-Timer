package com.joaquito.cronometer.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.joaquito.cronometer.R
import com.joaquito.cronometer.databinding.FragmentStopWatchBinding

class StopWatchFragment : Fragment() {

    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        running()
    }

    private fun running() {
        if (isRunning){
            binding.btnStop.isEnabled = true
            binding.btnRestart.isEnabled = true
            binding.btnStart.isEnabled = false
        }
    }

    private fun initListeners() {
        binding.btnStart.setOnClickListener { startTimer() }
        binding.btnStop.setOnClickListener { stopTimer() }
        binding.btnRestart.setOnClickListener { restartTimer() }
    }

    private fun restartTimer() {
        handler.removeCallbacks(runnable)
        timerSecs = 0
        isRunning = false
        binding.tvTimer.text = "00:00:00"
        binding.btnStart.isEnabled = true
        binding.btnStop.isEnabled = false
        binding.btnRestart.isEnabled = false
        binding.btnStart.text = "START"

    }

    private fun stopTimer() {
        if (isRunning){
            handler.removeCallbacks(runnable)
            isRunning = false
            binding.btnStart.isEnabled = true
            binding.btnStart.text = "RESUME"
            binding.btnStop.isEnabled = false
            binding.btnRestart.isEnabled = true
        }
    }

    private fun startTimer() {
        if (!isRunning){
            handler.postDelayed(runnable, 1000)
            isRunning = true
            binding.btnStart.isEnabled = false
            binding.btnStop.isEnabled = true
            binding.btnRestart.isEnabled = true
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStopWatchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}