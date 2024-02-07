package lingoncraft.client;

import baritone.api.BaritoneAPI;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lingoncraft.client.BlockPos.BlockPosCreator;
import lingoncraft.client.finders.BlockFinder;
import lingoncraft.client.finders.EntityFinder;
import lingoncraft.client.finders.PlayerFinder;
import lingoncraft.client.walker.PlayerWalker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;

public class LingonCraftClient implements ClientModInitializer {
    private Assistant assistant;

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {

        assistant = createBarritonAssistant();

        ServerMessageEvents.CHAT_MESSAGE.register((event, player, parameters) -> {
            System.out.println("Received message from server: " + event.getContent().getString());

            CompletableFuture<String> response = CompletableFuture.supplyAsync(() -> assistant.chat(event.getContent().getString()));
            response.thenAccept((result) -> {
                        Arrays.stream(result.split("\n"))
                                .forEach(m -> player.sendMessage(Text.of(m), false));
                    })
                    .exceptionally((throwable) -> {
                        throwable.printStackTrace();
                        Arrays.stream(throwable.getMessage().split("\n"))
                                .forEach(m -> player.sendMessage(Text.of(m), false));
                        assistant = createBarritonAssistant();
                        return null;
                    });
        });

        BaritoneAPI.getSettings().chatControl.value = false;
        BaritoneAPI.getSettings().freeLook.value = false;
        BaritoneAPI.getSettings().smoothLook.value = true;


    }

    private static Assistant createBarritonAssistant() {
        ChatLanguageModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .maxTokens(300)
                .modelName(GPT_3_5_TURBO)
                .build();

        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatModel)
                .tools(new PlayerFinder(), new BlockFinder(), new EntityFinder(), new PlayerWalker(), new Stopper())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    @SystemMessage({
            "You are an expert baritone prompt creator.",
            "Before executing commands, you MUST always check:",
            "the player position.",
            "Keep track of the old positions of the player.",
            "You will give very short answers. You will not mention baritone in any way.",
            "Mostly telling which tool is used is enough.",
    })
    interface Assistant {

        String chat(String userMessage);
    }
}
