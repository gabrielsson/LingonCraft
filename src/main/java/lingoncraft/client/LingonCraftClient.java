package lingoncraft.client;

import baritone.api.BaritoneAPI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

public class LingonCraftClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        ServerMessageEvents.CHAT_MESSAGE.register((client, message, sender) -> {
            System.out.println("Received message from server: " + message);
            //bariton move 10 10
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("goto 10 10");
        });
    }
}
