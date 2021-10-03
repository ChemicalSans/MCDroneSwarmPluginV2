package chemical.sans.droneswarm;

import chemical.sans.MainPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Drone extends BukkitRunnable {
    public static final double default_Speed = 0.2;
    public static final ItemStack default_DisplayItem = new ItemStack(Material.GOLD_BLOCK);

    DroneSwarm swarm;
    Location location;
    Location moveTo;
    double speed = default_Speed;
    ArmorStand display;
    ItemStack displayItem;

    DroneGoal droneGoal;
    Location parkingLocation;
    boolean occupied;


    public Drone(Location location, DroneSwarm swarm) {
        this.location = location;
        this.moveTo = location;
        //this.parkingLocation = location;
        this.swarm = swarm;
        this.occupied = false;
        this.swarm.drones.add(this);

        this.display = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        this.displayItem = default_DisplayItem;
        this.display.setHelmet(displayItem);
        display.setGravity(false);
        display.setSmall(true);
        display.setInvisible(true);
        display.setMarker(true);
        display.setBasePlate(false);
        display.setCustomName("delete");

        //MainPlugin.plugin.broadcast("DRONE[] new");
        this.runTaskTimer(MainPlugin.plugin,0,1);
    }

    public void delete() {
        this.swarm.drones.remove(this);
        this.cancel();
    }

    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
        this.display.setHelmet(displayItem);
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public void setParkingLocation(Location parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Location getLocation() {
        return location;
    }

    public void setDroneGoal(DroneGoal droneGoal) {
        this.droneGoal = droneGoal;
    }

    public void reset() {
        this.droneGoal = null;
        this.occupied = false;
        this.speed = default_Speed;
        this.setDisplayItem(Drone.default_DisplayItem);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void run() {
        //Movement
        if(parkingLocation != null) {
            moveTo = parkingLocation.clone();

            //Particle.DustOptions dustOptions = new Particle.DustOptions(Color.AQUA, 0.6f);
            //this.parkingLocation.getWorld().spawnParticle(Particle.REDSTONE,this.parkingLocation,1,dustOptions);
        } else {
            MainPlugin.plugin.broadcast("parkingLocation == null");
        }

        if(droneGoal !=  null) {
            moveTo = droneGoal.getLocation().clone();
        }

        try {
            Vector diff = moveTo.clone().toVector().subtract(location.clone().toVector());
            this.location = this.location.clone().add(diff.multiply(speed));

            this.display.teleport(this.location.clone().add(0,-1,0));
            this.display.setRotation(this.location.getPitch(),this.location.getYaw());

        } catch (Exception e) {
            MainPlugin.plugin.broadcast("MoveTo == null");
        }


        //Color
        /*
        Color color;
        if(this.isOccupied()) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 0.4f);
        this.location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, dustOptions);

         */

    }
}
