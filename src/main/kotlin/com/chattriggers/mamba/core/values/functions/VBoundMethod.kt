package com.chattriggers.mamba.core.values.functions

import codes.som.anthony.koffee.modifiers.private
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.ir.nodes.FunctionNode

class VBoundMethod(
    owner: VType,
    name: String,
    functionNode: FunctionNode,
    private val self: Value
) : VMethod(owner, name, functionNode)
