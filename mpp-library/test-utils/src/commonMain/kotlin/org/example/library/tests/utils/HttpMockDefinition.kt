package org.example.library.tests.utils

import dev.icerock.tests.utils.readResourceText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

/**
 * Данные Mock запроса с возможностью донастройки логики mock'а.
 */
class HttpMockDefinition(
    internal val method: HttpMethod,
    internal val path: String
) {
    // по умолчанию никакое тело мы не ожидаем
    internal var requestBody: String? = null
    internal var requestContentType: ContentType? = null

    // по умолчанию просто выдаем Json {}
    internal var responseBody: String = "{}"
    internal var responseContentType: ContentType = ContentType.Application.Json

    // по умолчанию выдаем 200 OK
    internal var statusCode: HttpStatusCode = HttpStatusCode.OK

    /**
     * Какое тело запроса ожидается при отправке.
     *
     * В случае Json сравнение будет происходить по данным внутри.
     *
     * Если тело в запросе не будет соответствовать ожидаемому - будет выброшена ошибка.
     *
     * @param body тело в виде строки, которое должно быть отправлено в запросе клиентом
     * @param contentType какой тип контента должен указывать клиент
     */
    fun body(
        body: String,
        contentType: ContentType
    ): HttpMockDefinition = apply {
        requestBody = body
        requestContentType = contentType
    }

    /**
     * Какое тело запроса ожидается при отправке.
     *
     * Сравнение будет происходить по данным внутри.
     *
     * Если тело в запросе не будет соответствовать ожидаемому - будет выброшена ошибка.
     *
     * @param body тело в виде строки, которое должно быть отправлено в запросе клиентом
     */
    infix fun bodyJson(body: String): HttpMockDefinition =
        body(body = body, contentType = ContentType.Application.Json)

    /**
     * Какое тело запроса ожидается при отправке. Данные считаются из файла в `commonTest/resources`.
     *
     * Сравнение будет происходить по данным внутри.
     *
     * Если тело в запросе не будет соответствовать ожидаемому - будет выброшена ошибка.
     *
     * @param path путь до файла в ресурсах.
     */
    infix fun bodyJsonFile(path: String): HttpMockDefinition =
        bodyJson(body = readResourceText(path))

    /**
     * Заменить в ожидаемом теле запроса плейсхолдер на какое-то значение.
     *
     * (!) Важно вызывать данную функцию ПОСЛЕ указания `body`
     *
     * @param key имя плейсхлдера. Например `TEST` означает что плейсхолдер ищется `{TEST}`
     * @param value на что заменить плейсхолдер
     */
    fun bodyArg(key: String, value: String): HttpMockDefinition = apply {
        requestBody = requestBody?.replace("{$key}", value)
    }

    /**
     * Какой ответ нужно выдать в случае если запрос соответствует ожиданиям.
     *
     * @param body тело ответа
     * @param contentType какой ContentType будет выдан
     */
    fun resp(
        body: String,
        contentType: ContentType
    ): HttpMockDefinition = apply {
        responseBody = body
        responseContentType = contentType
    }

    /**
     * Какой ответ нужно выдать в случае если запрос соответствует ожиданиям.
     *
     * Ответ будет с типом Json
     *
     * @param body тело ответа
     */
    infix fun respJson(body: String): HttpMockDefinition =
        resp(body = body, contentType = ContentType.Application.Json)

    /**
     * Какой ответ нужно выдать в случае если запрос соответствует ожиданиям.
     *
     * Ответ будет с типом Json. Сам контент будет считан из файла в `commonTest/resources`
     *
     * @param path путь до файла в ресурсах
     */
    infix fun respJsonFile(path: String): HttpMockDefinition =
        respJson(body = readResourceText(path))

    /**
     * Какой Http Status Code нужно выдать в ответ на запрос.
     *
     * @param code статус код
     */
    infix fun status(code: HttpStatusCode): HttpMockDefinition = apply {
        statusCode = code
    }
}
