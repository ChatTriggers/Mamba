package com.chattriggers.mamba.core.modules

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VModule
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.numbers.VInt
import com.chattriggers.mamba.core.values.numbers.toValue

object ModuleMath : VModule("math", LazyValue("ModuleMathType") { ModuleMathType }) 

object ModuleMathType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("factorial", isStatic = true) {
            val x = assertArgAs<VInt>(0).int
            var f = 1
            for (i in 1..x) {
                f *= i
            }
            f.toValue()
        }
    }
}
