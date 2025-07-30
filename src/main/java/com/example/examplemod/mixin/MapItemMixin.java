package com.example.examplemod.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapItem.class)
public abstract class MapItemMixin extends ComplexItem {
    public MapItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void onUpdate(Level p_42894_, Entity p_42895_, MapItemSavedData p_42896_, CallbackInfo ci) {
        System.out.println("[BetterMapUpdate] MapItem.update() called");
    }
}
