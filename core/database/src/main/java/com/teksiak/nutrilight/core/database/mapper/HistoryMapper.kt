package com.teksiak.nutrilight.core.database.mapper

import com.teksiak.nutrilight.core.database.entity.HistoryEntity

fun String.asHistoryEntity() = HistoryEntity(code = this)