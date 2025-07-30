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
    private static int updateCallCount = 0;
    private static long lastPrintTime = 0;
    private static long totalUpdateTime = 0;
    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    public MapItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void onUpdateStart(Level p_42894_, Entity p_42895_, MapItemSavedData p_42896_, CallbackInfo ci) {
        startTime.set(System.nanoTime());
    }

    @Inject(method = "update", at = @At("RETURN"))
    private void onUpdateEnd(Level p_42894_, Entity p_42895_, MapItemSavedData p_42896_, CallbackInfo ci) {
        // Get elapsed time and update total time
        long end = System.nanoTime();
        Long start = startTime.get();
        if (start != null) {
            totalUpdateTime += (end - start);
        }

        // Update call count
        updateCallCount++;

        // Print every sec
        long now = System.currentTimeMillis();
        if (now - lastPrintTime >= 1000) {
            double avgMicros = updateCallCount == 0 ? 0 : (totalUpdateTime / 1000.0) / updateCallCount;
            
            if (avgMicros > 1000) {
                System.out.println(String.format("[BetterMapUpdate] MapItem.update() LONG: %d in 1s, avg %.0f us", updateCallCount, avgMicros));
            } else {
                System.out.println(String.format("[BetterMapUpdate] MapItem.update(): %d in 1s, avg %.2f us", updateCallCount, avgMicros));
            }

            updateCallCount = 0;
            totalUpdateTime = 0;
            lastPrintTime = now;
        }
    }
}
