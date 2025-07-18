package com.yapp.breake.core.datastore.serializer

import com.yapp.breake.core.datastore.model.DatastoreUserToken

internal val UserSerializer = DataSerializer(
	DatastoreUserToken.Companion.serializer(),
	DatastoreUserToken.Companion.Empty,
	"User Token Datastore 읽기 실패",
)
