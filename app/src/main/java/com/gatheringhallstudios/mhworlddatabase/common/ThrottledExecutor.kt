package com.gatheringhallstudios.mhworlddatabase.common

import java.util.concurrent.Executor
import kotlin.concurrent.thread

class ThrottledExecutor : Executor {
    // note: java locks are re-entrant
    private val lock = Object()

    private var activeRunnable: Runnable? = null
    private var nextRunnable: Runnable? = null

    override fun execute(command: Runnable?) {
        synchronized(lock) {
            // always update the "next" runnable
            nextRunnable = Runnable {
                try {
                    command?.run()
                } finally {
                    // clears active runnable (we're done)
                    activeRunnable = null

                    scheduleNext()
                }
            }

            scheduleNext()
        }
    }

    private fun scheduleNext() {
        synchronized(lock) {
            if (activeRunnable == null) {
                activeRunnable = nextRunnable
                nextRunnable = null

                Thread(activeRunnable).start()
            }
        }
    }
}