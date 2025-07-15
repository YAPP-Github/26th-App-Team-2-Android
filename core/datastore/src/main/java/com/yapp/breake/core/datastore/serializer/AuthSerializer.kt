package com.yapp.breake.core.datastore.serializer

import com.yapp.breake.core.datastore.model.DatastoreAuthCode

internal val AuthSerializer = DataSerializer(
	DatastoreAuthCode.Companion.serializer(),
	DatastoreAuthCode.Companion.Empty,
	"Auth Code Datastore 읽기 실패",
)
