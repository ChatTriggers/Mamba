package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.modules.ModuleMath
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType

open class VModule(val name: String, val type: LazyValue<VType>) : VObject(type) {
    override fun toString() = "<module '$name' (built-in)>"

    companion object {
        fun getNativeModules(): List<VModule> {
            return listOf(
                ModuleMath
            )
        }
    }
}

object VModuleType : VType(LazyValue("VObjectType") { VObjectType })