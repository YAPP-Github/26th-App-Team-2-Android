import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
}

android {
	setNamespace("presentation.onboarding")
}
