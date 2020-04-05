package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.collections.VTuple

class VNameError(args: VTuple) : VException(args) {
    override val className = "NameError"

    constructor() : this(VTuple())
}

object VNameErrorType : VType(LazyValue("VExceptionType") { VExceptionType })