import com.yapp.breake.configureHiltAndroid
import com.yapp.breake.configureKotestAndroid
import com.yapp.breake.configureKotlinAndroid
import com.yapp.breake.configureRoborazzi
import com.yapp.breake.libs
import gradle.kotlin.dsl.accessors._17e8f14a00f6010565e411c5c2a1e9c3.implementation


plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
configureRoborazzi()

dependencies{
    val libs = project.extensions.libs
    implementation(libs.findLibrary("timber").get())
}