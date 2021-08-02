package io.github.usemsedge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.text.DecimalFormat;

public class MysticDropCounter {
    static int killCount = 0;
    static int mysticDrops = 0;
    static int sinceLastMysticDrop = 0;

    static int[] guiLocation = new int[]{2, 2};
    static boolean toggled = true;
    static int color = 0x00ffff;
    static String align = "left";

    static void onChatMessageReceived(String msg) {

        if (msg.contains("MYSTIC ITEM!") && !msg.contains(":") && msg.contains("dropped")) {
            mysticDrops++;
            sinceLastMysticDrop = 0;
        }

        else if (msg.contains("KILL!") && !msg.contains(":") && msg.contains("[") && msg.contains("]")) {
            killCount += 1;
            sinceLastMysticDrop += 1;
            PitUtils.saveLogInfo(msg);
            PitUtils.messagePlayer(msg);

        }
    }

    static void renderStats(FontRenderer renderer) {
        String killsPerMystic =
                "Kills/Mystic: " + ((mysticDrops == 0) ? mysticDrops
                        : new DecimalFormat("#.##")
                        .format(killCount / (mysticDrops * 1.0d)));
        String kills = "Kills: " + killCount;
        String mystics = "Mystic Drops: " + mysticDrops;
        String lastMystic = "Kills since last Mystic Drop: " + sinceLastMysticDrop;

        String longestMyst = lastMystic;
        //lastMystic is the longest string, because you are not getting a 20 digit number of kills

        if (align.equalsIgnoreCase("right")) {
            renderer.drawString(mystics, guiLocation[0] +
                            renderer.getStringWidth(longestMyst) -
                            renderer.getStringWidth(mystics),
                    guiLocation[1], color, true);
            renderer.drawString(kills, guiLocation[0] +
                            renderer.getStringWidth(longestMyst) -
                            renderer.getStringWidth(kills),
                    guiLocation[1] + renderer.FONT_HEIGHT, color, true);
            renderer.drawString(killsPerMystic, guiLocation[0] +
                            renderer.getStringWidth(longestMyst) -
                            renderer.getStringWidth(killsPerMystic),
                    guiLocation[1] + renderer.FONT_HEIGHT * 2, color, true);
            renderer.drawString(lastMystic, guiLocation[0] +
                            renderer.getStringWidth(longestMyst) -
                            renderer.getStringWidth(lastMystic),
                    guiLocation[1] + renderer.FONT_HEIGHT * 3, color, true);
        }
        else {
            renderer.drawString(mystics, guiLocation[0],
                    guiLocation[1], color, true);
            renderer.drawString(kills, guiLocation[0],
                    guiLocation[1] + renderer.FONT_HEIGHT, color, true);
            renderer.drawString(killsPerMystic, guiLocation[0],
                    guiLocation[1] + renderer.FONT_HEIGHT * 2, color, true);
            renderer.drawString(lastMystic, guiLocation[0],
                    guiLocation[1] + renderer.FONT_HEIGHT * 3, color, true);
        }
    }

    static boolean isValid(String row) {
        String[] things = row.split(",");
        //MysticDropCounter.toggled + "," + MysticDropCounter.killCount + "," + MysticDropCounter.mysticDrops + "," + MysticDropCounter.sinceLastMysticDrop
        //                                + "," + MysticDropCounter.guiLocation[0] + "," + MysticDropCounter.guiLocation[1] + "," + MysticDropCounter.align + "," + MysticDropCounter.color + "\n" +
        return PitUtils.isInteger(things[1]) && //killCount
                PitUtils.isInteger(things[2]) && //mysticDrops
                PitUtils.isInteger(things[3]) && //sinceLastMysticDrop
                PitUtils.isInteger(things[4]) && //guiLocation0
                PitUtils.isInteger(things[5]) && //guiLocation1
                PitUtils.isInteger(things[7], 16) && //color
                PitUtils.isBool(things[0]) &&  //toggled
                things[6].equalsIgnoreCase("left") || things[6].equalsIgnoreCase("right");
    }

    static boolean setVars(String line) {
        try {
            if (isValid(line)) {
                String[] row = line.split(",");
                toggled = row[0].equalsIgnoreCase("true");
                killCount = Integer.parseInt(row[1]);
                mysticDrops = Integer.parseInt(row[2]);
                sinceLastMysticDrop = Integer.parseInt(row[3]);
                guiLocation = new int[]{Integer.parseInt(row[4]), Integer.parseInt(row[5])};
                align = row[6];
                color = Integer.parseInt(row[7]);
                PitUtils.saveLogInfo("Mystic Counter save info successfully loaded\n");
                return true;
            }
            PitUtils.saveLogInfo("Mystic Counter save info failed: here was the faulty data " + line + "\n");
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("Mystic Counter save info had some kind of error\n");
            return false;
        }
    }
}
