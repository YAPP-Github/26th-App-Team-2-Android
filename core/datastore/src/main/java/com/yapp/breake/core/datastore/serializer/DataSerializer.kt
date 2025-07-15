package com.yapp.breake.core.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.yapp.breake.core.datastore.encryption.CryptoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

internal class DataSerializer<T>(
	private val serializer: KSerializer<T>,
	override val defaultValue: T,
	private val errorMessage: String = "데이터 읽기 실패",
) : Serializer<T> {
	private val cryptoData: CryptoData = CryptoData()

	override suspend fun readFrom(input: InputStream): T = try {
		val decodeStr = cryptoData.decrypt(input.readBytes())
			?: throw CorruptionException(errorMessage)
		Json.decodeFromString(
			serializer,
			decodeStr.decodeToString(),
		)
	} catch (serialization: SerializationException) {
		Timber.e(serialization, errorMessage)
		defaultValue
	}

	override suspend fun writeTo(
		t: T,
		output: OutputStream,
	) {
		withContext(Dispatchers.IO) {
			output.write(
				cryptoData.encrypt(
					Json.encodeToString(serializer, t)
						.encodeToByteArray(),
				),
			)
		}
	}
}
