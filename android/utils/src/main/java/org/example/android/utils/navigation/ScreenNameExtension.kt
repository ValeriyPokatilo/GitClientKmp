package org.example.android.utils.navigation

import org.example.android.utils.navigation.DefaultScreenNameExtension.defaultScreenName

object ScreenNameExtension {
    lateinit var allScreens: List<Screen>

    /**
     * Return screen name from screen list of application
     * @return The string path of default screen name
     */
    @Suppress("UnsafeCallOnNullableType")
    inline fun <reified S : Screen> getScreenName(): String {
        return allScreens.find { it is S }!!.screenName
    }

    /**
     * Return navigation path for screen with required parameters
     *
     * @param params The navigation arguments for screen path
     * @return The string path of default screen name with required parameters of navigation
     */
    fun Screen.screenNameWithParams(vararg params: Any): String {
        return defaultScreenName() + params.joinToString(separator = "/", prefix = "/")
    }

    /**
     * Return navigation path for screen with required and optional parameters
     *
     * @param params The navigation arguments for screen path
     * @param optionalParams The optional arguments that will be used for screen path
     * @return The string path of default screen name with required parameters of navigation,
     * path can contains optional parameters
     */
    fun Screen.screenNameWithOptionalParams(
        params: List<Any>,
        optionalParams: List<Pair<String, Any>>
    ): String {
        val optionals = optionalParams.joinToString { "?${it.first}=${it.second}" }
        return defaultScreenName() + params.joinToString { "/$it" } + optionals
    }

    /**
     * Return navigation path for screen only with optional parameters
     *
     * @param optionalParams The optional arguments that will be used for screen path
     * @return The string path of default screen name with optional parameters of navigation,
     */
    fun Screen.screenNameWithOptionalParams(optionalParams: List<Pair<String, Any>>): String {
        val optionals = optionalParams.joinToString { "?${it.first}=${it.second}" }
        return defaultScreenName() + optionals
    }
}
