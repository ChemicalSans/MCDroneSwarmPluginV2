package chemical.sans;

import org.bukkit.Location;

import java.util.Vector;

public class TreeUtils {
    public static Location addOffset(Location l,int x,int y,int z) {
        return new Location(l.getWorld(),l.getX()+x,l.getY()+y,l.getZ()+z);
    }

    public static Location addOffset(Location l,double x,double y,double z) {
        return new Location(l.getWorld(),l.getX()+x,l.getY()+y,l.getZ()+z);
    }

    public static void main(String[] args) {for(org.bukkit.util.Vector vec : pointsOnASphere(120,5)) {System.out.println("Vec: " + vec);}}
    public static Vector<org.bukkit.util.Vector> pointsOnASphere(int points, double radius) {
        /**
         *

         points = []
         phi = math.pi * (3. - math.sqrt(5.))  # golden angle in radians

         for i in range(samples):
         y = 1 - (i / float(samples - 1)) * 2  # y goes from 1 to -1
         radius = math.sqrt(1 - y * y)  # radius at y

         theta = phi * i  # golden angle increment

         x = math.cos(theta) * radius
         z = math.sin(theta) * radius

         points.append((x, y, z))

         */
        Vector<org.bukkit.util.Vector> ret = new Vector<>();


        double phi = Math.PI * (3d - Math.sqrt(5d));

        for(double i = 0; i < points; i++) {
            double y = 1d - (i / (points - 1d)) * 2d;  // y goes from 1 to -1
            double r = Math.sqrt(1 - y * y);        // radius at y
            double theta = phi * i;                  // golden angle increment

            double x = Math.cos(theta) * r;
            double z = Math.sin(theta) * r;

            ret.add(new org.bukkit.util.Vector(x*radius,y*radius,z*radius));
        }

        return ret;
    }

    public static Vector<org.bukkit.util.Vector> rotatePoints(Vector<org.bukkit.util.Vector> in, org.bukkit.util.Vector origin, double angle) {
        for(org.bukkit.util.Vector pivot : in) {
            pivot.setX(origin.getX() + Math.cos(angle) * (pivot.getX() - origin.getX()) - Math.sin(angle) * (pivot.getY() - origin.getY()));
            pivot.setY(origin.getY() + Math.sin(angle) * (pivot.getX() - origin.getX()) + Math.cos(angle) * (pivot.getY() - origin.getY()));
        }

        return in;
    }

    public static Location lookingAtDir(Location a, Location b) {
        org.bukkit.util.Vector vec = lookingAtDir(a.toVector(),b.toVector());
        return new Location(a.getWorld(),vec.getX(),vec.getY(),vec.getZ());
    }

    public static org.bukkit.util.Vector lookingAtDir(org.bukkit.util.Vector a, org.bukkit.util.Vector b) {
        return a.clone().subtract(b.clone()).normalize();
    }


}
