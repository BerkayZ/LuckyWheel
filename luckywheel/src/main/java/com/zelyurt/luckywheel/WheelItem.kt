package com.zelyurt.luckywheel

class WheelItem {
    var color:Int = 0
    lateinit var text:String
    constructor(color:Int) {
        this.color = color
    }
    constructor(color:Int, text:String) {
        this.color = color
        this.text = text
    }
}