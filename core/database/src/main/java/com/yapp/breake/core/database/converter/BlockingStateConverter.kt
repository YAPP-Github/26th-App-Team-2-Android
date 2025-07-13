package com.yapp.breake.core.database.converter

import androidx.room.TypeConverter
import com.yapp.breake.core.model.app.BlockingState

internal class BlockingStateConverter {

	@TypeConverter
	fun fromBlockingState(blockingState: BlockingState): String {
		return blockingState.name
	}

	@TypeConverter
	fun toBlockingState(value: String): BlockingState {
		return BlockingState.valueOf(value)
	}
}
