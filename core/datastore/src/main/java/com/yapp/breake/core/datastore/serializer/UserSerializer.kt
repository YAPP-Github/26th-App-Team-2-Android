package com.yapp.breake.core.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.yapp.breake.core.datastore.encryption.CryptoData
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

internal object UserSerializer : Serializer<DatastoreUserToken> {
	private val cryptoData: CryptoData = CryptoData()

	override val defaultValue: DatastoreUserToken
		get() = DatastoreUserToken.Empty

	override suspend fun readFrom(input: InputStream): DatastoreUserToken = try {
		val decodeStr = cryptoData.decrypt(input.readBytes())
			?: throw CorruptionException("User Token Datastore 읽기 실패")
		Json.decodeFromString(
			DatastoreUserToken.serializer(),
			decodeStr.decodeToString(),
		)
	} catch (serialization: SerializationException) {
		Timber.e(serialization, "User Token Datastore 읽기 실패")
		defaultValue
	}

	override suspend fun writeTo(
		t: DatastoreUserToken,
		output: OutputStream,
	) {
		withContext(Dispatchers.IO) {
			output.write(
				cryptoData.encrypt(
					Json.encodeToString(DatastoreUserToken.serializer(), t)
						.encodeToByteArray(),
				),
			)
		}
	}
}
