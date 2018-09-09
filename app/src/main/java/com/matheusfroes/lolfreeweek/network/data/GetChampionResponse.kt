package com.matheusfroes.lolfreeweek.network.data

data class GetChampionResponse(
        val type: String,
        val format: String,
        val version: String,
        val data: Map<String, ChampionResponse>)

data class ChampionResponse(
        var id: String,
        var image: ImageResponse,
        var key: Int,
        var lore: String,
        var name: String,
        var title: String,
        var spells: List<SpellResponse>,
        var skins: List<SkinResponse>)

data class SpellResponse(
        var id: String,
        var name: String,
        var image: ImageResponse,
        var description: String)

data class SkinResponse(
        var name: String,
        var num: Int = 0)

data class ImageResponse(
        val full: String,
        val group: String,
        val h: Int,
        val sprite: String,
        val w: Int,
        val x: Int,
        val y: Int)
