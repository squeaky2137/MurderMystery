package me.squeaky2137.spigotmurdermystery.GameManagement;

import me.squeaky2137.spigotmurdermystery.Commands;
import me.squeaky2137.spigotmurdermystery.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Game {

    private static Player sheriff;
    private static Player killer;
    private static Player hero;
    private static BukkitTask heroarrow;
    private static BukkitTask goldtimer;
    private static BukkitTask compassloop;
    private static BukkitTask Endgametimer;
    private static BukkitTask Gameend;
    private static BukkitTask Bossbar;
    private static List<Player> Villagers;
    private static ArmorStand gunholo;
    private static List<ArmorStand> playerDeath = new ArrayList<>();
    private static List<Player> KillerChance = new ArrayList<>();
    private static List<Player> SheriffChance = new ArrayList<>();
    private static boolean thrown = false;
    private int timer;
    private static double[][] RandomSpawns = {
            {-10016.00, 105.00, 0010158.00},
            {-10097.00, 00104.00, 0010160.00},
            {-10061.00, 00108.00, 0010086.00},
            {-9917.00, 00104.00, 0010098.00},
            {-9900.00, 00104.00, 0010094.00},
            {-9900.00, 00104.00, 0010118.00},
            {-10119.00, 00104.00, 0010010.00},
            {-10019.00, 00104.00, 0010007.00},
            {-9947.00, 00104.00, 0010006.00},
            {-9934.00, 00104.00, 009996.00},
            {-9897.00, 00104.00, 0010163.00},
            {-9980.00, 00104.00, 0010161.00},
            {-9925.00, 00104.00, 0010184.00},
            {-10011.00, 00110.00, 0010088.00},
            {-10002.00, 105.00, 10162.00},
            {-10011.00, 105.00, 10169.00},
            {-10007.00, 111.00, 10164.00},
            {-10027.00, 111.00, 10168.00},
            {-10029.00, 105.00, 10166.00},
    };

    private static double[][] CoinOnly = {
            {-10016.00, 105.00, 0010158.00},
            {-10097.00, 00104.00, 0010160.00},
            {-10061.00, 00108.00, 0010086.00},
            {-9917.00, 00104.00, 0010098.00},
            {-9900.00, 00104.00, 0010094.00},
            {-9900.00, 00104.00, 0010118.00},
            {-10119.00, 00104.00, 0010010.00},
            {-10019.00, 00104.00, 0010007.00},
            {-9947.00, 00104.00, 0010006.00},
            {-9934.00, 00104.00, 009996.00},
            {-9897.00, 00104.00, 0010163.00},
            {-9980.00, 00104.00, 0010161.00},
            {-9925.00, 00104.00, 0010184.00},
            {-10011.00, 00110.00, 0010088.00},
            {-10002.00, 105.00, 10162.00},
            {-10011.00, 105.00, 10169.00},
            {-10007.00, 111.00, 10164.00},
            {-10027.00, 111.00, 10168.00},
            {-10029.00, 105.00, 10166.00},
            {-10023.00, 105.00, 10169.00},
            {-10021.00, 106.00, 10161.00},
            {-10017.00, 108.00, 10170.00},
            {-10022.00, 111.00, 10162.00},
            {-10030.00, 112.00, 10162.00},
            {-10022.00, 111.00, 10169.00},
            {-10008.00, 112.00, 10170.00},
            {-10016.00, 111.00, 10163.00},
            {-10059.00, 00108.00, 0010087.00},
            {-9922.00, 00105.00, 0010000.00},
            {-9927.00, 00105.00, 009994.00},
            {-10079.00, 00104.00, 0010170.00}
    };

    public Game() {
        //get killer and sheriff
        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if(KillerChance.size() <= 0) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                KillerChance.add(player);
                KillerChance.add(player);
                KillerChance.add(player);
                KillerChance.add(player);
                KillerChance.add(player);
            }
        }
        killer = KillerChance.get(new Random().nextInt(KillerChance.size()));
        killer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);

        if(SheriffChance.size() <= 0) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                SheriffChance.add(player);
                SheriffChance.add(player);
                SheriffChance.add(player);
                SheriffChance.add(player);
                SheriffChance.add(player);
            }
        }

        int killerinsher = 0;
        for(Player pl : SheriffChance) {
            if(pl.getName().equals(killer.getName())) {
                killerinsher++;
            }
        }
         SheriffChance.removeAll(Collections.singleton(killer));
        sheriff = SheriffChance.get(new Random().nextInt(SheriffChance.size()));
        players.remove(killer);
        Villagers = players;
        hero = sheriff;
        timer = 0;
        List<double[]> clonedSpawns = new LinkedList<>(Arrays.asList(RandomSpawns));
        BukkitTask CountDown = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.RED + "The Murderer will receive their weapon in " + (5-timer));
                timer++;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                sheriff.getInventory().setItem(0, new ItemStack(Material.BOW, 1));
                killer.getInventory().setItem(1, new ItemStack(Material.IRON_SWORD, 1));
                CountDown.cancel();
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "The Murderer has received their weapon");
            }
        }.runTaskLater(Main.getPlugin(), 100);


        //Game ends in 15 mins
        Gameend = new BukkitRunnable() {
            @Override
            public void run() {
                Game.endGame("The Villagers Have Won!");
            }
        }.runTaskLater(Main.getPlugin(), 18000);
        Bukkit.createBossBar(new NamespacedKey(Main.getPlugin(), "Timer"), "15 minutes ", BarColor.BLUE, BarStyle.SOLID);
        Bukkit.getBossBar(new NamespacedKey(Main.getPlugin(), "Timer")).setVisible(true);
        Bukkit.getBossBar(new NamespacedKey(Main.getPlugin(), "Timer")).setProgress(1);
        for(Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getBossBar(new NamespacedKey(Main.getPlugin(), "Timer")).addPlayer(player);
        }
        Bossbar = new BukkitRunnable() {
            int time = 0;
            int sec = 60;
            @Override
            public void run() {
                sec--;
                Bukkit.getBossBar(new NamespacedKey(Main.getPlugin(), "Timer")).setProgress(Bukkit.getBossBar(new NamespacedKey(Main.getPlugin(), "Timer")).getProgress() - (1/900));
                Bukkit.getBossBar(new NamespacedKey(Main.getPlugin(), "Timer")).setTitle(14-time + " mins " + sec +  " remaining");
                if(sec <= 0) {
                    sec = 60;
                    time++;
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
        //Give Killer compass at 5 mins
        Endgametimer = new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack compass = new ItemStack(Material.COMPASS, 1);
                ItemMeta compassMeta = compass.getItemMeta();
                compassMeta.setDisplayName(ChatColor.RED + "Killer's Compass");
                compass.setItemMeta(compassMeta);
                killer.getInventory().setItem(4, compass);
                System.out.println("Killer's compass given");
                Bukkit.broadcastMessage(ChatColor.RED + "The Murderer is now able to track players");

                compassloop =new BukkitRunnable() {
                    @Override
                    public void run() {
                        Player nearestplayer = Villagers.get(new Random().nextInt(0, Villagers.size()));
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if(!player.getName().equals(nearestplayer.getName()) && !player.getName().equals(killer.getName())) {
                                if (player.getLocation().distance(killer.getLocation()) < nearestplayer.getLocation().distance(killer.getLocation())) {
                                    nearestplayer = player;
                                }
                            }
                        }
                        killer.setCompassTarget(nearestplayer.getLocation());
                        killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "You are tracking " + nearestplayer.getName() + ". " + ChatColor.RED + "You are " + Math.floor(killer.getLocation().distance(nearestplayer.getLocation())) + " blocks away"));
                    }
                }.runTaskTimer(Main.getPlugin(), 0, 5);
            }
        }.runTaskLater(Main.getPlugin(), 6000);


        //Display Title
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.getName().equals(killer.getName())) {
                player.sendTitle(ChatColor.DARK_RED + "You are the Killer",  ChatColor.RED + "You must kill everyone without being detected...", 10, 50, 10);
            } else if(player.getName().equals(sheriff.getName())) {
                player.sendTitle(ChatColor.DARK_BLUE + "You are the Sheriff", ChatColor.BLUE + "You must slay the killer...", 10, 50, 10);
            } else {
                player.sendTitle(ChatColor.DARK_GREEN + "You are a Villager", ChatColor.GREEN + "You must survive and find the killer...", 10, 50, 10);
            }

            double[] RandomSpot = clonedSpawns.get(new Random().nextInt(0, clonedSpawns.size()));
            player.teleport(new Location(Bukkit.getWorld("world"), RandomSpot[0], RandomSpot[1], RandomSpot[2]));
            clonedSpawns.remove(RandomSpot);
        }

        heroarrow = new BukkitRunnable() {
            @Override
            public void run() {
                if(hero != null) {
                    hero.getInventory().setItem(7, new ItemStack(Material.ARROW, 1));
                }
            }
        }.runTaskTimer(Main.getPlugin(), 50, 50);

        goldtimer = new BukkitRunnable() {
            @Override
            public void run() {
                int size = 0;
                double[] RandomSpot = CoinOnly[new Random().nextInt(0, CoinOnly.length)];
                for(Entity ent : Bukkit.getWorld("world").getNearbyEntities(new Location(Bukkit.getWorld("world"), RandomSpot[0], RandomSpot[1], RandomSpot[2]), RandomSpot[0], RandomSpot[1], RandomSpot[2])) {
                    size++;
                    if(ent instanceof Item item) {
                        if(item.getItemStack().getType() == Material.GOLD_NUGGET) {
                            size = -1;
                        }
                    }

                    if(size >= Bukkit.getWorld("world").getNearbyEntities(new Location(Bukkit.getWorld("world"), RandomSpot[0], RandomSpot[1], RandomSpot[2]), RandomSpot[0], RandomSpot[1], RandomSpot[2]).size()) {
                        Bukkit.getWorld("world").dropItem(new Location(Bukkit.getWorld("world"), RandomSpot[0], RandomSpot[1], RandomSpot[2]), new ItemStack(Material.GOLD_NUGGET, 1));
                    }

                }
            }
        }.runTaskTimer(Main.getPlugin(), 50, 25);

    }

    public static Player getKiller() {
        return killer;
    }

    public static Player getSheriff() {
        return sheriff;
    }

    public static Player getHero() {
        return hero;
    }
    public static void setHero(Player newhero) {
        hero = newhero;
    }

    public static List<Player> getVillagers() {
        return Villagers;
    }

    public static void removeVillager(String name) {
        Villagers.removeIf(player -> player.getName().equals(name));
    }

    public static void setGunholo(ArmorStand gunholo) {
        Game.gunholo = gunholo;
    }

    public static void addPlayerDeath(ArmorStand playerDeath) {
        Game.playerDeath.add(playerDeath);
    }


    public static ArmorStand getGunholo() {
        return gunholo;
    }

    public static boolean getThrown() {
        return thrown;
    }

    public static void setThrown(boolean thrown) {
        Game.thrown = thrown;
    }

    public static void endGame(String winmsg) {
        String heroname = "Nobody";
        if(hero != null) {
            heroname = hero.getName();
        }
        String gameendmsg = "" +
                ChatColor.AQUA + ChatColor.STRIKETHROUGH + "                  " + ChatColor.RESET + ChatColor.DARK_AQUA + ChatColor.BOLD + "[Murder Mystery]" + ChatColor.RESET + ChatColor.AQUA + ChatColor.STRIKETHROUGH + "                  \n" +
                ChatColor.GOLD + "|" + ChatColor.DARK_RED + "  Game Over!  " + ChatColor.GOLD + "|\n" +
                ChatColor.GOLD + "|" + winmsg + ChatColor.GOLD + "|\n\n" +
                ChatColor.GOLD + "•" + ChatColor.DARK_RED + "  " + ChatColor.RED + killer.getName() + ChatColor.DARK_RED + " is the Killer!  " + ChatColor.GOLD + "\n" +
                ChatColor.GOLD + "•" + ChatColor.DARK_BLUE + "  " + ChatColor.BLUE + sheriff.getName() + ChatColor.DARK_BLUE + " is the Sheriff!  " + ChatColor.GOLD + "\n" +
                ChatColor.GOLD + "•" + ChatColor.DARK_GREEN + "  " + ChatColor.GREEN + heroname + ChatColor.DARK_GREEN + " is the Hero!  " + ChatColor.GOLD + "\n" +
                ChatColor.AQUA + ChatColor.STRIKETHROUGH + "                                                            \n";
        Bukkit.broadcastMessage(ChatColor.RED + "The game has ended!");
        Bukkit.broadcastMessage(gameendmsg);
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            if(!player.getName().equals(killer.getName()) && !player.getName().equals(sheriff.getName())) {
                SheriffChance.add(player);
                KillerChance.add(player);
            }
            if(player.getName().equals(killer.getName())) {
                if(SheriffChance.contains(player)) {
                    SheriffChance.remove(player);
                }
                if(KillerChance.contains(player)) {
                    KillerChance.remove(player);
                }
            }
            if(player.getName().equals(sheriff.getName())) {
                if(SheriffChance.contains(player)) {
                    SheriffChance.remove(player);
                }
            }
            if(hero != null) {
                if (player.getName().equals(hero.getName())) {
                    KillerChance.add(player);
                }
            }
        });
        Bukkit.getWorlds().forEach(world -> {
           world.getEntities().forEach(entity -> {
               if(entity instanceof Item item) {
                   if(item.getItemStack().getType() == Material.BOW || item.getItemStack().getType() == Material.GOLD_NUGGET) {
                       item.remove();
                   }
               }
           });
        });


        for(int x = 0;x< playerDeath.size();x++) {
            playerDeath.get(x).remove();
        }


        if (gunholo != null) {
            gunholo.remove();
        }
        heroarrow.cancel();
        goldtimer.cancel();
        if(Endgametimer != null) {
            Endgametimer.cancel();
        }
        if(compassloop != null) {
            compassloop.cancel();
        }
        if(Gameend != null) {
            Gameend.cancel();
        }
        Bossbar.cancel();
        Bukkit.getBossBar(new NamespacedKey(Main.getPlugin(), "timer")).removeAll();
        Commands.game = null;
    }
}
