package chemical.sans.droneswarm.Formations;

import chemical.sans.TreeUtils;
import chemical.sans.droneswarm.DroneSwarm;
import org.bukkit.Location;


public class DroneFormationSphere extends DroneFormation{
    int points;
    double radius;

    public DroneFormationSphere(Location origin,int points,int radius, DroneSwarm swarm) {
        super(origin, TreeUtils.pointsOnASphere(points,radius), swarm);
        this.points = points;
        this.radius = radius;
    }

    public void add(int i) {
        points += i;
        this.setFormation(TreeUtils.pointsOnASphere(points,radius));
    }

    public void remove(int i) {
        points -= i;
        this.setFormation(TreeUtils.pointsOnASphere(points,radius));
    }
}
