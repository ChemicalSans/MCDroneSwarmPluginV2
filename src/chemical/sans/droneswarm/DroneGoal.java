package chemical.sans.droneswarm;

import chemical.sans.MainPlugin;
import chemical.sans.droneswarm.Formations.DroneFormation;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class DroneGoal extends BukkitRunnable {
    Location location;
    DroneSwarm swarm;
    boolean occupied;
    Drone drone;

    DroneFormation droneFormation;
    boolean selfDelete = false;


    public DroneGoal(Location location, DroneSwarm swarm) {
        this.location = location;
        this.swarm = swarm;
        this.swarm.droneGoals.add(this);
        this.occupied = false;

        this.runTaskTimer(MainPlugin.plugin,0,1);
    }

    public void setDroneFormation(DroneFormation droneFormation) {
        this.droneFormation = droneFormation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setSelfDelete(boolean selfDelete) {
        this.selfDelete = selfDelete;
    }

    public boolean hasDrone() {
        if(drone != null) return true;
        return false;
    }

    public void delete() {
        if(this.hasDrone()) {
            drone.reset();
        }

        if(droneFormation != null) {
            droneFormation.droneGoals.remove(this);
        }

        if(drone != null) {
            swarm.addDrone(drone);
        }

        this.swarm.droneGoals.remove(this);
        this.cancel();
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Drone getDrone() {
        return drone;
    }

    @Override
    public void run() {
        if(drone == null) {
            drone = swarm.getDrone();
        }
        if(drone != null) {
            drone.setDroneGoal(this);
            drone.setOccupied(true);
        }

        if(selfDelete && location.distance(drone.getLocation())<0.1) this.delete();


        /*
        Color color;
        if(occupied) {
            color = Color.GRAY;
        } else {
            color = Color.ORANGE;
        }
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 0.4f);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, dustOptions);

         */
    }


}
