package com.chattriggers.mamba.core.values

fun Boolean.toValue() = when (this) {
    true -> VTrue
    false -> VFalse
}
