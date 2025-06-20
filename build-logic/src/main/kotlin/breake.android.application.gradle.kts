import com.yapp.breake.configureHiltAndroid
import com.yapp.breake.configureKotestAndroid
import com.yapp.breake.configureKotlinAndroid
import com.yapp.breake.configureRoborazzi
import com.yapp.breake.configureTimber

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
configureRoborazzi()
configureTimber()
