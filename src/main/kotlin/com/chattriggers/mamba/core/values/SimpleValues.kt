package com.chattriggers.mamba.core.values

object VTrue : VObject() {
    override fun toString() = "True"
}

object VFalse : VObject() {
    override fun toString() = "False"
}

object VNone : VObject() {
    override fun toString() = "None"
}

object VEllipsis : VObject() {
    override fun toString() = "Ellipsis"
}
