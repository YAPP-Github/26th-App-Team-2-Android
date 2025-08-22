package com.yapp.breake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class StatisticResponse(
	val data: StatisticData,
)

@Serializable
internal data class StatisticData(
	val statistics: List<Statistic>,
)

@Serializable
internal data class Statistic(
	val date: String,
	val dayOfWeek: String,
	val actualTime: String,
	val goalTime: String,
)
