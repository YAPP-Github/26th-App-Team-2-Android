package com.teambrake.brake.core.datastore.serializer

import com.teambrake.brake.core.datastore.model.DatastoreAuthCode

internal val AuthSerializer = DataSerializer(
	DatastoreAuthCode.Companion.serializer(),
	DatastoreAuthCode.Companion.Empty,
	"Auth Code Datastore 읽기 실패",
)
