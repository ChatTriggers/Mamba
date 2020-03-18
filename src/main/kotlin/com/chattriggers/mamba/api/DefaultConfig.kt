package com.chattriggers.mamba.api

class DefaultConfig : FeatureConfig {
    override val stdout = System.out
    override val stderr = System.err
    override val stdin = System.`in`
}