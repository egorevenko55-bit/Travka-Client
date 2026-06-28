package com.travka.client.modules.render;

import com.travka.client.modules.Module;
import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {

    private float previousGamma = 1.0f;

    public FullbrightModule() {
        super("Fullbright", "Завжди максимальна яскравість", Category.RENDER);
    }

    @Override
    public void onEnable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            previousGamma = mc.options.getGamma().getValue().floatValue();
            mc.options.getGamma().setValue(16.0); // Max gamma = fullbright
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            mc.options.getGamma().setValue((double) previousGamma);
        }
    }
}
