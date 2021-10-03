package chemical.sans.droneswarm.Formations;

import chemical.sans.MainPlugin;
import chemical.sans.droneswarm.Drone;
import chemical.sans.droneswarm.DroneGoal;
import chemical.sans.droneswarm.DroneSwarm;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Vector;

public class DroneFormation extends BukkitRunnable {
    public Player player;
    public Location origin;
    public DroneSwarm swarm;
    public Vector<org.bukkit.util.Vector> offsets;
    public Vector<DroneGoal> droneGoals = new Vector<>();
    public ItemStack displayItem;

    public DroneFormation(Location origin , Vector<org.bukkit.util.Vector> offsets, DroneSwarm swarm){
        this.swarm = swarm;
        this.origin = origin;
        this.offsets = offsets;
        this.swarm.droneFormations.add(this);

        DroneGoal droneGoal;
        for(org.bukkit.util.Vector offset : offsets) {
            droneGoal = new DroneGoal(origin.clone().add(offset),swarm);
            droneGoal.setDroneFormation(this);
            droneGoals.add(droneGoal);
        }

        this.runTaskTimer(MainPlugin.plugin,0,1);
    }

    public void delete() {
        this.swarm.droneFormations.remove(this);
        for(DroneGoal droneGoal : droneGoals) {
            droneGoal.delete();
        }
        this.cancel();
    }

    public void setPlayer(Player player) {
        this.player = player;

    }

    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }

    public void setFormation(Vector<org.bukkit.util.Vector> offsets) {
        this.offsets = offsets;

        if(offsets.size()< droneGoals.size()) {
            int diff = droneGoals.size() - offsets.size();

            for(int i = 0; i < diff; i++) {
                DroneGoal droneGoal = droneGoals.remove(droneGoals.size()-1);
                droneGoal.delete();
            }
        }

        for(int i = 0;i < offsets.size();i++) {
            org.bukkit.util.Vector offset = offsets.get(i);
            Location location = origin.clone().add(offset);
            DroneGoal droneGoal;
            try {
                droneGoal = droneGoals.get(i);
            } catch (Exception e) {
                droneGoal = new DroneGoal(location,swarm);
                droneGoals.add(droneGoal);
            }

            droneGoal.setLocation(location);
        }

    }

    @Override
    public void run() {
        if(displayItem != null) {
            for(DroneGoal droneGoal : droneGoals) {
                if(droneGoal.hasDrone()) {
                    Drone drone = droneGoal.getDrone();
                    if(drone.getDisplayItem()!=displayItem) {
                        drone.setDisplayItem(displayItem);
                    }
                }
            }
        }

        if(player != null) {
            this.origin = player.getLocation().clone().add(0,1.7,0);
            for(DroneGoal droneGoal : droneGoals) {
                if(droneGoal.getDrone()!=null) {
                    droneGoal.getDrone().setSpeed(0.7);
                }
            }
        }

        for(int i = 0; i < droneGoals.size(); i++) {
            DroneGoal droneGoal = droneGoals.get(i);
            org.bukkit.util.Vector offset = offsets.get(i);

            droneGoal.setLocation(origin.clone().add(offset));
        }
    }
}
