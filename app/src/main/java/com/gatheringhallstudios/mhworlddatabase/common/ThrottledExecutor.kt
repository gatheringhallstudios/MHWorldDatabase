package com.gatheringhallstudios.mhworlddatabase.common

import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class ThrottledExecutor : Executor {
    private val lock = ReentrantReadWriteLock()

    private var activeRunnable: Runnable? = null
    private var nextRunnable = AtomicReference<Runnable?>()

    override fun execute(command: Runnable?) {
        nextRunnable.set(Runnable {
            try {
                command?.run()
            } finally {
                // clear active runnable (we're done)
                lock.write {
                    activeRunnable = null
                    scheduleNext()
                }
            }
        })

        scheduleNext()
    }

    private fun scheduleNext() {
        lock.read {
            if (activeRunnable == null) {
                lock.write {
                    activeRunnable = nextRunnable.getAndSet(null)

                    Thread(activeRunnable).start()
                }
            }
        }
    }
}