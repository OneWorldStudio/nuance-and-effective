package com.oneworldstuddo.effective.neoforge

import com.oneworldstuddo.effective.Effective
import com.oneworldstuddo.effective.EffectiveConfig
import com.oneworldstuddo.effective.effect.CascadeManager
import com.oneworldstuddo.effective.effect.GlowSquidHypnosisManager
import com.oneworldstuddo.effective.neoforge.shader.ModShaders2
import com.oneworldstuddo.effective.neoforge.shader.ParticleEngineClientExtensions
import com.oneworldstuddo.effective.isSatinInstalled
import com.oneworldstuddo.effective.particle.model.SplashBottomModel
import com.oneworldstuddo.effective.particle.model.SplashBottomRimModel
import com.oneworldstuddo.effective.particle.model.SplashModel
import com.oneworldstuddo.effective.particle.model.SplashRimModel
import com.oneworldstuddo.effective.registry.ModParticles
import com.oneworldstuddo.effective.registry.ModSounds
import com.oneworldstuddo.effective.render.ModShaders
import com.oneworldstuddo.effective.screenshake.ScreenShakeManager
import dev.cammiescorner.velvet.api.event.EntitiesPreRenderCallback
import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.ParticleEngine
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent
import net.neoforged.neoforge.client.event.RegisterShadersEvent
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.util.function.Supplier


@Mod(Effective.MOD_ID)
class EffectiveNeoForge(modContainer: ModContainer) {

    companion object {
        private val PARTICLE_RESOURCE_KEY: ResourceKey<Registry<ParticleType<*>>> = ResourceKey.createRegistryKey(
            Effective.id("particle_type"))
        private val PARTICLE_REGISTRY: DeferredRegister<ParticleType<*>> = DeferredRegister.create(
            PARTICLE_RESOURCE_KEY, Effective.MOD_ID)

        // Disable the syncing of the registry to prevent "unregistered object on dedicated server"
        internal val PARTICLE_REGISTRY_CLIENT: Registry<ParticleType<*>> = PARTICLE_REGISTRY.makeRegistry { builder ->
            builder.sync(false)
        }
    }

    init {
        Effective.onInitialize()
        ModAmbientConditionsForge.initialize()

        modContainer.registerConfig(ModConfig.Type.CLIENT, EffectiveConfig.configSpec)

        PARTICLE_REGISTRY.apply {
            ModParticles.PARTICLES.forEach {
                it.extra = register(it.id.path, it.type::value).id
            }
        }.register(MOD_BUS)

        DeferredRegister.create(Registries.SOUND_EVENT, Effective.MOD_ID).apply {
            ModSounds.SOUNDS.forEach {
                register(it.location.path, Supplier { it })
            }
        }.register(MOD_BUS)

        MOD_BUS.addListener { _: RegisterParticleProvidersEvent ->
            fun <O : ParticleOptions, T : ParticleType<O>> register(
                type: ResourceLocation,
                registration: ParticleEngine.SpriteParticleRegistration<O>,
            ) = (Minecraft.getInstance().particleEngine as ParticleEngineClientExtensions).`effective$register`(type,
                registration)

            ModParticles.PARTICLES.forEach {
                @Suppress("UNCHECKED_CAST") register(
                    it.extra as ResourceLocation,
                    it.provider as (SpriteSet) -> ParticleProvider<ParticleOptions>,
                )
            }
        }

//        MOD_BUS.addListener { event: RegisterShadersEvent ->
//            event.registerShader(
//                ShaderInstance(event.resourceProvider, Effective.id("soft_particle"), DefaultVertexFormat.PARTICLE),
//                SoftParticleRenderType::loadShader,
//            )
//        }

        MOD_BUS.addListener { event: RegisterShadersEvent ->
            val resourceManager = event.resourceProvider
            val ist = ModShaders2.PARTICLE.createInstance(resourceManager)
            ModShaders.PARTICLE = ist
            event.registerShader(ist) {}
        }

        MOD_BUS.addListener { event: EntityRenderersEvent.RegisterLayerDefinitions ->
            event.registerLayerDefinition(SplashModel.MODEL_LAYER, SplashModel.Companion::createBodyLayer)
            event.registerLayerDefinition(SplashBottomModel.MODEL_LAYER, SplashBottomModel.Companion::createBodyLayer)
            event.registerLayerDefinition(SplashRimModel.MODEL_LAYER, SplashRimModel.Companion::createBodyLayer)
            event.registerLayerDefinition(SplashBottomRimModel.MODEL_LAYER,
                SplashBottomRimModel.Companion::createBodyLayer)
        }

        FORGE_BUS.addListener { _: ClientTickEvent.Post ->
            CascadeManager.tick()
            ScreenShakeManager.tick()
        }

        FORGE_BUS.addListener { _: ClientPlayerNetworkEvent.LoggingOut ->
            CascadeManager.reset()
        }

        if (ModList.get().isLoaded("velvet")) {
            GlowSquidHypnosisManager.RAINBOW_SHADER // Force to load the shader

            FORGE_BUS.addListener { _: ClientTickEvent.Post ->
                GlowSquidHypnosisManager.rainbowTimer++
            }

            ShaderEffectRenderCallback.EVENT.register { tickDelta ->
                GlowSquidHypnosisManager.onShaderEffectRendered(tickDelta)
            }

            EntitiesPreRenderCallback.EVENT.register { _, _, tickDelta ->
                GlowSquidHypnosisManager.rainbowSTime.set((GlowSquidHypnosisManager.rainbowTimer + tickDelta) * 0.05F)
            }

        }
    }
}
