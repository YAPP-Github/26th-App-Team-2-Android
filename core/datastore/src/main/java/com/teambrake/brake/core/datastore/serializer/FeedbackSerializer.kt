package com.teambrake.brake.core.datastore.serializer

import com.teambrake.brake.core.datastore.model.DatastoreFeedback

internal val FeedbackSerializer = DataSerializer(
	DatastoreFeedback.Companion.serializer(),
	DatastoreFeedback.Companion.Default,
	"Feedback Datastore 읽기 실패",
)
