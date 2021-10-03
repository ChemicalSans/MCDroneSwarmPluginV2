package chemical.sans;

import chemical.sans.droneswarm.DroneSwarm;
import chemical.sans.droneswarm.Formations.DroneFormationSphere;
import chemical.sans.events.EventListener;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin implements Listener {
    public static MainPlugin plugin;
    public static DroneSwarm mainDroneSwarm;
    public static DroneFormationSphere mainDroneFormationSphere;

    @Override
    public void onEnable() {
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new EventListener(),this);



        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for(World world : getServer().getWorlds()) {
                for(Entity entity : world.getEntities()) {
                    if(entity instanceof  ArmorStand) {
                        ArmorStand armorStand = (ArmorStand) entity;
                        if(armorStand.getCustomName().equals("delete")) {
                            armorStand.remove();
                        }
                    }
                }
            }


                    mainDroneSwarm = new DroneSwarm(new Location(getServer().getWorld("world"),0, 100, 0),1000);
        }
        );
    }

    @Override
    public void onDisable() {
        mainDroneSwarm.delete();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (label) {
                case "test":
                    player.sendMessage("Test Command!");


                    if(mainDroneFormationSphere == null) {
                        mainDroneFormationSphere = new DroneFormationSphere(player.getLocation(), 50, 2, mainDroneSwarm);
                        mainDroneFormationSphere.setDisplayItem(new ItemStack(Material.DIAMOND_BLOCK));
                    }

                    mainDroneFormationSphere.setPlayer(player);

                    if(args.length == 1) {
                        int arg0 = Integer.valueOf(args[0]);

                        mainDroneFormationSphere.add(arg0);
                    }



                    break;
                case "drone":
                    player.sendMessage("drone Command!");
                    if(args.length == 0) {
                        mainDroneSwarm.deleteAllDroneGoals();
                    } else {
                        int i = Integer.valueOf(args[0]);
                        mainDroneSwarm.addDrones(player.getLocation(),i);
                    }
                    break;
                default:
                    player.sendMessage("Unknown Command!");
                    break;
            }

        }

        return false;
    }


    public void broadcast(String massage) {
        for(Player p : getServer().getOnlinePlayers()) {
            p.sendMessage(massage);
        }
    }

    public void printStackTrace(Exception e) {
        try {
            broadcast("§cCause: " + e.getCause().toString());
        } catch (Exception exception) { }
        for(StackTraceElement traceElement : e.getStackTrace()) {
            try {
                broadcast("§c       " + traceElement.toString());
            } catch (Exception exception) { }
        }

    }


}
