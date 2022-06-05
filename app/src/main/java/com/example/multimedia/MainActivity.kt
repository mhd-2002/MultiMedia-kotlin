package com.example.multimedia

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.multimedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start the media player
        binding.playBtn.setOnClickListener{
            if(pause){
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause = false
                Toast.makeText(this,"media playing",Toast.LENGTH_SHORT).show()
            }else{

                mediaPlayer = MediaPlayer.create(applicationContext,R.raw.dragon)
                mediaPlayer.start()
                Toast.makeText(this,"media playing",Toast.LENGTH_SHORT).show()

            }
            initializeSeekBar()
            binding.playBtn.isEnabled = false
            binding.pauseBtn.isEnabled = true
            binding.stopBtn.isEnabled = true

            mediaPlayer.setOnCompletionListener {
                binding.playBtn.isEnabled = true
                binding.pauseBtn.isEnabled = false
                binding.stopBtn.isEnabled = false
                Toast.makeText(this,"end",Toast.LENGTH_SHORT).show()
            }
        }
        // Pause the media player
        binding.pauseBtn.setOnClickListener {
            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
                pause = true
                binding.playBtn.isEnabled = true
                binding.pauseBtn.isEnabled = false
                binding.stopBtn.isEnabled = true
                Toast.makeText(this,"media pause",Toast.LENGTH_SHORT).show()
            }
        }
        // Stop the media player
        binding.stopBtn.setOnClickListener{
            if(mediaPlayer.isPlaying || pause.equals(true)){
                pause = false
                binding.seekBar.progress = 0
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
                handler.removeCallbacks(runnable)

                binding.playBtn.isEnabled = true
                binding.pauseBtn.isEnabled = false
                binding.stopBtn.isEnabled = false
                binding.tvPass.text = ""
                binding.tvDue.text = ""
                Toast.makeText(this,"media stop",Toast.LENGTH_SHORT).show()
            }
        }
        // Seek bar change listener
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }
    // Method to initialize seek bar and audio stats
    private fun initializeSeekBar() {
        binding.seekBar.max = mediaPlayer.seconds

        runnable = Runnable {
            binding.seekBar.progress = mediaPlayer.currentSeconds

            binding.tvPass.text = "${mediaPlayer.currentSeconds} sec"
            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            binding.tvDue.text = "$diff sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}
// Creating an extension property to get the media player time duration in seconds
val MediaPlayer.seconds:Int
    get() {
        return this.duration / 1000
    }
// Creating an extension property to get media player current position in seconds
val MediaPlayer.currentSeconds:Int
    get() {
        return this.currentPosition/1000
    }