package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.ir.nodes.FunctionNode

open class VMethod(
    val owner: VObject,
    name: String,
    functionNode: FunctionNode
) : VFunction(name, functionNode)
