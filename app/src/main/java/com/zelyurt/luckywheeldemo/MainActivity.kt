package com.zelyurt.luckywheeldemo

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.zelyurt.luckywheel.LuckyWheel
import com.zelyurt.luckywheel.OnLuckyWheelReachTheTarget
import com.zelyurt.luckywheel.WheelItem


class MainActivity : AppCompatActivity() {
    private lateinit var spinBtn: Button
    private lateinit var lwheel: LuckyWheel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wheelItems: MutableList<WheelItem> = ArrayList()
        lwheel = findViewById(R.id.lwheel)
        spinBtn = findViewById(R.id.spinBtn)

        wheelItems.add(WheelItem(ContextCompat.getColor(this, R.color.dashRed), "Long long logn text"))
        wheelItems.add(WheelItem(ContextCompat.getColor(this, R.color.dashBlue), "text 2"))
        wheelItems.add(WheelItem(ContextCompat.getColor(this, R.color.dashGreen), "text 3"))
        wheelItems.add(WheelItem(ContextCompat.getColor(this, R.color.dashPeach), "text 4"))
        wheelItems.add(WheelItem(ContextCompat.getColor(this, R.color.dashYellow), "text 5"))

        lwheel.setTarget(3)
        lwheel.addWheelItems(wheelItems)

        lwheel.setLuckyWheelReachTheTarget(object : OnLuckyWheelReachTheTarget {
            override fun onReachTarget() {
                Toast.makeText(this@MainActivity, "Target Reached", Toast.LENGTH_LONG).show()
            }
        })
        spinBtn.setOnClickListener(){
            lwheel.rotateToRandom()
        }
    }
}