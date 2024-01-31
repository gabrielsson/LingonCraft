package lingoncraft.client;

import baritone.api.BaritoneAPI;
import com.mojang.logging.LogUtils;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;

public class CommandProviderWrapper {

    private static final Logger LOGGER = LogUtils.getLogger();

    static class GotoChatCommand {
        @Tool("execute baritone commands like `command arg1 arg2`")
        private void baritoneRaw(String command) {
            LOGGER.info("Executing command: " + command);


            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(command);
        }

    }


}
