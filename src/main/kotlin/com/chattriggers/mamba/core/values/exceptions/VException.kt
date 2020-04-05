package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.collections.VTuple

open class VException(args: VTuple) : VBaseException(args) {
    override val className = "Exception"

    constructor() : this(VTuple())
}

object VExceptionType : VType(LazyValue("VBaseExceptionType") { VBaseExceptionType })