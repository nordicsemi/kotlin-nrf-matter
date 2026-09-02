package no.nordicsemi.nrf.matter.logger.db

import no.nordicsemi.nrf.matter.logger.LogEntity

fun LogDbEntity.toDomain(): LogEntity {
    return LogEntity(
        date = date,
        level = level,
        tag = tag,
        message = message,
    )
}

fun LogEntity.toEntity(): LogDbEntity {
    return LogDbEntity(
        date = date,
        level = level,
        tag = tag,
        message = message,
    )
}
