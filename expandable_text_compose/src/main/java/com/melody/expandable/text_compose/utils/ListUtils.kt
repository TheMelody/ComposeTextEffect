package com.melody.expandable.text_compose.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * ListUtils
 * @author 被风吹过的夏天
 * @email developer_melody@163.com
 * @github: https://github.com/TheMelody/OmniMap
 * created 2023/05/24 17:11
 */
@Suppress("BanInlineOptIn")
@OptIn(ExperimentalContracts::class)
internal inline fun <T> List<T>.fastAny(predicate: (T) -> Boolean): Boolean {
    contract { callsInPlace(predicate) }
    fastForEach { if (predicate(it)) return true }
    return false
}
@Suppress("BanInlineOptIn")
@OptIn(ExperimentalContracts::class)
internal inline fun <T> List<T>.fastForEach(action: (T) -> Unit) {
    contract { callsInPlace(action) }
    for (index in indices) {
        val item = get(index)
        action(item)
    }
}