package me.squeaky2137.spigotmurdermystery.events;


import me.squeaky2137.spigotmurdermystery.Commands;
import me.squeaky2137.spigotmurdermystery.GameManagement.Game;
import me.squeaky2137.spigotmurdermystery.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;

public class PlayerDeath implements Listener {

    //death event
    @EventHandler
    public void BowKill(EntityDamageByEntityEvent e) {


        if(Commands.game != null) {
            if(e.getEntity() instanceof Player) {
                Game game = Commands.game;
                if (e.getCause() == EntityDamageByEntityEvent.DamageCause.PROJECTILE) {
                    // murder die
                    if (e.getEntity().getName().equals(Game.getKiller().getName())) {
                        Player p = (Player) e.getEntity();
                        p.setGameMode(GameMode.SPECTATOR);
                        p.sendTitle(ChatColor.RED + "You died!", "", 20, 30, 20);
                        p.sendMessage(ChatColor.RED + "You died!");
                        p.setHealth(20);
                        p.getInventory().clear();
                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            pl.sendMessage(ChatColor.GREEN + "The Murderer has been killed!");
                            pl.sendTitle(ChatColor.GREEN + "Murderer killed!", "", 40, 40, 20);
                        }
                        Game.setHero((Player) ((Arrow)e.getDamager()).getShooter());
                        game.endGame(ChatColor.GREEN + " Villagers Won! ");

                        //Inno shot
                    } else {
                        ((Player) e.getEntity()).setGameMode(GameMode.SPECTATOR);
                        ((Player) e.getEntity()).sendTitle(ChatColor.RED + "You died!", "", 30, 40, 20);
                        e.getEntity().sendMessage(ChatColor.RED + "You died!");
                        ((Player) e.getEntity()).setHealth(20);
                        ((Player) e.getEntity()).getInventory().clear();



                        ArmorStand Playerded = e.getEntity().getWorld().spawn(e.getEntity().getLocation().add(0,-1,0), ArmorStand.class);
                        Playerded.setVisible(false);
                        Playerded.setGravity(false);
                        Playerded.setCustomName(ChatColor.RED + e.getEntity().getName() + "'s " + " grave");
                        Playerded.setCustomNameVisible(true);
                        Game.addPlayerDeath(Playerded);
                        Game.removeVillager(e.getEntity().getName());

                        if(Game.getHero() != null) {
                            if (Game.getHero().getName().equals(((Player) ((Arrow) e.getDamager()).getShooter()).getName())) {

                                //kill sheriff
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    p.sendMessage(ChatColor.RED + "The Sheriff has been killed!");
                                    p.sendTitle(ChatColor.RED + "Sheriff died!", "", 50, 30, 20);
                                }
                                Game.getHero().setGameMode(GameMode.SPECTATOR);
                                Game.getHero().sendTitle(ChatColor.RED + "You died!", "", 20, 30, 20);
                                Game.getHero().sendMessage(ChatColor.RED + "You died!");
                                Game.getHero().setHealth(20);
                                Game.getHero().getInventory().clear();
                                Game.removeVillager(Game.getHero().getName());
                                ArmorStand holo = e.getEntity().getWorld().spawn(Game.getHero().getLocation().add(0,-2,0), ArmorStand.class);
                                holo.setVisible(false);
                                holo.setGravity(true);
                                holo.setCustomName(ChatColor.GOLD + "Sheriff's Gun");
                                holo.setCustomNameVisible(true);
                                Game.setGunholo(holo);
                                Entity bow = e.getEntity().getWorld().dropItem(Game.getHero().getLocation(), new ItemStack(Material.BOW));
                                bow.setGlowing(true);
                                Game.setHero(null);
                            }
                        }

                        //end game
                        if(killerwin()) {
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                pl.sendMessage(ChatColor.RED + "The Murderer wins!");
                                pl.sendTitle(ChatColor.RED + "Murderer wins!", "", 40, 40, 20);
                            }
                            game.endGame(ChatColor.RED + " The Murderer Won! ");
                        }
                    }


                    //murder kills now
                } else if(e.getDamager().getName().equals(Game.getKiller().getName()) && ((Player) e.getDamager()).getInventory().getItemInMainHand().getType() == Material.IRON_SWORD) {
                    //hero/sheriff dies
                    if(Game.getHero() != null) {
                        if (e.getEntity().getName().equals(Game.getHero().getName())) {
                            Player p = (Player) e.getEntity();
                            p.setGameMode(GameMode.SPECTATOR);
                            p.sendTitle(ChatColor.RED + "You died!", "", 20, 30, 20);
                            p.sendMessage(ChatColor.RED + "You died!");
                            p.setHealth(20);
                            p.getInventory().clear();
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                pl.sendMessage(ChatColor.GREEN + "The Sheriff has been killed!");
                                pl.sendTitle(ChatColor.GREEN + "Sheriff died!", "", 20, 40, 20);
                            }
                            Game.removeVillager(e.getEntity().getName());
                            Game.setHero(null);
                            ArmorStand holog = p.getWorld().spawn(p.getLocation().add(0,-2,0), ArmorStand.class);
                            holog.setVisible(false);
                            holog.setGravity(true);
                            holog.setCustomName(ChatColor.GOLD + "Sheriff's Gun");
                            holog.setCustomNameVisible(true);
                            Game.setGunholo(holog);
                            Entity bow = p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.BOW));
                            bow.setGlowing(true);
                            if (killerwin()) {
                                for (Player pl : Bukkit.getOnlinePlayers()) {
                                    pl.sendMessage(ChatColor.RED + "The Murderer wins!");
                                    pl.sendTitle(ChatColor.RED + "The Murderer wins!", "", 40, 40, 20);
                                }
                                game.endGame(ChatColor.RED + " The Murderer Won! ");
                            }

