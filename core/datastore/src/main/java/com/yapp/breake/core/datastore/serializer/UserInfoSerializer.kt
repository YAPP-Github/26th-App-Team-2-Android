package com.yapp.breake.core.datastore.serializer

import com.yapp.breake.core.datastore.model.DatastoreUserInfo

internal val UserInfoSerializer = DataSerializer(
	DatastoreUserInfo.Companion.serializer(),
	DatastoreUserInfo.Companion.Empty,
	"User Info Datastore 읽기 실패",
)
