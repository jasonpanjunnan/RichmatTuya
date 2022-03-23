package com.richmat.mytuya.util

internal fun <E> MutableSet<E>.addOrRemove(element: E): MutableSet<E> {
    if (!add(element)) {
        remove(element)
    }
    return this
}

internal fun <E> MutableList<E>.addOrRemove(element: E): MutableList<E> {
    if (!add(element)) {
        remove(element)
    }
    return this
}