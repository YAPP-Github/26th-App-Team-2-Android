package com.teambrake.brake.core.datastore.serializer

import com.teambrake.brake.core.datastore.model.DatastoreUserInfo

internal val UserInfoSerializer = DataSerializer(
	DatastoreUserInfo.Companion.serializer(),
	DatastoreUserInfo.Companion.Empty,
	"User Info Datastore 읽기 실패",
)
