package com.stedi.randomimagegenerator.app.other.logger

class SoutLogger(private val tag: String) : Logger {

    override fun log(from: Any, message: String?) {
        println(tag + ": " + from.javaClass.simpleName + ": " + message)
    }

    override fun log(from: Any, t: Throwable?) {
        println(tag + ": " + from.javaClass.simpleName + ": Throwable: ")
        t?.printStackTrace()
    }

    override fun log(from: Any, message: String?, t: Throwable?) {
        println(tag + ": " + from.javaClass.simpleName + ": " + message + "; Throwable: ")
        t?.printStackTrace()
    }
}