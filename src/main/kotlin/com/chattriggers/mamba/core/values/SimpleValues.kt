package com.chattriggers.mamba.core.values

object VNone : VObject() {
    override val className: String
        get() = "NoneType"

    override fun toString() = "None"
}

object VEllipsis : VObject() {
    override val className: String
        get() = "ellipsis"

    override fun toString() = "Ellipsis"
}

object VNotImplemented : VObject() {
    override val className: String
        get() = "NotImplementedType"

    override fun toString() = "NotImplemented"
}
