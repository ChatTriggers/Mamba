package com.chattriggers.mamba.core.values

/**
 * Represents a value that exists within the runtime.
 *
 * May of course be exposed to user scripts, however
 * classes that inherit from this class may not be
 * created by the user.
 */
abstract class VNative : Value()