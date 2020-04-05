package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.collections.VTuple

class VStopIteration(args: VTuple) : VException(args) {
    override val className = "StopIteration"

    constructor() : this(VTuple())
}

object VStopIterationType : VType(LazyValue("VExceptionType") { VExceptionType })