package com.oneworldstuddo.effective.particle.type

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.core.particles.SimpleParticleType
import kotlin.math.abs

@Environment(EnvType.CLIENT)
class SplashParticleType(alwaysShow: Boolean) : SimpleParticleType(alwaysShow) {
    var width = 0F
    var velocityY = 0.0
        set(value) {
            field = abs(value)
        }
}