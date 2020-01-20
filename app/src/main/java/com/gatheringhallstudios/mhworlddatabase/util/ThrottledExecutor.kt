package com.gatheringhallstudios.mhworlddatabase.util

import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * A threaded serial executor that skips in-between unstarted jobs.
 * If job A is executing, and jobs B and C are added, then job C will be executed after A and B will be skipped.
 */
class ThrottledExecutor : Executor {
    private val lock = ReentrantReadWriteLock()

    // the current running executable.
    private var activeRunnable: Runnable? = null

    // the next-to-run. Set as an atomic reference to allow getting + setting simultaneously
    private var nextRunnable = AtomicReference<Runnable?>()

    /**
     * Adds a job to the executor. If no job is running, it runs immediately.
     * If a job is running, it will be set as the "next-to-run". Adding additional
     * jobs will replace the "next-to-run".
     */
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

    /**
     * Internal helper to run the next-to-run
     */
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