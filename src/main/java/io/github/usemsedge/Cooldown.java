package io.github.usemsedge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.io.FileWriter;
import java.io.PrintWriter;

public class Cooldown {
    static int steakCooldownInTicks = 200;
    static int auraCooldownInTicks = 300;
    static int eggCooldownInTicks = 200;
    static int currentSteakCooldownInTicks = 0;
    static int currentAuraCooldownInTicks = 0;
    static int currentEggCooldownInTicks = 0;
    static boolean displayCooldownInTicks = false;

    static int[] guiLocation = new int[]{2, 2};
    static boolean toggled = true;
    static int color = 0x00ffff;
    static String align = "left";

    static void onPlayerClick() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        //if (player.getHeldItem().toString())
        //PitUtils.saveLogInfo(player.getHeldItem().toString() + "\n");

        String name;
        try {
            name = player.getHeldItem().getDisplayName();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }


        PitUtils.saveLogInfo(name + "\n");
        if (name.contains("AAA-Rated Steak")) {
            PitUtils.saveLogInfo("Steak Used\n");
            if (currentSteakCooldownInTicks > 0) {
                PitUtils.messagePlayer(player, EnumChatFormatting.RED + "Steak on cooldown!");
                //steaks on cooldown
            }
            else {
                currentSteakCooldownInTicks = steakCooldownInTicks;
            }

        }
                else if (name.contains("Aura of Protection")) {
            PitUtils.saveLogInfo("Aura Used");
            if (currentAuraCooldownInTicks > 0) {
                //Aura cooldown
                PitUtils.messagePlayer(player, EnumChatFormatting.RED + "Aura on cooldown!");
            }
            else {
                currentAuraCooldownInTicks = auraCooldownInTicks;
            }

        }
                else if (name.contains("First-Aid Egg")) {  //"First-Aid Egg")) {
            PitUtils.saveLogInfo("Egg Used");
            if (currentEggCooldownInTicks > 0) {
                //First aid egg
                PitUtils.messagePlayer(player, EnumChatFormatting.RED + "Egg on cooldown!");
            }
            else {
                currentEggCooldownInTicks = eggCooldownInTicks;
            }
        }

                else {
            PitUtils.saveLogInfo(name + " used\n");
        }

        PitUtils.saveLogInfo("\n" + currentAuraCooldownInTicks + " aura\n" +
                            currentEggCooldownInTicks + " egg\n" +
                            currentSteakCooldownInTicks + "Steak\n\n");
    }

    static void adjustCooldowns() {
        if (currentAuraCooldownInTicks > 0) {
            currentAuraCooldownInTicks--;
        }
        if (currentSteakCooldownInTicks > 0) {
            currentSteakCooldownInTicks--;
        }
        if (currentEggCooldownInTicks > 0) {
            currentEggCooldownInTicks--;
        }
    }

    static void renderStats(FontRenderer renderer) {
        String eggCD, steakCD, auraCD;
        if (displayCooldownInTicks) {
            eggCD = "First-Aid Egg cool down: " + currentEggCooldownInTicks + " ticks";
            steakCD = "AAA-Rated Steak cool down: " + currentSteakCooldownInTicks + " ticks";
            auraCD = "Aura Of Protection cool down: " + currentAuraCooldownInTicks + " ticks";
        }
        else {
            eggCD = "First-Aid Egg cool down: " + currentEggCooldownInTicks / 20 + " seconds";
            steakCD = "AAA-Rated Steak cool down: " + currentSteakCooldownInTicks / 20 + " seconds";
            auraCD = "Aura Of Protection Cool down: " + currentAuraCooldownInTicks / 20+ " seconds";
        }
        String longestCd = auraCD;

        if (align.equals("right")) {
            renderer.drawString(steakCD, guiLocation[0] +
                            renderer.getStringWidth(longestCd) -
                            renderer.getStringWidth(steakCD),
                    guiLocation[1] + renderer.FONT_HEIGHT, color, true);
            renderer.drawString(eggCD, guiLocation[0] +
                            renderer.getStringWidth(longestCd) -
                            renderer.getStringWidth(eggCD),
                    guiLocation[1] + renderer.FONT_HEIGHT * 2, color, true);
            renderer.drawString(auraCD, guiLocation[0] +
                            renderer.getStringWidth(longestCd) -
                            renderer.getStringWidth(auraCD),
                    guiLocation[1] + renderer.FONT_HEIGHT * 3, color, true);
        }
        else {
            renderer.drawString(steakCD, guiLocation[0],
                    guiLocation[1] + renderer.FONT_HEIGHT, color, true);
            renderer.drawString(eggCD, guiLocation[0],
                    guiLocation[1] + renderer.FONT_HEIGHT * 2, color, true);
            renderer.drawString(auraCD, guiLocation[0],
                    guiLocation[1] + renderer.FONT_HEIGHT * 3, color, true);
        }
    }

    static boolean isValid(String row) {
        String[] things = row.split(",");

        return PitUtils.isInteger(things[1]) &&
                PitUtils.isInteger(things[2]) &&
                PitUtils.isInteger(things[4], 16) &&
                PitUtils.isBool(things[0]) &&
                things[3].equalsIgnoreCase("left") || things[3].equalsIgnoreCase("right");


                /*
        fw.write(permList.toString() + "\n" +
                MysticDropCounter.toggled + "," + MysticDropCounter.killCount + "," + MysticDropCounter.mysticDrops + "," + MysticDropCounter.sinceLastMysticDrop
                + "," + MysticDropCounter.guiLocation[0] + "," + MysticDropCounter.guiLocation[1] + "," + MysticDropCounter.align + "," + MysticDropCounter.color + "\n" +

                toggled + "," + Cooldown.guiLocation[0] + "," + Cooldown.guiLocation[1] + "," + Cooldown.align + "," + Cooldown.color + "\n" +

                AutoL.toggled + "," + AutoL.onBan + "," + AutoL.onPermList + "," + AutoL.onBountyClaimed
        );*/
    }

    static boolean setVars(String line) {
        try {
            if (isValid(line)) {
                String[] row = line.split(",");
                toggled = row[0].equalsIgnoreCase("true");
                guiLocation = new int[]{Integer.parseInt(row[1]), Integer.parseInt(row[2])};
                align = row[3];
                color = Integer.parseInt(row[4], 16);
                PitUtils.saveLogInfo("cooldown save info works");
                return true;
            }
            PitUtils.saveLogInfo("cooldown save info is not valid" + line);
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("cooldown save info does not work" + line);

            return false;
        }
    }
}
