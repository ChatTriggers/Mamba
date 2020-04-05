package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.collections.VTuple

class VTypeError(args: VTuple) : VException(args) {
    override val className = "TypeError"

    constructor() : this(VTuple())
}

object VTypeErrorType : VType(LazyValue("VExceptionType") { VExceptionType })