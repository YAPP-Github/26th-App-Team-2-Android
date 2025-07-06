package com.yapp.breake.core.database.converter

import androidx.room.TypeConverter
import com.yapp.breake.core.model.app.BlockingState
import kotlinx.serialization.json.Json

internal class BlockingStateConverter {

	@TypeConverter
	fun fromBlockingState(blockingState: BlockingState): String {
		return Json.encodeToString(blockingState)
	}

	@TypeConverter
	fun toBlockingState(value: String): BlockingState {
		return Json.decodeFromString(value)
	}
}
