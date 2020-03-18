package com.chattriggers.mamba.api

import java.io.InputStream
import java.io.PrintStream

interface FeatureConfig {
    val stdout: PrintStream
    val stderr: PrintStream
    val stdin: InputStream
}