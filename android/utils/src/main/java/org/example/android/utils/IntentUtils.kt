package org.example.android.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri
import io.github.aakira.napier.Napier

/**
 * Открывает почтовое приложение с предзаполненным адресом получателя.
 *
 * Использует [Intent.ACTION_SENDTO] с URI-схемой `mailto:`, что гарантирует
 * открытие именно почтового клиента, а не любого приложения для отправки данных.
 *
 * Если подходящее приложение не найдено, ошибка логируется через Napier.
 *
 * @param context Контекст, из которого вызывается функция.
 * @param email Адрес электронной почты получателя.
 *
 * Пример:
 * ```
 * openEmailApp(context, "support@example.com")
 * ```
 */
fun openEmailApp(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))

        if (context.findActivityOrNull() == null) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    try {
        context.startActivity(intent)
    } catch (exception: Exception) {
        Napier.e("can't open email app", exception)
    }
}

/**
 * Открывает экран выбора почтового приложения (chooser) с возможностью задать тему письма.
 *
 * Если на устройстве отсутствуют приложения, поддерживающие [Intent.ACTION_SENDTO]
 * с URI `mailto:`, пользователю отображается сообщение об ошибке [errorMessage].
 *
 * @param context Контекст вызова.
 * @param email Адрес получателя.
 * @param pickerTitle Заголовок окна выбора почтового приложения.
 * @param errorMessage Текст сообщения при отсутствии почтовых приложений.
 * @param title Тема письма (опционально).
 *
 * Пример:
 * ```
 * sendEmail(
 *     context,
 *     email = "support@example.com",
 *     pickerTitle = "Выберите почтовое приложение",
 *     errorMessage = "Нет доступных почтовых клиентов",
 *     title = "Обратная связь"
 * )
 * ```
 */
fun sendEmail(
    context: Context,
    email: String,
    pickerTitle: String,
    errorMessage: String,
    title: String? = null,
) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, title.orEmpty())

        if (context.findActivityOrNull() == null) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    try {
        context.startActivity(Intent.createChooser(intent, pickerTitle))
    } catch (e: ActivityNotFoundException) {
        Napier.e("can't open email app", e)
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Открывает приложение для звонков с предзаполненным номером телефона.
 *
 * Использует [Intent.ACTION_DIAL], который открывает экран набора номера,
 * но не выполняет сам звонок (в отличие от [Intent.ACTION_CALL], требующего разрешений).
 *
 * Перед открытием номер очищается от недопустимых символов.
 *
 * @param context Контекст вызова.
 * @param phoneNumber Номер телефона, который нужно набрать.
 *
 * Пример:
 * ```
 * callPhoneNumber(context, "+7 (999) 123-45-67")
 * ```
 */
fun callPhoneNumber(context: Context, phoneNumber: String) {
    val cleanedNumber = phoneNumber.filter { it.isDigit() || it == '+' }

    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$cleanedNumber".toUri()

        if (context.findActivityOrNull() == null) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Napier.e("can't open phone app", e)
    }
}

/**
 * Открывает навигационное приложение (Google Maps, Яндекс.Карты, 2ГИС)
 * с маршрутом от точки A до точки B.
 *
 * Приоритет отдаётся Google Maps, затем Яндекс.Картам, затем 2ГИС.
 * Если ни одно приложение не найдено, открывается веб-версия маршрута.
 *
 * @param context Контекст вызова.
 * @param chooserTitle Заголовок окна выбора навигатора.
 * @param failedMessage Сообщение, отображаемое при отсутствии навигационных приложений.
 * @param startLatitude Широта точки старта.
 * @param startLongitude Долгота точки старта.
 * @param endLatitude Широта точки назначения.
 * @param endLongitude Долгота точки назначения.
 *
 * Пример:
 * ```
 * openNavigator(
 *     context,
 *     chooserTitle = "Выберите навигатор",
 *     failedMessage = "Нет доступных навигационных приложений",
 *     startLatitude = 55.751244,
 *     startLongitude = 37.618423,
 *     endLatitude = 59.934280,
 *     endLongitude = 30.335099
 * )
 * ```
 */
