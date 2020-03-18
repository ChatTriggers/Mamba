package com.chattriggers.mamba.api

interface Operator {
    val symbol: String
}

enum class ArithmeticOperator(override val symbol: String) : Operator {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    INT_DIVIDE("//"),
    MODULUS("%"),
    POWER("**"),

    // TODO: BitwiseOperator?
    BIT_AND("&"),
    BIT_XOR("^"),
    BIT_OR("|"),
    SHIFT_LEFT("<<"),
    SHIFT_RIGHT(">>");

    companion object {
        fun from(operator: String) = values().first { operator == it.symbol }
    }
}

enum class UnaryOperator(override val symbol: String) : Operator {
    NEG("-"),
    POS("+"),
    INVERT("~");

    companion object {
        fun from(operator: String) = values().first { operator == it.symbol }
    }
}

enum class ComparisonOperator(override val symbol: String) : Operator {
    LT("<"),
    GT(">"),
    EQ("=="),
    GTE(">="),
    LTE("<="),
    DIAMOND("<>"),
    NOT_EQ("!="),
    IN("in"),
    NOT_IN("not in"),
    IS("is"),
    IS_NOT("is not");

    companion object {
        fun from(operator: String) = values().first { operator == it.symbol }
    }
}