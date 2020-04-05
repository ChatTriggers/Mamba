package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.collections.VDictType
import com.chattriggers.mamba.core.values.collections.VListType
import com.chattriggers.mamba.core.values.collections.VRangeType
import com.chattriggers.mamba.core.values.exceptions.*
import com.chattriggers.mamba.core.values.numbers.VComplexType
import com.chattriggers.mamba.core.values.numbers.VFloatType
import com.chattriggers.mamba.core.values.numbers.VIntType
import com.chattriggers.mamba.core.values.singletons.*

object GlobalScope : VObject(LazyValue("GlobalScopeType") { GlobalScopeType })

object GlobalScopeType : VType() {
    init {
        addFieldDescriptor("True", VTrue)
        addFieldDescriptor("False", VFalse)
        addFieldDescriptor("None", VNone)
        addFieldDescriptor("...", VEllipsis)

        addFieldDescriptor("object", VObjectType)
        addFieldDescriptor("type", VTypeType)
        addFieldDescriptor("complex", VComplexType)
        addFieldDescriptor("float", VFloatType)
        addFieldDescriptor("int", VIntType)
        addFieldDescriptor("bool", VBoolType)
        addFieldDescriptor("str", VStringType)
        addFieldDescriptor("list", VListType)
        addFieldDescriptor("dict", VDictType)
        addFieldDescriptor("range", VRangeType)

        addFieldDescriptor("ArithmeticError", VArithmeticErrorType)
        addFieldDescriptor("BaseException", VBaseExceptionType)
        addFieldDescriptor("Exception", VExceptionType)
        addFieldDescriptor("LookupError", VLookupErrorType)
        addFieldDescriptor("NameError", VNameErrorType)
        addFieldDescriptor("NotImplementedError", VNotImplementedErrorType)
        addFieldDescriptor("StopIteration", VStopIterationType)
        addFieldDescriptor("SyntaxError", VSyntaxErrorType)
        addFieldDescriptor("TypeError", VTypeErrorType)
        addFieldDescriptor("ValueError", VValueErrorType)

        addStaticMethodDescriptor("abs") {
            assertArgAs<VObject>(0).callProperty(interp, "__abs__")
        }

        addStaticMethodDescriptor("dir") {
            runtime.dir(arguments())
        }

        addStaticMethodDescriptor("print") {
            println(arguments().joinToString(separator = " "))
            VNone
        }
    }
}