fun openNavigator(
    context: Context,
    chooserTitle: String,
    failedMessage: String,
    startLatitude: Double,
    startLongitude: Double,
    endLatitude: Double,
    endLongitude: Double,
) {
    // URI для Google Maps и совместимых приложений
    val geoUri = "google.navigation:q=$endLatitude,$endLongitude&mode=d" // d = driving

    val geoIntent = Intent(Intent.ACTION_VIEW, geoUri.toUri())

    // URI для Яндекс.Карт
    val yandexUri =
        "yandexmaps://maps.yandex.ru/?rtext=$startLatitude,$startLongitude~$endLatitude,$endLongitude&rtt=auto"
    val yandexIntent = Intent(Intent.ACTION_VIEW, yandexUri.toUri())

    // URI для 2ГИС (отдельный кейс, если интересно)
    val dgUri = "dgis://2gis.ru/routeSearch/rsType/car/to/$endLongitude,$endLatitude"
    val dgIntent = Intent(Intent.ACTION_VIEW, dgUri.toUri())

    // Попробуем сразу через chooser
    val chooserIntents = listOf(geoIntent, yandexIntent, dgIntent)

    // Вариант без package — пусть система подберёт по intent-filter-ам
    val chooser = Intent.createChooser(geoIntent, chooserTitle)
    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, chooserIntents.toTypedArray())

    try {
        context.startActivity(chooser)
    } catch (e: Exception) {
        // Если ничего не найдено - используем веб-ссылку
        try {
            val webUri =
                "https://yandex.ru/maps/?rtext=$startLatitude,$startLongitude~$endLatitude,$endLongitude&rtt=auto"
            val webIntent = Intent(Intent.ACTION_VIEW, webUri.toUri())
            context.startActivity(webIntent)
        } catch (e: Exception) {
            Napier.e("openNavigator failed", e)
            // Можно показать сообщение пользователю, что ни одно приложение не найдено
            Toast
                .makeText(
                    context,
                    failedMessage,
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }
}

/**
 * Проверяет, доступен ли на устройстве Google Play Store.
 *
 * Создаёт [Intent.ACTION_VIEW] с URL магазина и проверяет, есть ли приложение,
 * способное его обработать (обычно `com.android.vending`).
 *
 * @return `true`, если Play Store доступен, иначе `false`.
 *
 * Пример:
 * ```
 * if (context.hasGooglePlay()) {
 *     // открыть ссылку на приложение
 * }
 * ```
 */
fun Context.hasGooglePlay(): Boolean {
    val playStoreUrl = "https://play.google.com/store/apps/details?id="
    val intent = Intent(Intent.ACTION_VIEW, playStoreUrl.toUri())

    return intent.resolveActivity(this.packageManager) != null
}

/**
 * Проверяет, доступен ли на устройстве хотя бы один браузер,
 * способный обрабатывать ссылки (Intent с ACTION_VIEW и CATEGORY_BROWSABLE).
 *
 * Используется для безопасного открытия URL через [Intent.ACTION_VIEW].
 *
 * ⚙️ Требования для Android 11 (API 30) и выше:
 * из-за ограничений на видимость пакетов необходимо добавить в `AndroidManifest.xml`
 * секцию `<queries>`, чтобы приложение могло находить браузеры:
 *
 * ```xml
 * <queries>
 *     <intent>
 *         <action android:name="android.intent.action.VIEW" />
 *         <category android:name="android.intent.category.BROWSABLE" />
 *         <data android:scheme="http" />
 *     </intent>
 *     <intent>
 *         <action android:name="android.intent.action.VIEW" />
 *         <category android:name="android.intent.category.BROWSABLE" />
 *         <data android:scheme="https" />
 *     </intent>
 * </queries>
 * ```
 *
 * Без этой секции метод всегда будет возвращать `false` на Android 11+,
 * даже если браузер (например, Chrome) установлен.
 *
 * @return `true`, если в системе найден хотя бы один браузер, иначе `false`.
 */
fun Context.isBrowserAvailable(): Boolean {
    val intent = Intent(Intent.ACTION_VIEW, "https://www.google.com".toUri()).apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
    }
    val resolveInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(
        intent,
        PackageManager.MATCH_DEFAULT_ONLY
    )
    return resolveInfoList.isNotEmpty()
}

