import com.teambrake.brake.configureCoroutineAndroid
import com.teambrake.brake.configureHiltAndroid
import com.teambrake.brake.configureKotest
import com.teambrake.brake.configureKotlinAndroid
import com.teambrake.brake.configureTimber

plugins {
    id("com.android.library")
    id("brake.verify.detekt")
}

configureKotlinAndroid()
configureKotest()
configureCoroutineAndroid()
configureHiltAndroid()
configureTimber()
