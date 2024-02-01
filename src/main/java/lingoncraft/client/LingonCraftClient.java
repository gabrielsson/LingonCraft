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
            });
        });

        BaritoneAPI.getSettings().chatControl.value = false;

    }

    private static Assistant createBarritonAssistant() {
        ChatLanguageModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .maxTokens(30)
                .modelName(GPT_3_5_TURBO)
                .build();

        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();


        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        Document document = Document.document("""
                Commands in Baritone:
                - `thisway 1000` then `path` to go in the direction you're facing for a thousand blocks
                - `goal x y z` or `goal x z` or `goal y`, then `path` to set a goal to a certain coordinate then path to it
                - `goto x y z` or `goto x z` or `goto y` to go to a certain coordinate (in a single step, starts going immediately)
                - `goal` to set the goal to your player's feet
                - `goal clear` to clear the goal
                - `cancel` or `stop` to stop everything, `forcecancel` is also an option
                - `goto portal` or `goto ender_chest` or `goto block_type` to go to a block. (in Impact, `.goto` is an alias for `.b goto` for the most part)
                - `mine diamond_ore iron_ore` to mine diamond ore or iron ore (turn on the setting `legitMine` to only mine ores that it can actually see. It will explore randomly around y=11 until it finds them.) An amount of blocks can also be specified, for example, `mine 64 diamond_ore`.
                - `click` to click your destination on the screen. Right click path to on top of the block, left click to path into it (either at foot level or eye level), and left click and drag to select an area (`#help sel` to see what you can do with that selection).
                - `follow player playerName` to follow a player. `follow players` to follow any players in range (combine with Kill Aura for a fun time). `follow entities` to follow any entities. `follow entity pig` to follow entities of a specific type.
                - `wp` for waypoints. A "tag" is like "home" (created automatically on right clicking a bed) or "death" (created automatically on death) or "user" (has to be created manually). So you might want `#wp save user coolbiome`, then to set the goal `#wp goal coolbiome` then `#path` to path to it. For death, `#wp goal death` will list waypoints under the "death" tag (remember stuff is clickable!)
                - `build` to build a schematic. `build blah.schematic` will load `schematics/blah.schematic` and build it with the origin being your player feet. `build blah.schematic x y z` to set the origin. Any of those can be relative to your player (`~ 69 ~-420` would build at x=player x, y=69, z=player z-420).
                - `schematica` to build the schematic that is currently open in schematica
                - `tunnel` to dig and make a tunnel, 1x2. It will only deviate from the straight line if necessary such as to avoid lava. For a dumber tunnel that is really just cleararea, you can `tunnel 3 2 100`, to clear an area 3 high, 2 wide, and 100 deep.
                - `farm` to automatically harvest, replant, or bone meal crops. Use `farm <range>` or `farm <range> <waypoint>` to limit the max distance from the starting point or a waypoint.
                - `axis` to go to an axis or diagonal axis at y=120 (`axisHeight` is a configurable setting, defaults to 120).
                - `explore x z` to explore the world from the origin of x,z. Leave out x and z to default to player feet. This will continually path towards the closest chunk to the origin that it's never seen before. `explorefilter filter.json` with optional invert can be used to load in a list of chunks to load.
                - `invert` to invert the current goal and path. This gets as far away from it as possible, instead of as close as possible. For example, do `goal` then `invert` to run as far as possible from where you're standing at the start.
                - `come` tells Baritone to head towards your camera, useful when freecam doesn't move your player position.
                - `blacklist` will stop baritone from going to the closest block so it won't attempt to get to it.
                - `eta` to get information about the estimated time until the next segment and the goal, be aware that the ETA to your goal is really unprecise.
                - `proc` to view miscellaneous information about the process currently controlling Baritone.
                - `repack` to re-cache the chunks around you.
                - `gc` to call `System.gc()` which may free up some memory.
                - `render` to fix glitched chunk rendering without having to reload all of them.
                - `reloadall` to reload Baritone's world cache or `saveall` to save Baritone's world cache.
                - `find` to search through Baritone's cache and attempt to find the location of the block.
                - `surface` or `top` to tell Baritone to head towards the closest surface-like area, this can be the surface or highest available air space.
                - `version` to get the version of Baritone you're running
                - `damn` daniel
                """);
        ingestor.ingest(document);

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .build();


        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
                .build();

        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatModel)
                .tools(new CommandProviderWrapper.GotoChatCommand())
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    @SystemMessage({
            "You are an expert baritone prompt creator.",
            "You will give very short answers. You will not mention baritone in any way.",
            "Mostly giving back the command that sent to baritone api is enough."
    })
    interface Assistant {

        String chat(String userMessage);
    }
}