/**
 * Пытается открыть указанный URL в браузере устройства.
 *
 * Если на устройстве установлен браузер (определяется через [isBrowserAvailable]),
 * создаётся [Intent] с действием [Intent.ACTION_VIEW] и переданным URL.
 * При отсутствии браузера пользователю показывается сообщение [noBrowserMessage] через [Toast].
 *
 * Если контекст, из которого вызывается функция, **не содержит Activity
 * (через ContextWrapper)** [Activity], добавляется флаг [Intent.FLAG_ACTIVITY_NEW_TASK],
 * чтобы открыть браузер из любого контекста
 * (например, из [android.app.Application] или [android.app.Service]).
 *
 * Возможные причины ошибок:
 * - Передан некорректный URL (без схемы `http://` или `https://`);
 * - Отсутствуют браузеры, обрабатывающие [Intent.ACTION_VIEW];
 * - Нарушения ограничений видимости пакетов (Android 11+ — требуется `<queries>` в манифесте).
 *
 * Пример использования:
 * ```
 * context.openUrl("https://example.com", "Браузер не найден")
 * ```
 * ⚙️ Требования для Android 11 (API 30) и выше:
 * из-за ограничений на видимость пакетов необходимо добавить в `AndroidManifest.xml`
 * секцию `<queries>`, чтобы приложение могло находить браузеры:
 *
 * ```xml
 * <queries>
 *     <intent>
 *         <action android:name="android.intent.action.VIEW" />
 *         <category android:name="android.intent.category.BROWSABLE" />
 *         <data android:scheme="http" />
 *     </intent>
 *     <intent>
 *         <action android:name="android.intent.action.VIEW" />
 *         <category android:name="android.intent.category.BROWSABLE" />
 *         <data android:scheme="https" />
 *     </intent>
 * </queries>
 * ```
 *
 * @param url URL-адрес, который нужно открыть.
 * @param noBrowserMessage Текст сообщения, отображаемого при отсутствии браузера.
 *
 * @see isBrowserAvailable
 */
fun Context.openUrl(url: String, noBrowserMessage: String) {
    if (isBrowserAvailable()) {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                    if (this@openUrl.findActivityOrNull() == null) {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
            )
        } catch (exception: Exception) {
            Napier.d("Error tryOpenInBrowser", exception)
        }
    } else {
        Toast.makeText(
            this,
            noBrowserMessage,
            Toast.LENGTH_LONG
        ).show()
    }
}

/**
 * Открывает системные настройки уведомлений текущего приложения.
 *
 * Использует [Settings.ACTION_APP_NOTIFICATION_SETTINGS], чтобы направить пользователя
 * прямо на экран управления уведомлениями, где можно включить или выключить их
 * для данного приложения.
 *
 * Если Context, из которого вызывается функция, **не содержит** [Activity]
 * (например, [android.app.Application] или [android.app.Service]),
 * добавляется флаг [Intent.FLAG_ACTIVITY_NEW_TASK].
 *
 * В случае ошибки (например, если системное действие недоступно
 * на конкретной версии Android или отсутствует Activity для обработки Intent),
 * исключение логируется, а приложение продолжает работу без падения.
 *
 * Пример использования:
 * ```
 * context.openNotificationSettings()
 * ```
 */
fun Context.openNotificationSettings() {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)

        if (findActivityOrNull() == null) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    try {
        startActivity(intent)
    } catch (e: Exception) {
        Napier.e(e) { "Failed to open notification settings" }
    }
}
