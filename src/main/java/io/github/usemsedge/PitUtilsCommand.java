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

    public void chat(EntityPlayer player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

    @Override
    public void processCommand(ICommandSender ics, String[] args) {
        if (ics instanceof EntityPlayer && PitUtils.isInPit) {
            final EntityPlayer player = (EntityPlayer) ics;
            if (args[0].equalsIgnoreCase("myst")) {
                if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                    PitUtils.toggledMyst ^= true;
                    chat(player, EnumChatFormatting.GREEN +
                            "Mystic Drop Counter has been toggled " +
                            EnumChatFormatting.DARK_GREEN +
                            (PitUtils.toggledMyst ? "on" : "off"));
                }


                else if (args.length == 2 && args[1].equalsIgnoreCase("count")) {
                    chat(player, "Mystic Drops: " + (int)PitUtils.mysticDrops);
                    chat(player, "Kills: " + (int)PitUtils.killCount);
                    chat(player, "Since Last Drop: " + (int)PitUtils.sinceLastMysticDrop);
                }
                else {
                    chat(player, EnumChatFormatting.BLACK + "________________________");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "Tracks amounts of kills per mystic drop");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "/pitutils myst [command] [arguments]");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "/pitutils myst toggle  (turns the display on or off)");

                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "/pitutils myst count  (prints the numbers to the chat)");
                }

            }

            else if (args[0].equalsIgnoreCase("cd")) {

                if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                    PitUtils.toggledCooldown ^= true;
                    chat(player, EnumChatFormatting.GREEN +
                            "Item Cooldown has been toggled " +
                            EnumChatFormatting.DARK_GREEN +
                            (PitUtils.toggledCooldown ? "on" : "off"));
                }
                else if (args.length == 3 && args[1].equalsIgnoreCase("display")) {
                    if (args[2].equalsIgnoreCase("ticks") || args[2].equalsIgnoreCase("t")) {
                        PitUtils.displayCooldownInTicks = true;
                    }
                    else if (args[2].equalsIgnoreCase("seconds") || args[2].equalsIgnoreCase("s")) {
                        PitUtils.displayCooldownInTicks = false;
                    }
                    else {
                        chat(player, EnumChatFormatting.RED + "Last argument must be (seconds|s|ticks|t), not " + args[3]);
                    }

                }
                else {
                    chat(player, EnumChatFormatting.BLACK + "________________________");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "Tracks cooldowns of First-Aid Egg, AAA-Rated Steak, and Aura of Protection");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "/pitutils cd [command] [arguments]");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "/pitutils cd toggle  (turns the display on or off");
                    chat(player, EnumChatFormatting.LIGHT_PURPLE + "/pitutils cd display (ticks|t|seconds|s)  (displays the cooldown times in ticks or seconds");

                }
            }

            else if (args.length == 2 && args[1].equalsIgnoreCase("align")) {
                PitUtils.align = (args[2].equalsIgnoreCase("right")) ? "right": "left";
            }


            else if (args[0].equalsIgnoreCase("tips")) {
                chat(player, EnumChatFormatting.DARK_GREEN + "PIT TIPS FOR NONS (not unranked players, think skyblock terminology)");
                chat(player, EnumChatFormatting.GREEN + "To get an axe: You must be Prestige 2 and buy the Barbarian renown upgrade, then buy it in the perk shop");
                chat(player, EnumChatFormatting.GREEN + "Perks you should unlock in order: G-Head, Strength Chain, Vampire (replace G-Head), Gladiator OR Streaker");
                chat(player, EnumChatFormatting.GREEN + "Fresh: red, green, yellow, blue, orange fresh pants, worth about 15k");
                chat(player, EnumChatFormatting.GREEN + "Golden (enchanted) swords: mystic swords, can be enchanted to T1 and T2 when you have the Level 1 Mysticism upgrade, and can be T3 when you have the Level 9 Mysticism Upgrade ");
                chat(player, EnumChatFormatting.GREEN + "The Pit is a PVP game and expect to be killed.");
                chat(player, EnumChatFormatting.GREEN + "The Pit has a rabbit infestation that you can't do anything about.");
                chat(player, EnumChatFormatting.GREEN + "If you think diamond armor is unfair, buy diamond armor yourself.");
                chat(player, EnumChatFormatting.GREEN + "Permanent diamond armor is either an Archangel Chestplate or someone unlocking Autobuy.");
                chat(player, EnumChatFormatting.GREEN + "Before downloading a Pit Mod, be sure to make sure it does not contain any RATs");
            }


            else {
                chat(player, EnumChatFormatting.BLACK + "___________________________");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "/pitutils [subsection] for more information");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "myst  (Mystic Counter)");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "cd  (Item Cooldowns)");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "align (right|left)  (sets the alignment of the display)");
                chat(player, EnumChatFormatting.LIGHT_PURPLE + "tips");

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