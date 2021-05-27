package wutdahack.actuallyunbreaking.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wutdahack.actuallyunbreaking.enchantment.ModEnchantments;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {


    @Shadow public abstract void setDamage(int damage);

    @Inject(method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", cancellable = true, at = @At(value = "HEAD"))
    public void damage(int amount, Random random, @Nullable ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        // preventing damage if item has the enchantment
        int i = EnchantmentHelper.getLevel(ModEnchantments.UNBREAKING, (ItemStack) (Object) this);
        if (i > 0) {
            setDamage(-2147483648); // setting items that didn't have unbreaking before and lost durability have full durability
            cir.setReturnValue(false); // making sure that it doesn't attempt to damage
        }
    }
}