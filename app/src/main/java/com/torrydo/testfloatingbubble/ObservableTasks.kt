package com.torrydo.testfloatingbubble

interface EventTask {
    fun doEventTask(bool: Boolean)
}

class EventTaskImpl: EventTask{
    override fun doEventTask(bool: Boolean) {
        println("do task number 1")
    }
}

val tasks = mutableListOf<EventTask>()

fun main() {

    tasks.add(object: EventTask{
        override fun doEventTask(bool: Boolean){
            println("do task number 1")
        }
    })

    val taskSize = tasks[0].hashCode()

    println("task size = ${taskSize}")

}