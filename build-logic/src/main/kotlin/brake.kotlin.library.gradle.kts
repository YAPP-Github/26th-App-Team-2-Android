import com.teambrake.brake.configureCoroutineKotlin
import com.teambrake.brake.configureKotest
import com.teambrake.brake.configureKotlin

plugins {
    kotlin("jvm")
    id("brake.verify.detekt")
}

configureKotlin()
configureCoroutineKotlin()
configureKotest()
configureCoroutineKotlin()
