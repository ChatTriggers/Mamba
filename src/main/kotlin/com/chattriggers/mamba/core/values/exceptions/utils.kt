package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.toValue

fun notImplemented(msg: String = ""): Nothing = throw MambaException(VNotImplementedError(VTuple(msg.toValue())))
