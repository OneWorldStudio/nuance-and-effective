package com.oneworldstuddo.effective.mixin.chorus;

import com.oneworldstuddo.effective.EffectiveConfig;
import com.oneworldstuddo.effective.registry.ModParticles;
import com.oneworldstuddo.effective.util.RandomSourceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChorusFlowerBlock.class)
public class ChorusPetalSpawnMixin extends ChorusFlowerBlockMixin {

    @Override
    protected void effective$afterAnimateTick(
        BlockState state, Level level, BlockPos pos, RandomSource random,
        CallbackInfo ci
    ) {
        int density = (int) ((6 - state.getValue(ChorusFlowerBlock.AGE)) * EffectiveConfig.chorusPetalDensity.get());
        for (int i = 0; i < density; i++) {
            level.addParticle(
                ModParticles.INSTANCE.getCHORUS_PETAL(),
                true,
                pos.getX() + 0.5 + RandomSourceUtils.nextDoubleOrNegative(random) * 5,
                pos.getY() + 0.5 + RandomSourceUtils.nextDoubleOrNegative(random) * 5,
                pos.getZ() + 0.5 + RandomSourceUtils.nextDoubleOrNegative(random) * 5,
                0.0,
                0.0,
                0.0
            );
        }
    }

}
