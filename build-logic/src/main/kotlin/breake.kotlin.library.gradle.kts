import com.yapp.breake.configureCoroutineKotlin
import com.yapp.breake.configureKotest
import com.yapp.breake.configureKotlin

plugins {
    kotlin("jvm")
    id("breake.verify.detekt")
}

configureKotlin()
configureCoroutineKotlin()
configureKotest()
configureCoroutineKotlin()
