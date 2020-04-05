package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.collections.VTuple

class VLookupError(args: VTuple) : VException(args) {
    override val className = "LookupError"

    constructor() : this(VTuple())
}

object VLookupErrorType : VType(LazyValue("VExceptionType") { VExceptionType })