package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.collections.VDictType
import com.chattriggers.mamba.core.values.collections.VListType
import com.chattriggers.mamba.core.values.collections.VRangeType
import com.chattriggers.mamba.core.values.exceptions.*
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.base.VTypeType
import com.chattriggers.mamba.core.values.numbers.*
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
            runtime.callProp(argument(0), "__abs__")
        }

        addMethod("all", isStatic = true) {
            val iterable = assertArgAs<VObject>(0)
            val returnList = runtime.iterableToList(iterable)

            if (returnList is VExceptionWrapper || returnList is VBaseException) {
                returnList as VObject
            } else {
                @Suppress("UNCHECKED_CAST")
                val list = (returnList as Wrapper).value as List<VObject>

                if (list.all { runtime.toBoolean(it) }) VTrue else VFalse
            }
        }

        addMethod("any", isStatic = true) {
            val iterable = assertArgAs<VObject>(0)
            val returnList = runtime.iterableToList(iterable)

            if (returnList is VExceptionWrapper || returnList is VBaseException) {
                returnList as VObject
            } else {
                @Suppress("UNCHECKED_CAST")
                val list = (returnList as Wrapper).value as List<VObject>

                if (list.any { runtime.toBoolean(it) }) VTrue else VFalse
            }
        }

        addMethod("callable", isStatic = true) {
            val obj = assertArgAs<VObject>(0)

            obj.slotMap.contains(Wrapper("__call__")).toValue()
        }

        addMethod("chr", isStatic = true) {
            val int = assertArgAs<VInt>(0).int

            runtime.construct(VStringType, listOf(int.toChar().toString()))
        }

        addMethod("dir", isStatic = true) {
            runtime.callProp(argument(0), "__dir__")
        }

        addMethod("id", isStatic = true) {
            assertArgAs<VObject>(0).id.toValue()
        }

        addMethod("print", isStatic = true) {
            println(arguments().joinToString(separator = " "))
            VNone
        }
    }
}
