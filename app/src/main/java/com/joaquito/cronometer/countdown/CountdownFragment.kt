package com.joaquito.cronometer.countdown

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import com.joaquito.cronometer.R
import com.joaquito.cronometer.databinding.FragmentCountdownBinding
import java.text.DecimalFormat
import kotlin.math.roundToInt


class CountdownFragment : Fragment() {

    private var _binding: FragmentCountdownBinding? = null
    private val binding get() = _binding!!

    private lateinit var customCountdownTimer: CustomCountdownTimer

    private val countdownTime = 30
    private val clockTime = (countdownTime * 1000).toLong()
    private val progressTime = (clockTime / 1000).toFloat()

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            onBackPressedMethod()
        }

    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)


        var secondsLeft = 0
        customCountdownTimer = object : CustomCountdownTimer(clockTime, 1000){

        }
        customCountdownTimer.onTick = {

            val second = (it / 1000.0f).roundToInt()
            if ( second != secondsLeft){
                secondsLeft = second

                timerFormat(secondsLeft, binding.tvTime)
            }
        }
        customCountdownTimer.onFinish = {
            timerFormat(0, binding.tvTime)
        }
        binding.circularProgressBar.max = progressTime.toInt()
        binding.circularProgressBar.progress = progressTime.toInt()

        customCountdownTimer.startTimer()

        binding.btnPause.setOnClickListener {
            customCountdownTimer.pauseTimer()
        }
        binding.btnReset.setOnClickListener {
            customCountdownTimer.restartTimer()
            binding.circularProgressBar.progress = progressTime.toInt()
        }
        binding.btnResume.setOnClickListener {
            customCountdownTimer.resumeTimer()
        }
    }

    private fun timerFormat(secondsLeft: Int, tvTime: TextView) {
        binding.circularProgressBar.progress = secondsLeft
        val decimalFormat = DecimalFormat("00")
        val hour = secondsLeft / 3600
        val min = (secondsLeft % 3600) / 60
        val secs = secondsLeft % 60

        val timeFormat1 = decimalFormat.format(secondsLeft)
        val timeFormat2 = decimalFormat.format(min) + ":" + decimalFormat.format(secs)
        val timeFormat3 = decimalFormat.format(hour) + ":" + decimalFormat.format(min) + ":" + decimalFormat.format(secs)

        binding.tvTime.text = timeFormat1 + "\n" + timeFormat2 + "\n" + timeFormat3
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCountdownBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun onBackPressedMethod() {
        customCountdownTimer.destroyTimer()
        //finish()
    }

    override fun onPause() {
        customCountdownTimer.pauseTimer()
        super.onPause()
    }

    override fun onResume() {
        customCountdownTimer.resumeTimer()
        super.onResume()
    }

    override fun onDestroy() {
        customCountdownTimer.destroyTimer()
        super.onDestroy()
    }

}