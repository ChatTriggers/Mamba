package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.collections.VTuple

class VValueError(args: VTuple) : VException(args) {
    override val className = "ValueError"

    constructor() : this(VTuple())
}

object VValueErrorType : VType(LazyValue("VExceptionType") { VExceptionType })