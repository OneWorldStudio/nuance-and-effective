@file:JvmName("Platform")

package com.oneworldstuddo.effective

import dev.architectury.injectables.annotations.ExpectPlatform

@ExpectPlatform
fun isSatinInstalled(): Boolean {
    throw NotImplementedError()
}