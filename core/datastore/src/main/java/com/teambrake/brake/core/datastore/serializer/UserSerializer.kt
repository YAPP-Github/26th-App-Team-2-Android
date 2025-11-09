package com.teambrake.brake.core.datastore.serializer

import com.teambrake.brake.core.datastore.model.DatastoreUserToken

internal val UserSerializer = DataSerializer(
	DatastoreUserToken.Companion.serializer(),
	DatastoreUserToken.Companion.Empty,
	"User Token Datastore 읽기 실패",
)
