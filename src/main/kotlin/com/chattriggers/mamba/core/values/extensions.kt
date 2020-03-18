package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.values.numbers.VFalse
import com.chattriggers.mamba.core.values.numbers.VTrue

fun Boolean.toValue() = when (this) {
    true -> VTrue
    false -> VFalse
}
