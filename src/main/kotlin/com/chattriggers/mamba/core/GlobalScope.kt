package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.collections.VDictType
import com.chattriggers.mamba.core.values.collections.VListType
import com.chattriggers.mamba.core.values.collections.VRangeType
import com.chattriggers.mamba.core.values.exceptions.*
import com.chattriggers.mamba.core.values.numbers.VComplexType
import com.chattriggers.mamba.core.values.numbers.VFloatType
import com.chattriggers.mamba.core.values.numbers.VIntType
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.base.VTypeType
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.*

object GlobalScope : VObject(LazyValue("GlobalScopeType") { GlobalScopeType })

object GlobalScopeType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addField("True") { VTrue }
        addField("False") { VFalse }
        addField("None") { VNone }
        addField("...") { VEllipsis }

        addField("object") { VObjectType }
        addField("type") { VTypeType }
        addField("complex") { VComplexType }
        addField("float") { VFloatType }
        addField("int") { VIntType }
        addField("bool") { VBoolType }
        addField("str") { VStringType }
        addField("list") { VListType }
        addField("dict") { VDictType }
        addField("range") { VRangeType }

        addField("ArithmeticError") { VArithmeticErrorType }
        addField("BaseException") { VBaseExceptionType }
        addField("Exception") { VExceptionType }
        addField("LookupError") { VLookupErrorType }
        addField("NameError") { VNameErrorType }
        addField("NotImplementedError") { VNotImplementedErrorType }
        addField("StopIteration") { VStopIterationType }
        addField("SyntaxError") { VSyntaxErrorType }
        addField("TypeError") { VTypeErrorType }
        addField("ValueError") { VValueErrorType }

        addMethod("abs", isStatic = true) {
            runtime.callProperty(argument(0), "__abs__")
        }

        addMethod("all", isStatic = true) {
            val iterable = assertArgAs<VObject>(0)
            val returnList = runtime.iterableToList(iterable)

            if (returnList is VExceptionWrapper) {
                returnList
            } else {
                @Suppress("UNCHECKED_CAST")
                val list = (returnList as Wrapper).value as List<VObject>

                if (list.all { runtime.toBoolean(it) }) VTrue else VFalse
            }
        }

        addMethod("dir", isStatic = true) {
            runtime.callProperty(argument(0), "__dir__")
        }

        addMethod("print", isStatic = true) {
            println(arguments().joinToString(separator = " "))
            VNone
        }
    }
}
