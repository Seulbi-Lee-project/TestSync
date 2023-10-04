package com.example.testsync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testsync.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.syncButton.setOnClickListener {
            val startTime = System.currentTimeMillis()
            for(i in 1..6){
                synchronousTask(i)
            }
            val endTime = System.currentTimeMillis()
            Log.d("time", "${(endTime-startTime)/1000}초 경과")
        }

        binding.asyncButton.setOnClickListener {
            GlobalScope.launch {
                val startTime = System.currentTimeMillis()
                synchronousTask(1)
                val firstTime = System.currentTimeMillis()
                val deferred = listOf(
                    async { synchronousTask(2) },
                    async { synchronousTask(3) },
                    async { synchronousTask(4) },
                    async { synchronousTask(5) }
                )
                deferred.awaitAll()
                synchronousTask(6)
                val endTime = System.currentTimeMillis()
                Log.d("time", "${(endTime-startTime)/1000}초 경과, firstTime : ${(firstTime-startTime)/1000}초")

            }

        }
    }

    fun synchronousTask(num:Int): String {
        val time = num*1000
        Thread.sleep(time.toLong())
        return "$num 번 작업 완료"
    }
}