package com.oneworldstuddo.effective.neoforge.shader

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.oneworldstuddo.effective.Effective.id


internal object ModShaders2 {

    var PARTICLE: ShaderHolder = ShaderHolder(id("particle/particle"), DefaultVertexFormat.PARTICLE, "LumiTransparency")

}
