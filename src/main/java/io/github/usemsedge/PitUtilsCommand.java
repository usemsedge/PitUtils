package io.github.usemsedge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class PitUtilsCommand extends CommandBase {
    @Override
    public List getCommandAliases() {
        return new ArrayList<String>() {
            {
                add("pit");
            }
        };
    }

    @Override
    public String getCommandName()
    {
        return "pitutils";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pitutils [subsection]";
    }

    @Override
    public void processCommand(ICommandSender ics, String[] args) {
        if (ics instanceof EntityPlayer && PitUtils.isInPit) {
            final EntityPlayer player = (EntityPlayer) ics;
            if (args[0].equalsIgnoreCase("myst")) {
                if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                    MysticDropCounter.toggled ^= true;
                    PitUtils.messagePlayer(player, EnumChatFormatting.GREEN +
                            "Mystic Drop Counter has been toggled " +
                            EnumChatFormatting.DARK_GREEN +
                            (MysticDropCounter.toggled ? "on" : "off"));
                }


                else if (args.length == 2 && args[1].equalsIgnoreCase("count")) {
                    PitUtils.messagePlayer(player, "Mystic Drops: " + MysticDropCounter.mysticDrops);
                    PitUtils.messagePlayer(player, "Kills: " + MysticDropCounter.killCount);
                    PitUtils.messagePlayer(player, "Since Last Drop: " + MysticDropCounter.sinceLastMysticDrop);
                }
                else if (args.length == 3 && args[1].equalsIgnoreCase("align")) {
                    MysticDropCounter.align = (args[2].equalsIgnoreCase("right")) ? "right": "left";
                }
                else {
                    PitUtils.messagePlayer(player, EnumChatFormatting.BLACK + "________________________");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "Tracks amounts of kills per mystic drop");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit myst [command] [arguments]");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit myst toggle  (turns the display on or off)");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit myst align (right|left) (aligns the display right or left");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit myst count  (prints the numbers to the chat)");
                }

            }

            else if (args[0].equalsIgnoreCase("cd")) {

                if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                    Cooldown.toggled ^= true;
                    PitUtils.messagePlayer(player, EnumChatFormatting.GREEN +
                            "Item Cooldown has been toggled " +
                            EnumChatFormatting.DARK_GREEN +
                            (Cooldown.toggled ? "on" : "off"));
                }
                else if (args.length == 3 && args[1].equalsIgnoreCase("display")) {
                    if (args[2].equalsIgnoreCase("ticks") || args[2].equalsIgnoreCase("t")) {
                        Cooldown.displayCooldownInTicks = true;
                    }
                    else if (args[2].equalsIgnoreCase("seconds") || args[2].equalsIgnoreCase("s")) {
                        Cooldown.displayCooldownInTicks = false;
                    }
                    else {
                        PitUtils.messagePlayer(player, EnumChatFormatting.RED + "Last argument must be (seconds|s|ticks|t), not " + args[3]);
                    }

                }
                else if (args.length == 3 && args[1].equalsIgnoreCase("align")) {
                    Cooldown.align = (args[2].equalsIgnoreCase("right")) ? "right": "left";
                }
                else {
                    PitUtils.messagePlayer(player, EnumChatFormatting.BLACK + "________________________");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "Tracks cooldowns of First-Aid Egg, AAA-Rated Steak, and Aura of Protection");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit cd [command] [arguments]");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit cd toggle  (turns the display on or off");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit cd align (right|left) (aligns the display right or left");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit cd display (ticks|t|seconds|s)  (displays the cooldown times in ticks or seconds");

                }
            }

            else if (args[0].equalsIgnoreCase("l")) {
                if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                    AutoL.toggled ^= true;
                    PitUtils.messagePlayer(player, EnumChatFormatting.GREEN +
                            "Auto-L has been toggled " +
                            EnumChatFormatting.DARK_GREEN +
                            (AutoL.toggled ? "on" : "off"));
                }
                else if (args.length == 3 && args[1].equalsIgnoreCase("add")) {
                    if (args[2].equalsIgnoreCase("bounty")) {
                        AutoL.onBountyClaimed = true;
                    }
                    else if (args[2].equalsIgnoreCase("perm")) {
                        AutoL.onPermList = true;
                    }
                    else if (args[2].equalsIgnoreCase("ban")) {
                        AutoL.onBan = true;
                    }
                    else {
                        PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "Last argument must be (ban|bounty|perm)");
                        PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "ban: Says L when someone gets banned");
                        PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "bounty: Says L when you claim a bounty");
                        PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "perm: Says L when you kill someone on your perm list");
                    }
                }
                else if (args.length == 3 && args[1].equalsIgnoreCase("remove")) {
                    if (args[2].equalsIgnoreCase("bounty")) {
                        AutoL.onBountyClaimed = false;
                    }
                    else if (args[2].equalsIgnoreCase("perm")) {
                        AutoL.onPermList = false;
                    }
                    else if (args[2].equalsIgnoreCase("ban")) {
                        AutoL.onBan = false;
                    }
                    else {
                        PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "Last argument must be (ban|bounty|perm)");
                        PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "ban: Says L when someone gets banned");
                        PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "bounty: Says L when you claim a bounty");
                        PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "perm: Says L when you kill someone on your perm list");
                    }
                }
                else {
                    PitUtils.messagePlayer(player, EnumChatFormatting.BLACK + "________________________");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "Automatically says L at certain times");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit l [command] [arguments]");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit l remove (ban|bounty|perm) (don't say L when this happens)");
                    PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit l add (ban|bounty|perm) (say L when this happens)");
                }
            }

            else if (args[0].equalsIgnoreCase("tips")) {
                PitUtils.messagePlayer(player, EnumChatFormatting.DARK_GREEN + "PIT TIPS FOR NONS (not unranked players, think skyblock terminology)");
                PitUtils.messagePlayer(player, EnumChatFormatting.GREEN + "To get an axe: You must be Prestige 2 and buy the Barbarian renown upgrade, then buy it in the perk shop");
                PitUtils.messagePlayer(player, EnumChatFormatting.GREEN + "Perks you should unlock in order: G-Head, Strength Chain, Vampire (replace G-Head), Gladiator OR Streaker");
                PitUtils.messagePlayer(player, EnumChatFormatting.GREEN + "Fresh: red, green, yellow, blue, orange fresh pants, worth about 15k");
                PitUtils.messagePlayer(player, EnumChatFormatting.GREEN + "Golden (enchanted) swords: mystic swords, can be enchanted to T1 and T2 when you have the Level 1 Mysticism upgrade, and can be T3 when you have the Level 9 Mysticism Upgrade ");
                PitUtils.messagePlayer(player, EnumChatFormatting.GREEN + "The Pit is a PVP game and expect to be killed.");
                PitUtils.messagePlayer(player, EnumChatFormatting.GREEN + "The Pit has a rabbit infestation that you can't do anything about.");
                PitUtils.messagePlayer(player, EnumChatFormatting.GREEN + "If you think diamond armor is unfair, buy diamond armor yourself.");
                PitUtils.messagePlayer(player, EnumChatFormatting.GREEN + "Permanent diamond armor is either an Archangel Chestplate or someone unlocking Autobuy.");
                PitUtils.messagePlayer(player, EnumChatFormatting.GREEN + "Before downloading a Pit Mod, be sure to make sure it does not contain any RATs");
            }

            else {
                PitUtils.messagePlayer(player, EnumChatFormatting.BLACK + "___________________________");
                PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pitutils (/pit) [subsection] for more information");
                PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit myst  (Mystic Counter)");
                PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit cd  (Item Cooldowns)");
                PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit l (Auto-L");
                PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit tips");
                PitUtils.messagePlayer(player, EnumChatFormatting.LIGHT_PURPLE + "/pit help");

            }
        }

        else {
            ics.addChatMessage(
                    new ChatComponentText(
                            EnumChatFormatting.RED + "Please join The Hypixel Pit to use this command."));
        }
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}