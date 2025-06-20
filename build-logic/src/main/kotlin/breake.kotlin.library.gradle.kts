import com.yapp.breake.configureKotest
import com.yapp.breake.configureKotlin

plugins {
    kotlin("jvm")
    id("breake.verify.detekt")
}

configureKotlin()
configureKotest()
