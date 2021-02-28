package io.github.usemsedge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class Cooldown {
    static int steakCooldownInTicks = 200;
    static int auraCooldownInTicks = 300;
    static int eggCooldownInTicks = 200;
    static int currentSteakCooldownInTicks = 0;
    static int currentAuraCooldownInTicks = 0;
    static int currentEggCooldownInTicks = 0;
    static boolean displayCooldownInTicks = false;

    static int[] guiLocation = new int[]{2, 50};
    static boolean toggled = true;
    static int color = 0x00ffff;
    static String align = "left";

    static void onPlayerClick(PlayerInteractEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        String name;
        try {
            name = player.getHeldItem().getDisplayName();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        if (name.contains("AAA-Rated Steak")) {
            if (currentSteakCooldownInTicks > 0) {
                PitUtils.messagePlayer(EnumChatFormatting.RED + "Steak on cooldown!");
                //steaks on cooldown
                e.setCanceled(true);
            }
            else {
                currentSteakCooldownInTicks = steakCooldownInTicks;
            }

        }
        else if (name.contains("Aura of Protection")) {
            if (currentAuraCooldownInTicks > 0) {
                //Aura cooldown
                PitUtils.messagePlayer(EnumChatFormatting.RED + "Aura on cooldown!");
                e.setCanceled(true);
            }
            else {
                currentAuraCooldownInTicks = auraCooldownInTicks;
            }

        }
        else if (name.contains("First-Aid Egg")) {  //"First-Aid Egg")) {
            if (currentEggCooldownInTicks > 0) {
                //First aid egg
                PitUtils.messagePlayer(EnumChatFormatting.RED + "Egg on cooldown!");
            }
            else {
                currentEggCooldownInTicks = eggCooldownInTicks;
            }
        }

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
                    guiLocation[1], color, true);
            renderer.drawString(eggCD, guiLocation[0] +
                            renderer.getStringWidth(longestCd) -
                            renderer.getStringWidth(eggCD),
                    guiLocation[1] + renderer.FONT_HEIGHT, color, true);
            renderer.drawString(auraCD, guiLocation[0] +
                            renderer.getStringWidth(longestCd) -
                            renderer.getStringWidth(auraCD),
                    guiLocation[1] + renderer.FONT_HEIGHT * 2, color, true);
        }
        else {
            renderer.drawString(steakCD, guiLocation[0],
                    guiLocation[1], color, true);
            renderer.drawString(eggCD, guiLocation[0],
                    guiLocation[1] + renderer.FONT_HEIGHT, color, true);
            renderer.drawString(auraCD, guiLocation[0],
                    guiLocation[1] + renderer.FONT_HEIGHT * 2, color, true);
        }
    }

    static boolean isValid(String row) {
        String[] things = row.split(",");

        return PitUtils.isInteger(things[1]) &&
                PitUtils.isInteger(things[2]) &&
                PitUtils.isInteger(things[4], 16) &&
                PitUtils.isBool(things[0]) &&
                things[3].equalsIgnoreCase("left") || things[3].equalsIgnoreCase("right");

    }

    static boolean setVars(String line) {
        try {
            if (isValid(line)) {
                String[] row = line.split(",");
                toggled = row[0].equalsIgnoreCase("true");
                guiLocation = new int[]{Integer.parseInt(row[1]), Integer.parseInt(row[2])};
                align = row[3];
                color = Integer.parseInt(row[4]);
                PitUtils.saveLogInfo("Cooldown save info successfully loaded\n");

                return true;
            }
            PitUtils.saveLogInfo("Cooldown save info failed: here was the faulty data " + line + "\n");
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("Cooldown save info had some kind of error\n");

            return false;
        }
    }
}
