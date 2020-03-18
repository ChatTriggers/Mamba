package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.values.VType

class VNativeMethod(name: String, val owner: VType, function: VNativeFuncType) : VNativeFunction(name, function)
