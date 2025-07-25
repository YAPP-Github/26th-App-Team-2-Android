package com.yapp.breake.core.datastore.serializer

import com.yapp.breake.core.datastore.model.DatastoreOnboarding

internal val OnboardingSerializer = DataSerializer(
	DatastoreOnboarding.Companion.serializer(),
	DatastoreOnboarding.Companion.Default,
	"Onboarding Flag Datastore 읽기 실패",
)
