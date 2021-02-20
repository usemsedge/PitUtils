package io.github.usemsedge;

import net.minecraft.client.gui.FontRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PermTracker {
    static boolean toggled = true;
    static boolean sayInChat = false;
    static int[] guiLocation = new int[]{2, 200};
    static int color = 0x00ffff;
    static String align = "left";

    static List<String> permedPlayersInServer = new ArrayList<>();

    static List<String> findPermedPlayersInServer () {
        List<String> players = PitUtils.getPlayers();
        List<String> foundPlayers = new ArrayList<>();
        StringBuilder s = new StringBuilder();

        for (String player : players) {
            if (PitUtils.permList.contains(player)) {
                foundPlayers.add(player);
                s.append(" " + player);
            }
        }
        PitUtils.saveLogInfo(s.toString() + "\n");
        PitUtils.saveLogInfo(PitUtils.permList.toString() + "\n");
        PitUtils.saveLogInfo(players.toString());
        return foundPlayers;
    }

    static void renderStats(FontRenderer renderer) {
        String longest = "";
        for (String player : permedPlayersInServer) {
            if (renderer.getStringWidth(player) > renderer.getStringWidth(longest)) {
                longest = player;
            }
        }

        if (align == "right") {
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
        if (
                PitUtils.isInteger(things[2]) &&
                PitUtils.isInteger(things[3]) &&
                PitUtils.isInteger(things[5]) &&
                PitUtils.isBool(things[0]) &&  //toggled
                PitUtils.isBool(things[1]) && //display in chat
                things[4].equalsIgnoreCase("left") || things[4].equalsIgnoreCase("right") ) {
            return true;
            //PermTracker.toggled + "," + PermTracker.guiLocation[0] + "," + PermTracker.guiLocation[1] + PermTracker.align + "," + PermTracker.color
        }
        return false;
    }

    static boolean setVars(String line) {
        PitUtils.saveLogInfo("permtracker started to save info \n");
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
                PitUtils.saveLogInfo("perm tracker save info works");
                return true;
            }
            PitUtils.saveLogInfo("perm tracker save info invalud" + line);
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("perm tracker save info does not work" + line);
            return false;
        }
    }
}
