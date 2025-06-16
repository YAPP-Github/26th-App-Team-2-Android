import com.yapp.breake.configureHiltAndroid
import com.yapp.breake.configureKotestAndroid
import com.yapp.breake.configureKotlinAndroid


plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
