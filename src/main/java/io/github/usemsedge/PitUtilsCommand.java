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
    public List<String> getCommandAliases() {
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
            try {

                if (args[0].equalsIgnoreCase("myst")) {
                    if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                        MysticDropCounter.toggled ^= true;
                        PitUtils.messagePlayer(EnumChatFormatting.GREEN +
                                "Mystic Drop Counter has been toggled " +
                                EnumChatFormatting.DARK_GREEN +
                                (MysticDropCounter.toggled ? "on" : "off"));
                    } else if (args.length == 2 && args[1].equalsIgnoreCase("count")) {
                        PitUtils.messagePlayer("Mystic Drops: " + MysticDropCounter.mysticDrops);
                        PitUtils.messagePlayer("Kills: " + MysticDropCounter.killCount);
                        PitUtils.messagePlayer("Since Last Drop: " + MysticDropCounter.sinceLastMysticDrop);
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("align")) {
                        MysticDropCounter.align = (args[2].equalsIgnoreCase("right")) ? "right" : "left";
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("color")) {
                        char[] c = args[2].toCharArray();

                        //last six chars of the input string
                        // #ffffff will give "ffffff", so will 0xffffff
                        char[] x = Arrays.copyOfRange(c, c.length - 6, c.length);
                        String number = String.copyValueOf(x);

                        if (!PitUtils.isInteger(number, 16)) {
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Changes the color of the display");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /pit myst color (color)");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "(color) should be substituted for a 6-character hex value like 00ffff");
                            return;
                        }
                        MysticDropCounter.color = Integer.decode("0x" + number);
                    } else if (args.length == 4 && args[1].equalsIgnoreCase("pos")) {

                        if (PitUtils.isInteger(args[2]) &&
                                PitUtils.isInteger(args[3])) {

                            MysticDropCounter.guiLocation[0] = Integer.parseInt(args[2]);
                            MysticDropCounter.guiLocation[1] = Integer.parseInt(args[3]);
                        } else {
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Changes the location of the display");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /pit myst pos (x) (y)");
                        }
                    } else {
                        PitUtils.messagePlayer(EnumChatFormatting.BLACK + "________________________");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Tracks amounts of kills per mystic drop");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit myst [command] [arguments]");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit myst toggle  (turns the display on or off)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit myst align (right|left) (aligns the display right or left");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit myst count  (prints the numbers to the chat)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit myst color (color) (changes the mystic drop display to the color)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit myst pos (x) (x) (changes the location of the mystic drop display");
                    }

                } else if (args[0].equalsIgnoreCase("cd")) {

                    if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                        Cooldown.toggled ^= true;
                        PitUtils.messagePlayer(EnumChatFormatting.GREEN +
                                "Item Cooldown has been toggled " +
                                EnumChatFormatting.DARK_GREEN +
                                (Cooldown.toggled ? "on" : "off"));
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("display")) {
                        if (args[2].equalsIgnoreCase("ticks") || args[2].equalsIgnoreCase("t")) {
                            Cooldown.displayCooldownInTicks = true;
                        } else if (args[2].equalsIgnoreCase("seconds") || args[2].equalsIgnoreCase("s")) {
                            Cooldown.displayCooldownInTicks = false;
                        } else {
                            PitUtils.messagePlayer(EnumChatFormatting.RED + "Last argument must be (seconds|s|ticks|t), not " + args[2]);
                        }

                    } else if (args.length == 3 && args[1].equalsIgnoreCase("align")) {
                        Cooldown.align = (args[2].equalsIgnoreCase("right")) ? "right" : "left";
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("color")) {
                        char[] c = args[2].toCharArray();

                        char[] x = Arrays.copyOfRange(c, c.length - 6, c.length);
                        String number = String.copyValueOf(x);

                        if (!PitUtils.isInteger(number, 16)) {
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Changes the color of the display");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /pit cd color (color)");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "(color) should be substituted for a 6-character hex value like 00ffff");
                            return;
                        }
                        Cooldown.color = Integer.decode("0x" + number);
                    } else if (args.length == 4 && args[1].equalsIgnoreCase("pos")) {

                        if (PitUtils.isInteger(args[2]) &&
                                PitUtils.isInteger(args[3])) {

                            Cooldown.guiLocation[0] = Integer.parseInt(args[2]);
                            Cooldown.guiLocation[1] = Integer.parseInt(args[3]);
                        } else {
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Changes the location of the display");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /pit cd pos (x) (y)");
                        }
                    } else {
                        PitUtils.messagePlayer(EnumChatFormatting.BLACK + "________________________");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Tracks cooldowns of First-Aid Egg, AAA-Rated Steak, and Aura of Protection");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit cd [command] [arguments]");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit cd toggle  (turns the display on or off");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit cd align (right|left) (aligns the display right or left");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit cd display (ticks|t|seconds|s)  (displays the cooldown times in ticks or seconds");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit cd color (color) (changes the cooldown display to the color)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit cd pos (x) (x) (changes the location of the cooldown display");
                    }
                } else if (args[0].equalsIgnoreCase("l")) {
                    if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                        AutoL.toggled ^= true;
                        PitUtils.messagePlayer(EnumChatFormatting.GREEN +
                                "Auto-L has been toggled " +
                                EnumChatFormatting.DARK_GREEN +
                                (AutoL.toggled ? "on" : "off"));
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("add")) {
                        if (args[2].equalsIgnoreCase("bounty")) {
                            AutoL.onBountyClaimed = true;
                            PitUtils.messagePlayer(EnumChatFormatting.GREEN + "Will now say L when you claim a bounty");
                        } else if (args[2].equalsIgnoreCase("perm")) {
                            AutoL.onPermList = true;
                            PitUtils.messagePlayer(EnumChatFormatting.GREEN + "Will now say L when you kill someone on your perm list");
                        } else if (args[2].equalsIgnoreCase("ban")) {
                            AutoL.onBan = true;
                            PitUtils.messagePlayer(EnumChatFormatting.GREEN + "Will now say L when someone gets banned");
                        } else {
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Last argument must be (ban|bounty|perm)");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "ban: Says L when someone gets banned");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "bounty: Says L when you claim a bounty");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "perm: Says L when you kill someone on your perm list");
                        }
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("remove")) {
                        if (args[2].equalsIgnoreCase("bounty")) {
                            AutoL.onBountyClaimed = false;
                            PitUtils.messagePlayer(EnumChatFormatting.GREEN + "Will now not say L when you claim a bounty");
                        } else if (args[2].equalsIgnoreCase("perm")) {
                            AutoL.onPermList = false;
                            PitUtils.messagePlayer(EnumChatFormatting.GREEN + "Will now not say L when you kill someone on your perm list");
                        } else if (args[2].equalsIgnoreCase("ban")) {
                            AutoL.onBan = false;
                            PitUtils.messagePlayer(EnumChatFormatting.GREEN + "Will now not say L when someone gets banned");
                        } else {
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Last argument must be (ban|bounty|perm)");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "ban: Says L when someone gets banned");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "bounty: Says L when you claim a bounty");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "perm: Says L when you kill someone on your perm list");
                        }
                    } else {
                        PitUtils.messagePlayer(EnumChatFormatting.BLACK + "________________________");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Automatically says L at certain times");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit l [command] [arguments]");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit l remove (ban|bounty|perm) (don't say L when this happens)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit l add (ban|bounty|perm) (say L when this happens)");
                    }
                } else if (args[0].equalsIgnoreCase("perm")) {
                    if (args.length == 3 && args[1].equalsIgnoreCase("add")) {
                        if (PitUtils.checkUsername(args[2])) {
                            if (PitUtils.permList.contains(args[2])) {
                                PitUtils.messagePlayer(EnumChatFormatting.RED + args[2] + " is already permed.");
                            } else {
                                PitUtils.permList.add(args[2]);
                                PitUtils.messagePlayer(EnumChatFormatting.GREEN + args[2] + " is now permed.");
                            }
                        } else {
                            PitUtils.messagePlayer(EnumChatFormatting.RED + args[2] + " is not a valid username.");
                        }

                    } else if (args.length == 3 && args[1].equalsIgnoreCase("remove")) {
                        if (PitUtils.checkUsername(args[2])) {
                            if (PitUtils.permList.contains(args[2])) {
                                PitUtils.permList.remove(args[2]);
                                PitUtils.messagePlayer(EnumChatFormatting.GREEN + args[2] + " is no longer permed.");
                            } else {
                                PitUtils.messagePlayer(EnumChatFormatting.RED + args[2] + " is not permed.");
                            }
                        } else {
                            PitUtils.messagePlayer(EnumChatFormatting.RED + args[2] + " is not a valid username.");
                        }
                    } else if (args.length == 2 && args[1].equalsIgnoreCase("clear")) {
                        PitUtils.permList.clear();
                        PitUtils.messagePlayer(EnumChatFormatting.GREEN + "Your perm list is now empty.");
                    } else if (args.length == 2 && args[1].equalsIgnoreCase("view")) {
                        for (int i = 0; i < PitUtils.permList.size(); i++) {
                            PitUtils.messagePlayer(EnumChatFormatting.RED + PitUtils.permList.get(i));
                        }
                    } else if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                        PermTracker.toggled ^= true;
                        PitUtils.messagePlayer(EnumChatFormatting.GREEN +
                                "Perm List Display has been toggled " +
                                EnumChatFormatting.DARK_GREEN +
                                (PermTracker.toggled ? "on" : "off"));
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("align")) {
                        PermTracker.align = (args[2].equalsIgnoreCase("right")) ? "right" : "left";
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("color")) {
                        char[] c = args[2].toCharArray();

                        char[] x = Arrays.copyOfRange(c, c.length - 6, c.length);
                        String number = String.copyValueOf(x);

                        if (!PitUtils.isInteger(number, 16)) {
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Changes the color of the display");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /pit perm color (color)");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "(color) should be substituted for a 6-character hex value like 00ffff");
                            return;
                        }
                        PermTracker.color = Integer.decode("0x" + number);
                    } else if (args.length == 4 && args[1].equalsIgnoreCase("pos")) {

                        if (PitUtils.isInteger(args[2]) &&
                                PitUtils.isInteger(args[3])) {

                            PermTracker.guiLocation[0] = Integer.parseInt(args[2]);
                            PermTracker.guiLocation[1] = Integer.parseInt(args[3]);
                        } else {
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Changes the location of the display");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /pit perm pos (x) (y)");
                        }
                    } else if (args.length == 2 && args[1].equalsIgnoreCase("chat")) {
                        PermTracker.sayInChat ^= true;
                        PitUtils.messagePlayer(EnumChatFormatting.GREEN +
                                "Perm List Say In Chat " +
                                EnumChatFormatting.DARK_GREEN +
                                (PermTracker.toggled ? "on" : "off"));
                    } else {
                        PitUtils.messagePlayer(EnumChatFormatting.BLACK + "__________________________");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Manages your list of \"permed\" (permenantly hunted) players, or players you want to hunt and kill");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "If toggled on, people in your lobby and the perm list will show up");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm [command] [argument]");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm add (player) (adds this player to your perm list");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm remove (player) (removes this player from your perm list");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm clear (deletes everyone from your perm list)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm view (views your perm list)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm toggle (turns on or off the display");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm color (color) (changes the color of the display)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm align (right|left) (aligns the text right or left");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm pos (x) (y) (sets the locatino of the display)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm chat (turns on or off saying the permed people in chat constantly)");
                    }


                } else if (args[0].equalsIgnoreCase("dark")) {

                    if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                        DarkChecker.toggled ^= true;

                        PitUtils.messagePlayer(EnumChatFormatting.GREEN +
                                "Dark Pants Display has been toggled " +
                                EnumChatFormatting.DARK_GREEN +
                                (DarkChecker.toggled ? "on" : "off"));
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("align")) {
                        DarkChecker.align = (args[2].equalsIgnoreCase("right")) ? "right" : "left";
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("color")) {
                        char[] c = args[2].toCharArray();

                        char[] x = Arrays.copyOfRange(c, c.length - 6, c.length);
                        String number = String.copyValueOf(x);

                        if (!PitUtils.isInteger(number, 16)) {
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Changes the color of the display");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /pit dark color (color)");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "(color) should be substituted for a 6-character hex value like 00ffff");
                            return;
                        }
                        DarkChecker.color = Integer.decode("0x" + number);
                    } else if (args.length == 4 && args[1].equalsIgnoreCase("pos")) {

                        if (PitUtils.isInteger(args[2]) &&
                                PitUtils.isInteger(args[3])) {

                            DarkChecker.guiLocation[0] = Integer.parseInt(args[2]);
                            DarkChecker.guiLocation[1] = Integer.parseInt(args[3]);
                        } else {
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Changes the location of the display");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /pit dark pos (x) (y)");
                        }
                    } else if (args.length == 2 && args[1].equalsIgnoreCase("chat")) {

                        DarkChecker.sayInChat ^= true;
                        PitUtils.messagePlayer(EnumChatFormatting.GREEN +
                                "Dark List Say In Chat " +
                                EnumChatFormatting.DARK_GREEN +
                                (DarkChecker.toggled ? "on" : "off"));

                    } else {
                        PitUtils.messagePlayer(EnumChatFormatting.BLACK + "__________________________");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "If toggled on, people in your lobby wearing dark pants will show up");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit dark [command] [argument]");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit dark toggle (turns on or off the display");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit dark color (color) (changes the color of the display)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit dark align (right|left) (aligns the text right or left");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit dark pos (x) (y) (sets the locatino of the display)");
                        PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit dark chat (turns on or off saying the dark people in chat constantly)");
                    }


                }
                else if (args[0].equalsIgnoreCase("count")) {
                        if (args.length == 2 && args[1].equalsIgnoreCase("toggle")) {
                            CountingPlayers.toggled ^= true;

                            PitUtils.messagePlayer(EnumChatFormatting.GREEN +
                                    "Counting Players Display has been toggled " +
                                    EnumChatFormatting.DARK_GREEN +
                                    (CountingPlayers.toggled ? "on" : "off"));
                        } else if (args.length == 3 && args[1].equalsIgnoreCase("align")) {
                            CountingPlayers.align = (args[2].equalsIgnoreCase("right")) ? "right" : "left";
                        } else if (args.length == 3 && args[1].equalsIgnoreCase("color")) {
                            char[] c = args[2].toCharArray();

                            char[] x = Arrays.copyOfRange(c, c.length - 6, c.length);
                            String number = String.copyValueOf(x);

                            if (!PitUtils.isInteger(number, 16)) {
                                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Changes the color of the display");
                                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /pit count color (color)");
                                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "(color) should be substituted for a 6-character hex value like 00ffff");
                                return;
                            }
                            CountingPlayers.color = Integer.decode("0x" + number);
                        } else if (args.length == 4 && args[1].equalsIgnoreCase("pos")) {

                            if (PitUtils.isInteger(args[2]) &&
                                    PitUtils.isInteger(args[3])) {

                                CountingPlayers.guiLocation[0] = Integer.parseInt(args[2]);
                                CountingPlayers.guiLocation[1] = Integer.parseInt(args[3]);
                            } else {
                                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Changes the location of the display");
                                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Correct usage: /pit count pos (x) (y)");
                            }
                        } else {
                            PitUtils.messagePlayer(EnumChatFormatting.BLACK + "__________________________");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "If toggled on, shows shark, notglad, solitude");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit count [command] [argument]");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit count toggle (turns on or off the display");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit count color (color) (changes the color of the display)");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit count align (right|left) (aligns the text right or left");
                            PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit count pos (x) (y) (sets the locatino of the display)");
                        }
                }
                    else if (args[0].equalsIgnoreCase("tips")) {
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Features:");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "All features are toggleable and customizable.");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Mystic Counter: Tracks your mystic drops compared to your kills.");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Cooldown: Tracks cooldowns and prevents you from using too many items.");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Auto L: Automatically says L whenever someone gets banned or you kill someone.");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Perm List: Add people to a list and you can see if they are in your server.");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Dark Checker: See if there are players in darks in your server.");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Contact me at cityblock#7498 or on the github/forge if you find bugs or need help.");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "https://github.com/usemsedge/PitUtils");
                } else {
                    PitUtils.messagePlayer(EnumChatFormatting.BLACK + "___________________________");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Pit Utils is a QOL mod for The Hypixel Pit.");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pitutils (/pit) [subsection] for more information");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit myst  (Mystic Counter)");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit cd  (Item Cooldowns)");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit l (Auto-L");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm (your perm list)");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit dark (dark pant users)");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit count (# of players near you)");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit tips");
                    PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit help");
                }
            }
            catch (Exception e) {
                PitUtils.messagePlayer(EnumChatFormatting.BLACK + "___________________________");
                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "Pit Utils is a QOL mod for The Hypixel Pit.");
                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pitutils (/pit) [subsection] for more information");
                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit myst  (Mystic Counter)");
                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit cd  (Item Cooldowns)");
                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit l (Auto-L");
                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit perm (your perm list)");
                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit dark (dark pant users)");
                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit count (# of players near you)");
                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit tips");
                PitUtils.messagePlayer(EnumChatFormatting.LIGHT_PURPLE + "/pit help");
            }
        }

        else {
            ics.addChatMessage(
                    new ChatComponentText(
                            EnumChatFormatting.RED + "Please join The Hypixel Pit to use this command."));
        }
        PitUtils.saveInfo();
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}