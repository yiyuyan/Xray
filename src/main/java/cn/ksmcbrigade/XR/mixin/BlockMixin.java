package cn.ksmcbrigade.XR.mixin;

import cn.ksmcbrigade.XR.Xray;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.InvocationTargetException;

import static cn.ksmcbrigade.XR.Xray.blocks;
import static cn.ksmcbrigade.XR.Xray.is;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(at = @At("RETURN"), method = "shouldRenderFace(" + "Lnet/minecraft/world/level/block/state/BlockState;"+
            "Lnet/minecraft/world/level/BlockGetter;" +
            "Lnet/minecraft/core/BlockPos;" +
            "Lnet/minecraft/core/Direction;" +
            "Lnet/minecraft/core/BlockPos;" +
            ")Z",
            cancellable = true)
    private static void shouldRenderFace(BlockState state, BlockGetter reader, BlockPos pos, Direction face,
                                         BlockPos blockPosaaa, CallbackInfoReturnable<Boolean> ci) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (ci == null)
            ci = new CallbackInfoReturnable<>("shouldSideBeRendered", true);

        Object o = is(I18n.get("key.xr"));
        if((o instanceof Boolean) && ((boolean) o)){
            if(!blocks.contains(Xray.getName(state.getBlock()))){
                ci.setReturnValue(false);
            }
            else{
                ci.setReturnValue(true);
            }
        }
    }
}
