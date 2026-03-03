package org.example.library.utils

/**
 * Создает новый список, в котором элементы удовлетворяющие условию будут заменены на новый.
 */
fun <T, C : Collection<T>> C.replace(
    condition: (T) -> Boolean,
    newItem: T
): List<T> {
    return map { item ->
        if (condition(item)) newItem else item
    }
}
