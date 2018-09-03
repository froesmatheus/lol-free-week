package com.matheusfroes.lolfreeweek.data

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import net.rithms.riot.constant.Platform
import javax.inject.Inject

class UserPreferences @Inject constructor() : KotprefModel() {

    companion object {
        const val CURRENT_PLATFORM = "com.matheusfroes.lolfreeweek.current_platform"
        const val CURRENT_API_VERSION = "com.matheusfroes.lolfreeweek.current_api_version"
        const val FIRST_ACCESS = "com.matheusfroes.lolfreeweek.first_access"
    }

    var currentPlatform by enumValuePref(Platform.NA, key = CURRENT_PLATFORM)
    var currentApiVersion by stringPref(key = CURRENT_API_VERSION, default = "8.17.1")
    var firstAccess by booleanPref(key = FIRST_ACCESS, default = true)
}