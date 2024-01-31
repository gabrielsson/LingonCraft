package lingoncraft.client;

import baritone.api.BaritoneAPI;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

public class LingonCraftClient implements ClientModInitializer {
    private Assistant assistant;
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {

        assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey("demo"))
                .tools(new CommandProviderWrapper.GotoChatCommand())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();


        ServerMessageEvents.CHAT_MESSAGE.register((event ,player, parameters) -> {

            System.out.println("Received message from server: " + event.getContent().getString());
            //bariton move 10 10

            System.out.println(assistant.chat(event.getContent().getString()));
        });



    }

    interface Assistant {

        String chat(String userMessage);
    }
}
