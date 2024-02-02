package lingoncraft.client.finders;

import baritone.api.BaritoneAPI;
import com.mojang.logging.LogUtils;
import dev.langchain4j.agent.tool.Tool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;

import java.util.Optional;

public class EntityFinder {
    private static final Logger log = LogUtils.getLogger();

    @Tool
    public BlockPos findCow() {
        log.info("find cow");

        return findAnimal(CowEntity.class);
    }

    @Tool
    public BlockPos findHorse() {
        log.info("find horse");
        return findAnimal(HorseEntity.class);
    }

    @Tool
    public BlockPos findFrog() {
        log.info("find frog");
        return findAnimal(FrogEntity.class);
    }

    @Tool
    public BlockPos findGoat() {
        log.info("find goat");
        return findAnimal(GoatEntity.class);
    }

    //camel
    @Tool
    public BlockPos findCamel() {
        log.info("find camel");
        return findAnimal(CamelEntity.class);
    }

    //chicken
    @Tool
    public BlockPos findChicken() {
        log.info("find chicken");
        return findAnimal(ChickenEntity.class);
    }

    //Donkey
    @Tool
    public BlockPos findDonkey() {
        log.info("find donkey");
        return findAnimal(DonkeyEntity.class);
    }

    //Cat
    @Tool
    public BlockPos findCat() {
        log.info("find cat");
        return findAnimal(CatEntity.class);
    }

    //Fox
    @Tool
    public BlockPos findFox() {
        log.info("find fox");
        return findAnimal(FoxEntity.class);
    }

    //Llama
    @Tool
    public BlockPos findLlama() {
        log.info("find llama");
        return findAnimal(LlamaEntity.class);
    }

    //Mule
    @Tool
    public BlockPos findMule() {
        log.info("find mule");
        return findAnimal(MuleEntity.class);
    }

    //Ocelot
    @Tool
    public BlockPos findOcelot() {
        log.info("find ocelot");
        return findAnimal(OcelotEntity.class);
    }

    //Panda
    @Tool
    public BlockPos findPanda() {
        log.info("find panda");
        return findAnimal(PandaEntity.class);
    }

    //Parrot
    @Tool
    public BlockPos findParrot() {
        log.info("find parrot");
        return findAnimal(ParrotEntity.class);
    }

    //Pig
    @Tool
    public BlockPos findPig() {
        log.info("find pig");
        return findAnimal(PigEntity.class);
    }

    private BlockPos findAnimal(Class<? extends Entity> clazz) {
        log.info("findAnimal for " + clazz.getName());

        var player = BaritoneAPI.getProvider().getPrimaryBaritone().getPlayerContext().player();

        TypeFilter<Entity, ? extends Entity> filter = TypeFilter.instanceOf(clazz);

        var box = Box.of(player.getPos(), 1000, 1000, 1000);

        var entities = player.getWorld()
                .getEntitiesByType(filter, box, entity -> true);

        Optional<BlockPos> resultOpt = entities.stream()
                .map(Entity::getBlockPos)
                .findFirst();

        log.info("Found animal: " + resultOpt);
        return resultOpt.orElse(null);
    }
}
