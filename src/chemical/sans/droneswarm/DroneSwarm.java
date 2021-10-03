package chemical.sans.droneswarm;

import chemical.sans.TreeUtils;
import chemical.sans.droneswarm.Formations.DroneFormation;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Vector;

import static chemical.sans.MainPlugin.plugin;

public class DroneSwarm extends BukkitRunnable {
    public Vector<Drone> drones = new Vector<>();
    public Vector<DroneGoal> droneGoals = new Vector<>();
    public Vector<DroneFormation> droneFormations = new Vector<>();

    Location origin;

    Vector<Drone> freeDrones = new Vector<>();
    Vector<Location> parkingLocations = new Vector<>();

    public DroneSwarm(Location origin, int drone) {
        this.origin = origin;

        for(int i = 0; i < drone; i++) {this.addDrone(new Drone(origin,this));}

        this.runTaskTimer(plugin,0,1);
    }

    public void updateParkingDrones() {
        Vector<org.bukkit.util.Vector> offsets = TreeUtils.pointsOnASphere(freeDrones.size(),10);

        for(int i = 0;i < offsets.size(); i++) {
            org.bukkit.util.Vector offset = offsets.get(i);
            Drone drone = freeDrones.get(i);
            Location offsetLocation = origin.clone().add(offset);
            Location parkingLocation;
            try {
                parkingLocation = parkingLocations.get(i);
                parkingLocation.setX(offsetLocation.getX());
                parkingLocation.setY(offsetLocation.getY());
                parkingLocation.setZ(offsetLocation.getZ());
                parkingLocation.setWorld(offsetLocation.getWorld());
                parkingLocation.setDirection(TreeUtils.lookingAtDir(drone.location.toVector(),origin.toVector()));
            } catch (Exception e) {
                parkingLocation = origin.clone().add(offset);
                parkingLocations.add(parkingLocation);
            }

            drone.setParkingLocation(parkingLocation);
        }

    }



    public void deleteAllDroneGoals() {
        while (!droneGoals.isEmpty()) {
            droneGoals.get(0).delete();
        }
    }

    public Drone getDrone() {
        if(freeDrones.isEmpty()) return null;
        Drone drone = freeDrones.remove(freeDrones.size()-1);
        updateParkingDrones();
        return drone;
    }

    public void addDrone(Drone drone) {
        drone.reset();
        freeDrones.add(drone);
        updateParkingDrones();
    }

    public void addDrone(Location location) {
        this.addDrone(new Drone(location,this));
    }

    public void addDrones(Location location,int amount) {
        for(int i = 0;i < amount; i++) {
            this.addDrone(new Drone(location,this));
        }
    }

    public void delete() {
        for(DroneGoal droneGoal : droneGoals) droneGoal.delete();
        for(Drone drone : drones) drone.delete();
        this.cancel();
    }

    @Override
    public void run() {



    }
}
