package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.values.Value

/**
 * Base class for any callable object that can
 * exist in the Mamba runtime
 */
abstract class VCallable(override val name: String) : Value(), ICallable