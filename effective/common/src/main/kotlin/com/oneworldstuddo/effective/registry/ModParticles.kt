package com.oneworldstuddo.effective.registry

import com.oneworldstuddo.effective.Effective
import com.oneworldstuddo.effective.particle.AllayTwinkleParticle
import com.oneworldstuddo.effective.particle.BubbleParticle
import com.oneworldstuddo.effective.particle.CascadeCloudParticle
import com.oneworldstuddo.effective.particle.ChorusPetalParticle
import com.oneworldstuddo.effective.particle.DropletParticle
import com.oneworldstuddo.effective.particle.EndBubbleParticle
import com.oneworldstuddo.effective.particle.EyesParticle
import com.oneworldstuddo.effective.particle.FireflyParticle
import com.oneworldstuddo.effective.particle.GlowingCascadeCloudParticle
import com.oneworldstuddo.effective.particle.GlowingDropletParticle
import com.oneworldstuddo.effective.particle.GlowingRippleParticle
import com.oneworldstuddo.effective.particle.GlowingSplashParticle
import com.oneworldstuddo.effective.particle.LavaSplashParticle
import com.oneworldstuddo.effective.particle.MistParticle
import com.oneworldstuddo.effective.particle.RippleParticle
import com.oneworldstuddo.effective.particle.SculkDustParticle
import com.oneworldstuddo.effective.particle.SplashParticle
import com.oneworldstuddo.effective.particle.WillOWispParticle
import com.oneworldstuddo.effective.particle.WispParticle
import com.oneworldstuddo.effective.particle.type.AllayTwinkleParticleType
import com.oneworldstuddo.effective.particle.type.SplashParticleType
import com.oneworldstuddo.effective.particle.type.WispParticleType
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.ResourceLocation
import java.awt.Color

object ModParticles {

    val PARTICLES = mutableListOf<ParticleEntry<*, *>>()

    val CASCADE_CLOUD by register("cascade_cloud", CascadeCloudParticle::Provider) { SimpleParticleType(true) }
    val GLOWING_CASCADE_CLOUD by register("glowing_cascade_cloud",
        GlowingCascadeCloudParticle::Provider) { SimpleParticleType(true) }
    val MIST by register("mist", MistParticle::Provider) { SimpleParticleType(true) }
    val SPLASH by register("splash", SplashParticle::Provider) { SplashParticleType(true) }
    val GLOWING_SPLASH by register("glowing_splash", GlowingSplashParticle::Provider) { SplashParticleType(true) }
    val DROPLET by register("droplet", DropletParticle::Provider) { SimpleParticleType(true) }
    val GLOWING_DROPLET by register("glowing_droplet", GlowingDropletParticle::Provider) { SimpleParticleType(true) }
    val RIPPLE by register("ripple", RippleParticle::Provider) { SimpleParticleType(true) }
    val GLOWING_RIPPLE by register("glowing_ripple", GlowingRippleParticle::Provider) { SimpleParticleType(true) }
    val BUBBLE by register("bubble", BubbleParticle::Provider) { SimpleParticleType(true) }
    val END_BUBBLE by register("end_bubble", EndBubbleParticle::Provider) { SimpleParticleType(true) }
    val FIREFLY by register("firefly", FireflyParticle::Provider) { SimpleParticleType(true) }
    val CHORUS_PETAL by register("chorus_petal", ChorusPetalParticle::Provider) { SimpleParticleType(true) }
    val WILL_O_WISP by register("will_o_wisp", { spriteSet ->
        WillOWispParticle.Provider(spriteSet, Color(0.1F, 0.75F, 1F), Color(0F, 0.1F, 1F))
    }) { SimpleParticleType(true) }
    val WISP by register("wisp", WispParticle::Provider) { WispParticleType(true) }
    val ALLAY_TWINKLE by register("allay_twinkle", AllayTwinkleParticle::Provider) { AllayTwinkleParticleType(true) }
    val EYES by register("eyes", EyesParticle::Provider) { SimpleParticleType(true) }
    val SCULK_DUST by register("sculk_dust", SculkDustParticle::Provider) { SimpleParticleType(true) }
    val LAVA_SPLASH by register("lava_splash", LavaSplashParticle::Provider) { SplashParticleType(true) }

    private fun <I : ParticleType<T>, T : ParticleOptions> register(
        id: String, provider: (SpriteSet) -> ParticleProvider<T>, type: () -> I
    ): Lazy<I> {
        val lazyType = lazy { type.invoke() }
        val entry = ParticleEntry(Effective.id(id), lazyType, provider)
        PARTICLES += entry
        return lazyType
    }

    data class ParticleEntry<I : ParticleType<T>, T : ParticleOptions>(
        val id: ResourceLocation,
        val type: Lazy<I>,
        val provider: (SpriteSet) -> ParticleProvider<T>,
        var extra: Any? = null
    )

}
