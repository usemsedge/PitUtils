package io.github.usemsedge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.EntityLivingBase;
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
        PitUtils.saveLogInfo(("start checking for darks + \n"));
        List<EntityPlayer> players = Minecraft.getMinecraft().theWorld.playerEntities;
        PitUtils.saveLogInfo("length of players is " + players.size());
        for (EntityPlayer player : players) {
            PitUtils.saveLogInfo("a player" + player.getName() + "\n");
            try {
                item = player.inventory.armorInventory[3]; //pants?? third slot in r
                PitUtils.saveLogInfo(item.getDisplayName() + "\n");
                if (item.getDisplayName().contains("Dark") || item.getDisplayName().contains("Evil")) {
                    playersUsingDarks.add(player.getName());
                }
                PitUtils.saveLogInfo("their armor had stuff in it " + player.inventory.armorInventory.toString() + "             " + item.getDisplayName() + "\n");
            }
            catch (Exception e) {
                PitUtils.saveLogInfo("they armor slots made an exception" + player.inventory.armorInventory.toString() + "\n");
            }
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

        List<String> e = playersUsingDarksInServer;
        if (e.isEmpty()) {
            e.add("No darks");
        }

        if (align == "right") {
            for (int i = 0; i < playersUsingDarksInServer.size(); i++) {
                renderer.drawString(playersUsingDarksInServer.get(i), guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(playersUsingDarksInServer.get(i)),
                        guiLocation[1] + renderer.FONT_HEIGHT * i, color, true);
            }
        }
        else {
            for (int i = 0; i < playersUsingDarksInServer.size(); i++) {
                renderer.drawString(playersUsingDarksInServer.get(i), guiLocation[0],
                        guiLocation[1] + renderer.FONT_HEIGHT * i, color, true);
            }
        }
    }

    static boolean isValid(String row) {
        String[] things = row.split(",");
        if (
                PitUtils.isInteger(things[2]) &&
                        PitUtils.isInteger(things[3]) &&
                        PitUtils.isInteger(things[5]) &&
                        PitUtils.isBool(things[0]) &&  //toggled
                        PitUtils.isBool(things[1]) && //display in chat
                        things[4].equalsIgnoreCase("left") || things[4].equalsIgnoreCase("right") ) {
            return true;
            //PermTracker.toggled + ","say in chat + PermTracker.guiLocation[0] + "," + PermTracker.guiLocation[1] + PermTracker.align + "," + PermTracker.color
        }
        return false;
    }

    static boolean setVars(String line) {
        PitUtils.saveLogInfo("dark tracker started to save info \n");
        try {
            if (isValid(line)) {
                String[] row = line.split(",");
                PitUtils.saveLogInfo("line has been split \n");
                toggled = row[0].equalsIgnoreCase("true");
                sayInChat = row[1].equalsIgnoreCase("true");
                PitUtils.saveLogInfo("toggled has been set");

                guiLocation = new int[]{Integer.parseInt(row[2]), Integer.parseInt(row[3])};
                PitUtils.saveLogInfo("guilocation set");
                align = row[4];
                PitUtils.saveLogInfo("align set");
                color = Integer.parseInt(row[5]);
                PitUtils.saveLogInfo("dark tracker save info works");
                return true;
            }
            PitUtils.saveLogInfo("dark tracker save info invalud" + line);
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("dark tracker save info does not work" + line);
            return false;
        }
    }
}
