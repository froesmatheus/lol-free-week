package com.matheusfroes.lolfreeweek.extra

sealed class ResultDownload<D> {

    class InProgress<D>(val progress: Int, val max: Int) : ResultDownload<D>()

    data class Complete<D>(val data: D? = null) : ResultDownload<D>()

    data class Error<D>(val error: Throwable? = null) : ResultDownload<D>()

}
