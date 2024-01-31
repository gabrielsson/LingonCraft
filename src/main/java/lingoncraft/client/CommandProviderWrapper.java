package lingoncraft.client;

import com.mojang.logging.LogUtils;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;

public class CommandProviderWrapper {

    private static final Logger LOGGER = LogUtils.getLogger();

    static class GotoChatCommand {

        private void move_To_Coordinates(int x, int y, int z) {
            LOGGER.info("Moving to coordinates: " + x + " " + y + " " + z);


            baritone.api.BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("goto " + x + " " + y + (z > 0 ? " " + z : ""));
        }

        @Tool("Moves the user to provided coordinates")
        public void move_To_Coordinates(int x, int y) {
            move_To_Coordinates(x, y, -1);
        }
    }


}
