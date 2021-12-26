package com.app.mapsample.domain.base

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

/**
 * Built on the same lines as BaseUseCase by https://github.com/rmoustafa/Kotlin-Coroutines-Clean-Architecture
 */

typealias SimpleResult = Result<Any, Error>

abstract class BaseUseCase<in Params> : CoroutineScope {

    private val parentJob = SupervisorJob()
    private val mainDispatcher = Dispatchers.Main
    private val backgroundDispatcher = Dispatchers.Default
    protected val resultChannel = Channel<SimpleResult>()

    val receiveChannel: ReceiveChannel<SimpleResult> = resultChannel

    override val coroutineContext: CoroutineContext
        get() = parentJob + mainDispatcher


    protected abstract suspend fun run(params: Params?)

    /**
     * By overriding invoke, we allow use cases to be called as "invoking"
     */
     fun invoke(params: Params?) {
        launch {
            withContext(backgroundDispatcher) {
                run(params)
            }
        }
    }

    protected fun <T> startAsync(block: suspend () -> T): Deferred<T> = async(parentJob) {
        block()
    }

    /**
     * Should be called when use-case owner is destroyed
     * This will ensure coroutine is cancelled if it's still running some tasks
     */
    fun clear() {
        resultChannel.close()
        parentJob.cancel()
    }
}
