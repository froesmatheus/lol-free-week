package com.matheusfroes.lolfreeweek.data.model

import java.util.*

enum class Platform(val id: String, private val initials: String) {
    NA("NA1", "na"),
    LAN("LA1", "lan"),
    LAS("LA2", "las"),
    BR("BR1", "br"),
    KR("KR", "kr"),
    EUNE("EUN1", "eune"),
    EUW("EUW1", "euw"),
    JP("JP1", "jp"),
    OCE("OC1", "oce"),
    RU("RU", "ru"),
    TR("TR1", "tr");

    override fun toString(): String {
        return id
    }

    fun getName() = initials

    companion object {
        private val locales = listOf(
                "cs_CZ",
                "el_GR",
                "pl_PL",
                "ro_RO",
                "hu_HU",
                "en_GB",
                "de_DE",
                "es_ES",
                "it_IT",
                "fr_FR",
                "ja_JP",
                "ko_KR",
                "es_MX",
                "es_AR",
                "pt_BR",
                "en_US",
                "en_AU",
                "ru_RU",
                "tr_TR",
                "ms_MY",
                "en_PH",
                "en_SG",
                "th_TH",
                "vn_VN",
                "id_ID",
                "zh_MY",
                "zh_CN",
                "zh_TW")


        fun getDefaultLocale(): String {
            val locale = Locale.getDefault()
            val localeStr = "${locale.language}_${locale.country}"

            return if (locales.contains(localeStr)) localeStr else "en_US"
        }

        fun getPlatformByName(name: String): Platform {
            for (platform in Platform.values()) {
                if (platform.name.toLowerCase() == name.toLowerCase()) {
                    return platform
                }
            }
            throw NoSuchElementException("Unknown platform name: $name")
        }

        fun getPlatforms(): List<Platform> {
            return listOf(NA, LAN, LAS, BR, KR, EUNE, EUW, JP, OCE, RU, TR)
        }
    }
}