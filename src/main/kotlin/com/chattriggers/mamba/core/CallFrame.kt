package com.chattriggers.mamba.core

data class CallFrame(
    val file: String,
    val source: String,
    val lineNumber: Int
)