import com.teambrake.brake.configureHiltAndroid
import com.teambrake.brake.configureKotestAndroid
import com.teambrake.brake.configureKotlinAndroid
import com.teambrake.brake.configureRoborazzi
import com.teambrake.brake.configureTimber

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
configureRoborazzi()
configureTimber()
