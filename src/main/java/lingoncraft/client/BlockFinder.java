package lingoncraft.client;

import baritone.api.BaritoneAPI;
import com.mojang.logging.LogUtils;
import dev.langchain4j.agent.tool.Tool;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;

public class BlockFinder {

    private final Logger log = LogUtils.getLogger();

    // Translated method to find a block by its string ID
    private Block findBlockFromString(String blockId) {
        Identifier id = new Identifier(blockId);
        Block block = Registries.BLOCK.get(id);
        if (block != Blocks.AIR) {
            return block;
        } else {
            System.out.println("Block not found for ID: " + blockId);
            return null;
        }
    }

    @Tool("Check if a given material is available nearby. material are minecraft identifiers such as minecraft:gold_ore or minecraft:diamond_ore")
    public BlockPos findMaterial(String material) {
        log.info("find material: " + material);
        var player = BaritoneAPI.getProvider().getPrimaryBaritone().getPlayerContext();

        int maxItems = 1;
        int maxYDiff = 10;
        int radius = 100; // Define the search radius
        Block blockToFind = findBlockFromString(material);
        if (blockToFind != null) {
            var foundBlocks = BaritoneAPI.getProvider().getWorldScanner().scanChunkRadius(player, java.util.List.of(blockToFind), maxItems, maxYDiff, radius);
            if (!foundBlocks.isEmpty()) {
                return foundBlocks.iterator().next();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    @Tool("Start mining materials. material can be gold, iron or diamond")
    public void mineMaterial(String material) {
        log.info("mine " + material);
        BaritoneAPI.getProvider().getPrimaryBaritone().getMineProcess().mineByName(1, "minecraft:" + material + "_ore");
    }

    @Tool("Stop mining materials")
    public void stopMining() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getMineProcess().cancel();
    }
}