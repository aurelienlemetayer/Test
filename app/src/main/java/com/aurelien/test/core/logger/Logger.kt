package com.aurelien.test.core.logger

import android.util.Log
import com.aurelien.test.BuildConfig

object Logger {
    private val LEVEL = if (BuildConfig.DEBUG) Level.DEBUG else Level.WARNING

    /**
     * Log a message with Android default logger
     *
     * @param level:        The level of the message
     * @param tag:          The tag to use
     * @param message:      The message to display
     */
    @JvmStatic
    fun log(
        level: Level,
        tag: String,
        message: String
    ) {

        if (level.ordinal >= LEVEL.ordinal) {

            when (level) {
                Level.VERBOSE -> Log.v(tag, message)
                Level.DEBUG -> Log.d(tag, message)
                Level.INFO -> Log.i(tag, message)
                Level.WARNING -> Log.w(tag, message)
                Level.ERROR -> Log.e(tag, message)
            }

            /**
             * In a real project I will send the logs on log service (Datadog...)
             *
             * if (!BuildConfig.DEBUG && level.ordinal >= Level.WARNING.ordinal) {
             *   // Send logs
             * }
             */
        }
    }

    enum class Level {
        VERBOSE,
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }
}
