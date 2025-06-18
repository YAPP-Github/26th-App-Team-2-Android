import com.yapp.breake.configureCoroutineAndroid
import com.yapp.breake.configureHiltAndroid
import com.yapp.breake.configureKotest
import com.yapp.breake.configureKotlinAndroid

plugins {
    id("com.android.library")
    id("breake.verify.detekt")
}

configureKotlinAndroid()
configureKotest()
configureCoroutineAndroid()
configureHiltAndroid()