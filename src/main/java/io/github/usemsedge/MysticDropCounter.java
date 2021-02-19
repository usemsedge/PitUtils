package io.github.usemsedge;

import net.minecraft.client.gui.FontRenderer;
import scala.Int;

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
        long d = System.currentTimeMillis();

        if (msg.contains("MYSTIC ITEM!") && !msg.contains(":") && msg.contains("dropped")) {
            mysticDrops++;
            sinceLastMysticDrop = 0;
        }

        else if (msg.contains("KILL!") && !msg.contains(":") && msg.contains("[") && msg.contains("]")) {
            killCount += 1;
            sinceLastMysticDrop += 1;

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


        //PitUtils.saveLogInfo("\n\n" + eggCD + "\n" + auraCD + "\n" + steakCD + "\n\n");

        String longestMyst = lastMystic;
        //lastMystic is the longest string, because you are not getting a 20 digit number of kills

        if (align == "right") {
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
        if (PitUtils.isInteger(things[1]) && //killCount
            PitUtils.isInteger(things[2]) && //mysticDrops
            PitUtils.isInteger(things[3]) && //sinceLastMysticDrop
            PitUtils.isInteger(things[4]) && //guiLocation0
            PitUtils.isInteger(things[5]) && //guiLocation1
            PitUtils.isInteger(things[7], 16) && //color
            PitUtils.isBool(things[0]) &&  //toggled
            things[6].equalsIgnoreCase("left") || things[6].equalsIgnoreCase("right") ) {
            return true;
            //MysticDropCounter.toggled + "," + MysticDropCounter.killCount + "," + MysticDropCounter.mysticDrops + "," + MysticDropCounter.sinceLastMysticDrop
            //                                + "," + MysticDropCounter.guiLocation[0] + "," + MysticDropCounter.guiLocation[1] + "," + MysticDropCounter.align + "," + MysticDropCounter.color + "\n" +
        }
        return false;
    }

    static boolean setVars(String line) {
        PitUtils.saveLogInfo("mysticdropcounter started to save info \n");
        try {
            if (isValid(line)) {
                String[] row = line.split(",");
                PitUtils.saveLogInfo("line has been split \n");
                toggled = row[0].equalsIgnoreCase("true");
                PitUtils.saveLogInfo("toggled has been set");
                killCount = Integer.parseInt(row[1]);
                PitUtils.saveLogInfo("killcount set");
                mysticDrops = Integer.parseInt(row[2]);
                PitUtils.saveLogInfo("mysticdrops set");
                sinceLastMysticDrop = Integer.parseInt(row[3]);
                PitUtils.saveLogInfo("sincelast set");
                guiLocation = new int[]{Integer.parseInt(row[4]), Integer.parseInt(row[5])};
                PitUtils.saveLogInfo("guilocation set");
                align = row[6];
                PitUtils.saveLogInfo("align set");
                color = Integer.parseInt(row[7]);
                PitUtils.saveLogInfo("mystic drop save info works");
                return true;
            }
            PitUtils.saveLogInfo("mystic drop save info invalud" + line);
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("mystic drop save info does not work" + line);
            return false;
        }
    }
}
