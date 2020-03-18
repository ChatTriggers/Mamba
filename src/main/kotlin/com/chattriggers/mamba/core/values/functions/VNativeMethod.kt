package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.values.VObject

class VNativeMethod(name: String, val owner: VObject, function: VNativeFuncType) : VNativeFunction(name, function)
