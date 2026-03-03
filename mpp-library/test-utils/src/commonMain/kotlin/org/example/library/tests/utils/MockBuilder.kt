package org.example.library.tests.utils

import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.content.TextContent
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

/**
 * Создает обработчик запросов для MockHttpClientEngine, в котором настраиваем моки для запросов.
 * Используется специальный билдер запросов.
 *
 * Минимально необходимое:
 * ```
 * buildHttpMocks {
 *   req("/home")
 *
 *   req("/error") status HttpStatusCode.BadRequest
 * }
 * ```
 * Так создастся мок для запроса GET /home который вернет статус 200 с телом {} по умолчанию.
 * И также будет мок для запроса GET /error который вернет статус 400 с телом {}
 *
 * Более сложный пример:
 * ```
 * buildHttpMocks {
 *   req("/v1/auth", HttpMethod.Post)
 *     .bodyJson("""{"test": "abc"}""")
 *     .respJsonFile("ok.json")
 * }
 * ```
 * Запрос будет POST /v1/auth в котором должен отправляться на сервер Json `{"test": "abc"}` и в
 * ответ будет выдано содержимое файла `ok.json` из `commonTest/resources`.
 *
 * Еще более сложный пример:
 * ```
 * buildHttpMocks {
 *   req("/v1/auth", HttpMethod.Post)
 *     .bodyJsonFile("auth.json")
 *     .bodyArg("KEY", "test")
 *     .respJsonFile("ok.json")
 *     .status(HttpStatusCode.Accepted)
 * }
 * ```
 * Запрос будет POST /v1/auth в котором должен отправляться на сервер Json, считанный из файла
 * `auth.json` и в считанном содержимом будут заменены плейсхлодеры `{KEY}` на `test`, а в
 * ответ будет выдано содержимое файла `ok.json` из `commonTest/resources` и кодом 202.
 *
 * Если запрос не зарегистрирован - будет выброшена ошибка.
 *
 * @param configuration блок настройки запросов. Можно указывать множество запросов
 *
 * @return обработчик запросов для MockHttpClientEngine
 */
fun buildHttpMocks(
    configuration: HttpMockBuilder.() -> Unit
): MockRequestHandler {
    val mocks = mutableListOf<HttpMockDefinition>()
    val builder = object : HttpMockBuilder {
        override fun req(path: String, method: HttpMethod): HttpMockDefinition {
            return HttpMockDefinition(method = method, path = path).also { mocks.add(it) }
        }
    }
    builder.configuration()
    return buildHttpMocks(mocks)
}

private fun buildHttpMocks(
    mocks: List<HttpMockDefinition>
): MockRequestHandler {
    return handler@{ request ->
        // находим нужный нам мок
        val reqMocks: List<HttpMockDefinition> =
            mocks.filter { it.method == request.method && it.path == request.url.encodedPath }

        if (reqMocks.size > 1) {
            error("Multiple mocks for request ${request.method} ${request.url.encodedPath}")
        } else if (reqMocks.isEmpty()) {
            error("Unhandled request ${request.method} ${request.url.encodedPath}")
        }
        val mock: HttpMockDefinition = reqMocks.single()

        // проверяем корректность тела запроса, если оно есть
        mock.requestContentType?.let { contentType ->
            val actualContentType = (request.body as? TextContent)?.contentType
            if (actualContentType != contentType) {
                error("invalid content type - $actualContentType")
            }
        }
        mock.requestBody?.let { body ->
            val actualBody: String =
                (request.body as? TextContent)?.text ?: return@handler respondError(
                    status = HttpStatusCode.BadRequest,
                    content = "empty body"
                )

            // чтобы не влияло форматирование и порядок ключей
            val actualJson = kotlinx.serialization.json.Json.parseToJsonElement(actualBody)
            val expectJson = kotlinx.serialization.json.Json.parseToJsonElement(body)

            if (actualJson != expectJson) {
                error("invalid body - $actualJson")
            }
        }

        // запрос корректен, даем ответ
        respond(
            status = mock.statusCode,
            content = mock.responseBody,
            headers = headersOf(HttpHeaders.ContentType, mock.responseContentType.toString())
        )
    }
}
