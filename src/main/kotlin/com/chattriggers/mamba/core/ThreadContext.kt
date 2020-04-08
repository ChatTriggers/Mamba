package com.chattriggers.mamba.core

class ThreadContext(val interp: Interpreter) {
    val runtime = Runtime(this)

    companion object {
        private val ctxThreadLocal = ThreadLocal<ThreadContext>()

        internal val currentContext: ThreadContext
            get() = ctxThreadLocal.get()

        fun enterContext(context: ThreadContext) {
            ctxThreadLocal.set(context)
        }

        fun exitContext() {
            if (ctxThreadLocal.get() == null) {
                throw IllegalStateException("Exiting context before entering")
            }
            ctxThreadLocal.set(null)
        }
    }
}