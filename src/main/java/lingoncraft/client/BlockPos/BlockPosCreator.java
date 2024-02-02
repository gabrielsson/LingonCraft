package lingoncraft.client.BlockPos;

import dev.langchain4j.agent.tool.Tool;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockPosCreator {
    private static final Logger log = LoggerFactory.getLogger(lingoncraft.client.BlockPos.BlockPosCreator.class);

    @Tool({"Create BlockPos object from coordinates."})
    public BlockPos createBlockPos(int x, int y, int z) {
        log.info("createBlockPos from %d, %d, %d".formatted(x, y, z));
        return new BlockPos(x, y, z);
    }

}
