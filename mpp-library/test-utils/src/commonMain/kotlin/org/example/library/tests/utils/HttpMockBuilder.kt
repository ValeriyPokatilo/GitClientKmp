package org.example.library.tests.utils

import io.ktor.http.HttpMethod

interface HttpMockBuilder {
    /**
     * Добавить Mock запрос.
     *
     * По умолчанию запрос не будет ожидать никакого тела, а в ответ выдаст 200 OK с телом Json {}
     *
     * @param path ожидаемый путь запроса, например /v1/auth
     * @param method ожидаемый метод, например HttpMethod.Post
     *
     * @return возвращает объект для донастройки
     */
    fun req(path: String, method: HttpMethod = HttpMethod.Get): HttpMockDefinition
}
