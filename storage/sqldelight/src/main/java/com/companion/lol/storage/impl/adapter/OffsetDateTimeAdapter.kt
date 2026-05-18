package com.companion.lol.storage.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.companion.lol.storage.impl.model.model.OffsetDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.UtcOffset

object OffsetDateTimeAdapter: ColumnAdapter<OffsetDateTime, String> {
    override fun decode(databaseValue: String): OffsetDateTime {
        val (date, offset) = databaseValue.split("#")
        return OffsetDateTime(
            dateTime = LocalDateTime.parse(date),
            offset = UtcOffset.parse(offset)
        )
    }

    override fun encode(value: OffsetDateTime): String {
        val (date, offset) = value
        return "$date#$offset"
    }

}
