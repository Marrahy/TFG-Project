package com.sergimarrahyarenas.bloodstats.models.itemdata

data class ItemData(
    val maxPageSize: Int,
    val page: Int,
    val pageCount: Int,
    val pageSize: Int,
    val results: List<Result>
)