package com.travka.client.modules.movement;

import com.travka.client.modules.Module;
import net.minecraft.client.MinecraftClient;

public class AutoSprintModule extends Module {

    public AutoSprintModule() {
        super("AutoSprint", "Автоматичний біг без затискання клавіші", Category.MOVEMENT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        // Sprint if moving forward and not sneaking
        if (client.player.forwardSpeed > 0 && !client.player.isSneaking()) {
            client.player.setSprinting(true);
        }
    }
}
