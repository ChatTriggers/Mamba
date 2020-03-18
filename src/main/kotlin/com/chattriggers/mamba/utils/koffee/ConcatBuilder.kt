package com.chattriggers.mamba.utils.koffee

import codes.som.anthony.koffee.MethodAssembly
import codes.som.anthony.koffee.insns.InstructionAssembly
import codes.som.anthony.koffee.insns.jvm.dup
import codes.som.anthony.koffee.insns.jvm.invokevirtual
import codes.som.anthony.koffee.insns.jvm.pop
import codes.som.anthony.koffee.insns.sugar.construct
import codes.som.anthony.koffee.types.*


class ConcatBuilder(assembly: MethodAssembly) : InstructionAssembly by assembly {
    val ConcatBuilder.iappend: Unit
        get() {
            invokevirtual(StringBuilder::class, "append", void, int)
            dup
        }

    val ConcatBuilder.cappend: Unit
        get() {
            invokevirtual(StringBuilder::class, "append", void, char)
            dup
        }

    val ConcatBuilder.fappend: Unit
        get() {
            invokevirtual(StringBuilder::class, "append", void, float)
            dup
        }

    val ConcatBuilder.dappend: Unit
        get() {
            invokevirtual(StringBuilder::class, "append", void, double)
            dup
        }

    val ConcatBuilder.lappend: Unit
        get() {
            invokevirtual(StringBuilder::class, "append", void, long)
            dup
        }

    val ConcatBuilder.bappend: Unit
        get() {
            invokevirtual(StringBuilder::class, "append", void, boolean)
            dup
        }

    val ConcatBuilder.oappend: Unit
        get() {
            invokevirtual(StringBuilder::class, "append", void, Any::class)
            dup
        }

    val ConcatBuilder.sappend: Unit
        get() {
            invokevirtual(StringBuilder::class, "append", void, String::class)
            dup
        }

    companion object {
        fun MethodAssembly.concatBuilder(body: ConcatBuilder.() -> Unit) {
            construct(StringBuilder::class)
            dup

            ConcatBuilder(this).body()
            pop

            invokevirtual(StringBuilder::class, "toString", String::class)
        }
    }
}