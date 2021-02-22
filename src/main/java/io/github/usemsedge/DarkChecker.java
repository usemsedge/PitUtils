package io.github.usemsedge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DarkChecker {
    static boolean toggled = true;
    static int[] guiLocation = new int[]{100, 2};
    static boolean sayInChat = false;
    static String align = "left";
    static int color = 0x00ffff;

    static List<String> playersUsingDarksInServer = new ArrayList<>();

    static List<String> checkForDarks() {
        ItemStack item;
        List<String> playersUsingDarks = new ArrayList<>();
        List<EntityPlayer> players = Minecraft.getMinecraft().theWorld.playerEntities;
        for (EntityPlayer player : players) {
            try {
                item = player.inventory.armorInventory[1]; //pants?? third slot in r
                if ((item.getDisplayName().contains("Dark") || item.getDisplayName().contains("Evil")) && item.getDisplayName().contains("Tier")) {
                    playersUsingDarks.add(player.getName());
                }
            }
            catch (Exception e) {} //no pants
        }
        return playersUsingDarks;
    }

    static void renderStats(FontRenderer renderer) {
        String longest = "";
        for (String player : playersUsingDarksInServer) {
            if (renderer.getStringWidth(player) > renderer.getStringWidth(longest)) {
                longest = player;
            }
        }

        List<String> e = new ArrayList<>(playersUsingDarksInServer);
        e.add(0, "List of dark users");

        if (align.equalsIgnoreCase("right")) {
            for (int i = 0; i < e.size(); i++) {
                renderer.drawString(e.get(i), guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(e.get(i)),
                        guiLocation[1] + renderer.FONT_HEIGHT * i, color, true);
            }
        }
        else {
            for (int i = 0; i < e.size(); i++) {
                renderer.drawString(e.get(i), guiLocation[0],
                        guiLocation[1] + renderer.FONT_HEIGHT * i, color, true);
            }
        }
    }

    static boolean isValid(String row) {
        String[] things = row.split(",");
        //PermTracker.toggled + ","say in chat + PermTracker.guiLocation[0] + "," + PermTracker.guiLocation[1] + PermTracker.align + "," + PermTracker.color
        return PitUtils.isInteger(things[2]) &&
                PitUtils.isInteger(things[3]) &&
                PitUtils.isInteger(things[5]) &&
                PitUtils.isBool(things[0]) &&  //toggled
                PitUtils.isBool(things[1]) && //display in chat
                things[4].equalsIgnoreCase("left") || things[4].equalsIgnoreCase("right");
    }

    static boolean setVars(String line) {
        try {
            if (isValid(line)) {
                String[] row = line.split(",");
                toggled = row[0].equalsIgnoreCase("true");
                sayInChat = row[1].equalsIgnoreCase("true");
                guiLocation = new int[]{Integer.parseInt(row[2]), Integer.parseInt(row[3])};
                align = row[4];
                color = Integer.parseInt(row[5]);
                PitUtils.saveLogInfo("Dark Tracker save info successfully loaded\n");
                return true;
            }
            PitUtils.saveLogInfo("Dark Tracker save info failed: here was the faulty data " + line + "\n");
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("Dark Tracker save info had some kind of error\n");
            return false;
        }
    }
}
