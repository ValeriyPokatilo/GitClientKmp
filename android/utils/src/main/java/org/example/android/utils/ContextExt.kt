package org.example.android.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Пытается получить [Activity], ассоциированную с данным [Context].
 *
 * Функция рекурсивно разворачивает цепочку [ContextWrapper] и возвращает
 * первую найденную [Activity], если она присутствует в иерархии контекстов.
 *
 * Наличие [Activity] **не гарантировано**.
 * Функция может вернуть `null`, если контекст был получен из:
 * - [android.app.Application]
 * - [android.app.Service]
 * - [android.content.BroadcastReceiver]
 * - фоновых задач (WorkManager и т.п.)
 * - тестового или платформенного окружения
 *
 * Рекомендуется использовать в случаях, когда [Activity] **опциональна**
 * (например, для определения необходимости флага
 * [android.content.Intent.FLAG_ACTIVITY_NEW_TASK]).
 *
 * Пример использования:
 * ```
 * val activity = context.findActivityOrNull()
 * if (activity == null) {
 *     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
 * }
 * ```
 *
 * @return [Activity], если она присутствует в цепочке контекстов,
 * или `null`, если [Activity] не найдена.
 */
fun Context.findActivityOrNull(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
