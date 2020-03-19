package com.chattriggers.mamba.utils

open class ParentDeferredMap<K, V>(private val parentMap: MutableMap<K, V>? = null) : MutableMap<K, V> {
    private val map = mutableMapOf<K, V>()

    override val size: Int
        get() = map.size + (parentMap?.size ?: 0)

    override fun containsKey(key: K): Boolean {
        return map.containsKey(key) || (parentMap?.containsKey(key) ?: false)
    }

    override fun containsValue(value: V): Boolean {
        return map.containsValue(value) || (parentMap?.containsValue(value) ?: false)
    }

    override fun get(key: K): V? {
        return if (map.contains(key)) {
            map[key]
        } else {
            parentMap?.get(key)
        }
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty() && (parentMap?.isEmpty() ?: true)
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = if (parentMap == null) map.entries else (map.entries + parentMap.entries).toMutableSet()

    override val keys: MutableSet<K>
        get() = if (parentMap == null) map.keys else (map.keys + parentMap.keys).toMutableSet()

    override val values: MutableCollection<V>
        get() = if (parentMap == null) map.values else (map.values + parentMap.values).toMutableSet()

    override fun clear() {
        map.clear()
        parentMap?.clear()
    }

    override fun put(key: K, value: V): V? {
        return if (map.contains(key) || parentMap == null) {
            val v = map[key]
            map[key] = value
            v
        } else {
            val v = parentMap[key]
            parentMap[key] = value
            v
        }
    }

    override fun putAll(from: Map<out K, V>) {
        for ((key, value) in from) {
            if (map.contains(key) || parentMap == null) {
                map[key] = value
            } else {
                parentMap[key] = value
            }
        }
    }

    override fun remove(key: K): V? {
        return if (map.contains(key) || parentMap == null) {
            map.remove(key)
        } else {
            parentMap.remove(key)
        }
    }
}