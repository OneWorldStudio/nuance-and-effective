package com.oneworldstuddo.effective.fabric

import com.oneworldstuddo.effective.Effective
import net.fabricmc.api.ModInitializer

class EffectiveFabric : ModInitializer {

    override fun onInitialize() {
        Effective.onInitialize()
    }

}