package io.github.usemsedge;

import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.List;

public class PermTracker {
    static boolean toggled = true;
    static boolean sayInChat = false;
    static int[] guiLocation = new int[]{2, 100};
    static int color = 0x00ffff;
    static String align = "left";

    static List<String> permedPlayersInServer = new ArrayList<>();

    static List<String> findPermedPlayersInServer () {
        List<String> players = PitUtils.getPlayerNames();
        List<String> foundPlayers = new ArrayList<>();

        for (String player : players) {
            for (String permedPlayer : PitUtils.permList) {
                if (player.equalsIgnoreCase(permedPlayer)) {
                    foundPlayers.add(player);
                }
            }
        }
        return foundPlayers;
    }

    static void renderStats(FontRenderer renderer) {
        String longest = "";
        for (String player : permedPlayersInServer) {
            if (renderer.getStringWidth(player) > renderer.getStringWidth(longest)) {
                longest = player;
            }
        }

        List<String> e = new ArrayList<>(permedPlayersInServer);
        if (e.isEmpty()) {
            e.add("No permed players");
        }

        if (align.equalsIgnoreCase("right")) {
            for (int i = 0; i < permedPlayersInServer.size(); i++) {
                renderer.drawString(permedPlayersInServer.get(i), guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(permedPlayersInServer.get(i)),
                        guiLocation[1] + renderer.FONT_HEIGHT * i, color, true);
            }
        }
        else {
            for (int i = 0; i < permedPlayersInServer.size(); i++) {
                renderer.drawString(permedPlayersInServer.get(i), guiLocation[0],
                        guiLocation[1] + renderer.FONT_HEIGHT * i, color, true);
            }
        }
    }

    static boolean isValid(String row) {
        String[] things = row.split(",");
        //PermTracker.toggled + "," + PermTracker.guiLocation[0] + "," + PermTracker.guiLocation[1] + PermTracker.align + "," + PermTracker.color
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
                PitUtils.saveLogInfo("Perm Tracker save info successfully loaded\n");
                return true;
            }
            PitUtils.saveLogInfo("Perm Tracker save info failed: here was the faulty data " + line + "\n");
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("Perm Tracker save info had some kind of error\n");
            return false;
        }
    }
}
