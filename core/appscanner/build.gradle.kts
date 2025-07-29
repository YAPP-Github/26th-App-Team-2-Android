import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
}

android {
	setNamespace("core.appscanner")
}
