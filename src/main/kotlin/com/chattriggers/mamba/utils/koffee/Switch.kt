package com.chattriggers.mamba.utils.koffee

import codes.som.anthony.koffee.MethodAssembly
import codes.som.anthony.koffee.insns.jvm.goto
import codes.som.anthony.koffee.insns.jvm.lookupswitch

typealias MethodAssemblyLambda = MethodAssembly.() -> Unit

class Switch {
    private val cases = mutableMapOf<Int, MethodAssemblyLambda>()
    private var defaultCase: MethodAssemblyLambda? = null

    fun case(value: Int, body: MethodAssemblyLambda) {
        if (cases.contains(value)) {
            throw IllegalArgumentException("Switch statement already contains case $value")
        }

        cases[value] = body
    }

    fun default(body: MethodAssemblyLambda) {
        if (defaultCase != null) {
            throw IllegalArgumentException("Switch statement already contains a default case")
        }

        defaultCase = body
    }

    companion object {
        const val CASE_PREFIX = "__$\$SWITCH_CASE_PREFIX_"
        const val DEFAULT_LABEL = "__$\$SWITCH_DEFAULT_LABEL"
        const val LAST_LABEL = "__$\$SWITCH_LAST_LABEL"

        fun MethodAssembly.switch(body: Switch.() -> Unit) {
            val switch = Switch().apply(body)

            val pairs = switch.cases.keys.map { it to L[CASE_PREFIX + it] }.toTypedArray()

            lookupswitch(L[DEFAULT_LABEL], *pairs)

            switch.cases.forEach { test, body ->
                +L[CASE_PREFIX + test]
                this.apply(body)
            }

            +L[DEFAULT_LABEL]

            val defaultCase = switch.defaultCase

            if (defaultCase != null) {
                this.apply(defaultCase)
            }

            +L[LAST_LABEL]
        }

        val MethodAssembly._break: Unit
            get() {
                goto(L[LAST_LABEL])
            }
    }
}


