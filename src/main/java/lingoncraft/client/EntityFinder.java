package lingoncraft.client;

import baritone.api.BaritoneAPI;
import com.mojang.logging.LogUtils;
import dev.langchain4j.agent.tool.Tool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;

import java.util.Optional;

public class EntityFinder {
    private static final Logger LOGGER = LogUtils.getLogger();


    @Tool({"Get x z of nearest cow"})
    public EntityPosition findCow() {
        LOGGER.info("find cow");

        return findAnimal(CowEntity.class);
    }

    @Tool
    public EntityPosition findHorse() {
        LOGGER.info("find horse");
        return findAnimal(HorseEntity.class);
    }

    @Tool
    public EntityPosition findFrog() {
        LOGGER.info("find frog");
        return findAnimal(FrogEntity.class);
    }

    @Tool
    public EntityPosition findGoat() {
        LOGGER.info("find goat");
        return findAnimal(GoatEntity.class);
    }

    //camel
    @Tool
    public EntityPosition findCamel() {
        LOGGER.info("find camel");
        return findAnimal(CamelEntity.class);
    }

    //chicken
    @Tool
    public EntityPosition findChicken() {
        LOGGER.info("find chicken");
        return findAnimal(ChickenEntity.class);
    }

    //Donkey
    @Tool
    public EntityPosition findDonkey() {
        LOGGER.info("find donkey");
        return findAnimal(DonkeyEntity.class);
    }

    //Cat
    @Tool
    public EntityPosition findCat() {
        LOGGER.info("find cat");
        return findAnimal(CatEntity.class);
    }

    //Fox
    @Tool
    public EntityPosition findFox() {
        LOGGER.info("find fox");
        return findAnimal(FoxEntity.class);
    }

    //Llama
    @Tool
    public EntityPosition findLlama() {
        LOGGER.info("find llama");
        return findAnimal(LlamaEntity.class);
    }

    //Mule
    @Tool
    public EntityPosition findMule() {
        LOGGER.info("find mule");
        return findAnimal(MuleEntity.class);
    }

    //Ocelot
    @Tool
    public EntityPosition findOcelot() {
        LOGGER.info("find ocelot");
        return findAnimal(OcelotEntity.class);
    }

    //Panda
    @Tool
    public EntityPosition findPanda() {
        LOGGER.info("find panda");
        return findAnimal(PandaEntity.class);
    }

    //Parrot
    @Tool
    public EntityPosition findParrot() {
        LOGGER.info("find parrot");
        return findAnimal(ParrotEntity.class);
    }

    //Pig
    @Tool
    public EntityPosition findPig() {
        LOGGER.info("find pig");
        return findAnimal(PigEntity.class);
    }

    private  EntityPosition findAnimal(Class<? extends Entity> clazz) {
        LOGGER.info("findAnimal for " + clazz.getName());

        var player = BaritoneAPI.getProvider().getPrimaryBaritone().getPlayerContext().player();

        TypeFilter<Entity, ? extends Entity> filter = TypeFilter.instanceOf(clazz);

        var box = Box.of(player.getPos(), 1000, 1000, 1000);

        var entities = player.getWorld()
                .getEntitiesByType(filter, box, entity -> true);

        Optional<EntityPosition> resultOpt = entities.stream()
                .map(entity -> new EntityPosition(entity.getX(), entity.getZ()))
                .findFirst();

        LOGGER.info("Found animal: " + resultOpt);
        return resultOpt.orElse(null);
    }
}
