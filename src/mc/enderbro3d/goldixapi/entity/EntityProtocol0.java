package mc.enderbro3d.goldixapi.entity;

import mc.enderbro3d.goldixapi.wrapper.WrapperPlayServerNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.EntitySheep;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class EntityProtocol0 {
    private WrapperPlayServerNamedEntitySpawn packet;

    private LivingEntity entity = null;




    public EntityProtocol0(World world) {

        entity = world.spawnCreature(new Location(world, 0, 256, 0), EntityType.PLAYER);
    }


    public void buildPacket() {
        packet = new WrapperPlayServerNamedEntitySpawn();
        packet.setEntityID(entity.getEntityId());
    }
}
