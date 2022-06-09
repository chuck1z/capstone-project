package com.darkshandev.freshcam.utils

import java.nio.FloatBuffer

fun FloatBuffer.getIndexOfMax(): Int {
    var indexOfMax = 0
    for (i in 0 until this.capacity()) {
        val current = this.get(i)
        if (current > this.get(indexOfMax)) {
            indexOfMax = i
        }
    }
    return indexOfMax
}
fun FloatBuffer.getMax(): Float {
    var indexOfMax = 0
    for (i in 0 until this.capacity()) {
        val current = this.get(i)
        if (current > this.get(indexOfMax)) {
            indexOfMax = i
        }
    }
    return this.get(indexOfMax)
}
fun Float.asPercentage(): String {
    return String.format("%.2f", this)+"%"

}
