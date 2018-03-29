package com.stedi.randomimagegenerator.app.other.logger

interface Logger {
    fun log(from: Any, message: String?)

    fun log(from: Any, t: Throwable?)

    fun log(from: Any, message: String?, t: Throwable?)
}