                            //Innocent dies
                        } else {
                            ((Player) e.getEntity()).setGameMode(GameMode.SPECTATOR);
                            ((Player) e.getEntity()).sendTitle(ChatColor.RED + "You died!", "", 20, 30, 20);
                            e.getEntity().sendMessage(ChatColor.RED + "You died!");
                            ((Player) e.getEntity()).setHealth(20);
                            ((Player) e.getEntity()).getInventory().clear();



                            ArmorStand Playerded = e.getEntity().getWorld().spawn(e.getEntity().getLocation().add(0,-1,0), ArmorStand.class);
                            Playerded.setVisible(false);
                            Playerded.setGravity(false);
                            Playerded.setCustomName(ChatColor.RED + e.getEntity().getName() + "'s " + " grave");
                            Playerded.setCustomNameVisible(true);

                            Game.addPlayerDeath(Playerded);
                            Game.removeVillager(e.getEntity().getName());
                        }
                    } else {
                        ((Player) e.getEntity()).setGameMode(GameMode.SPECTATOR);
                        ((Player) e.getEntity()).sendTitle(ChatColor.RED + "You died!", "", 20, 30, 20);
                        e.getEntity().sendMessage(ChatColor.RED + "You died!");
                        ((Player) e.getEntity()).setHealth(20);
                        ((Player) e.getEntity()).getInventory().clear();



                        ArmorStand Playerded = e.getEntity().getWorld().spawn(e.getEntity().getLocation().add(0,-1,0), ArmorStand.class);
                        Playerded.setVisible(false);
                        Playerded.setGravity(false);
                        Playerded.setCustomName(ChatColor.RED + e.getEntity().getName() + "'s " + " grave");
                        Playerded.setCustomNameVisible(true);
                        Game.addPlayerDeath(Playerded);
                        Game.removeVillager(e.getEntity().getName());
                    }
                        //end game check
                        if(killerwin()) {
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                pl.sendMessage(ChatColor.RED + "The Murderer wins!");
                                pl.sendTitle(ChatColor.RED + "The Murderer wins!", "", 40, 40, 20);
                            }
                            game.endGame(ChatColor.RED + " The Murderer Won! ");
                        }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }


    //cancel killer picking up gun
    // set new hero
    // coin management
    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if(Commands.game != null) {
                if(e.getEntity() instanceof Player player) {
                    if (e.getItem().getItemStack().getType() == Material.BOW) {
                        if(e.getEntity().getName().equals(Game.getKiller().getName())) {
                            e.setCancelled(true);
                        } else {
                            Bukkit.broadcastMessage(ChatColor.RED + "Gun picked up!");
                            for(Player pl : Bukkit.getOnlinePlayers()) {
                                pl.sendTitle(ChatColor.RED + "The gun has been picked up!", "", 20, 40, 20);
                            }
                            Game.setHero(player);
                            Game.getGunholo().remove();
                            Game.setGunholo(null);
                        }
                    }
                    if (e.getItem().getItemStack().getType() == Material.GOLD_NUGGET) {
                        if(Game.getHero() != null) {
                            if (e.getEntity().getName().equals(Game.getHero().getName())) {
                                e.setCancelled(true);
                            } else {
                                ItemStack flowerslot = player.getInventory().getItem(8);
                                if (flowerslot == null) {
                                    e.setCancelled(true);
                                    e.getItem().remove();
                                    player.getInventory().setItem(8, new ItemStack(Material.GOLD_NUGGET, 1));
                                } else {
                                    int floweramount = flowerslot.getAmount() + e.getItem().getItemStack().getAmount();
                                    if (floweramount >= 10) {
                                        e.getItem().remove();
                                        player.getInventory().getItem(8).setAmount(0);

                                        if (Objects.requireNonNull(player.getInventory().getItem(0)).getType() == Material.BOW) {
                                            ItemStack ArrowSlot = player.getInventory().getItem(8);
                                            int amount = 0;
                                            if (ArrowSlot != null) {
                                                amount = ArrowSlot.getAmount() + 1;
                                            }

                                            player.getInventory().setItem(7, new ItemStack(Material.ARROW, amount));
                                        } else {
                                            player.getInventory().addItem(new ItemStack(Material.BOW));
                                            player.getInventory().setItem(7, new ItemStack(Material.ARROW, 1));
                                        }


                                    }
                                }
                            }
                        } else {
                            ItemStack flowerslot = player.getInventory().getItem(8);
                            if (flowerslot == null) {
                                e.getItem().remove();
                                player.getInventory().setItem(8, new ItemStack(Material.SUNFLOWER, 1));
                            } else {
                                int floweramount = flowerslot.getAmount() + e.getItem().getItemStack().getAmount();
                                if (floweramount >= 10) {
                                    e.getItem().remove();
                                    player.getInventory().getItem(8).setAmount(0);

                                    if (Objects.requireNonNull(player.getInventory().getItem(0)).getType() == Material.BOW) {
                                        ItemStack ArrowSlot = player.getInventory().getItem(8);
                                        int amount = 0;
                                        if (ArrowSlot != null) {
                                            amount = ArrowSlot.getAmount() + 1;
                                        }

                                        player.getInventory().setItem(7, new ItemStack(Material.ARROW, amount));
                                    } else {
                                        player.getInventory().addItem(new ItemStack(Material.BOW));
                                        player.getInventory().setItem(7, new ItemStack(Material.ARROW, 1));
                                    }


                                }
                            }
                        }
                    }
                }
        }
    }


    @EventHandler
    public void onThrowSword(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD) {
                //throw sword event
                if(!Game.getThrown()) {
                    Vector dir = player.getEyeLocation().getDirection();
                    ArmorStand as = player.getWorld().spawn(player.getLocation().add(dir), ArmorStand.class);
                    as.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
                    as.setRightArmPose(new EulerAngle(0, 0, 5));
                    as.setGravity(false);
                    as.setVisible(false);
                    as.setCanPickupItems(false);
                    as.setMarker(true);

                    BukkitTask swordthrow = new BukkitRunnable() {
                        @Override
                        public void run() {
                            as.teleport(as.getLocation().add(dir));
                            for (Player p1 : Bukkit.getOnlinePlayers()) {
                                if(!p1.getName().equals(Game.getKiller().getName())) {
                                    if (p1.getLocation().distance(as.getLocation()) <= 1) {
                                        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, p1, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1.0);
                                        Bukkit.getPluginManager().callEvent(event);
                                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 0);
                                        cancel();
                                    }
                                }

                            }
                            if (as.getLocation().add(dir).add(0, 1, 0).getBlock().getType() != Material.AIR && as.getLocation().add(dir).add(0, 1, 0).getBlock().getType() != Material.WHEAT) {
                                as.remove();
                                cancel();
                            }


                        }
                    }.runTaskTimer(Main.getPlugin(), 0, 2);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (as != null) {
                                as.remove();
                                swordthrow.cancel();
                            }

                        }
                    }.runTaskLater(Main.getPlugin(), 30);

                    Game.setThrown(true);
                    ItemStack sword = player.getInventory().getItemInMainHand();
                    Damageable swordmet = (Damageable) sword.getItemMeta();
                    swordmet.setDamage(250);
                    sword.setItemMeta(swordmet);
                    BukkitTask sworddur = new BukkitRunnable() {
                        @Override
                        public void run() {
                            Damageable swordmeta = (Damageable) sword.getItemMeta();
                            swordmeta.setDamage(swordmeta.getDamage() - 3);
                            sword.setItemMeta(swordmeta);
                        }
                    }.runTaskTimer(Main.getPlugin(), 0, 1);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Game.setThrown(false);
                            sworddur.cancel();
                        }
                    }.runTaskLater(Main.getPlugin(), 100);


                }

            }
        }
    }


    //cancel moving items
    @EventHandler
    public void cancelinvmove(InventoryClickEvent e) {
        if(Commands.game != null) {
            if(e.getWhoClicked() instanceof Player) {
                e.setCancelled(true);
            }
        }
    }


    //cancel dropping items
    @EventHandler
    public void canceldrop(PlayerDropItemEvent e) {
        if(Commands.game != null) {
            e.setCancelled(true);
        }
    }


    //stop pickup arrows
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() == EntityType.ARROW && (e.getHitBlock() != null)) {
            entity.remove();
        }
    }

    public boolean killerwin() {
        return Game.getVillagers().size() <= 0;
    }
}
