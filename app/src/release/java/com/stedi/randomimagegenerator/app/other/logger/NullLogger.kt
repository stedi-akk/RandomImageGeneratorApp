package com.stedi.randomimagegenerator.app.other.logger

class NullLogger : Logger {
    override fun log(from: Any, message: String?) {
    }

    override fun log(from: Any, t: Throwable?) {
    }

    override fun log(from: Any, message: String?, t: Throwable?) {
    }
}