package io.github.usemsedge;

import net.minecraft.client.Minecraft;

public class AutoL {
    static boolean toggled = true;

    static boolean onBountyClaimed = false;
    static boolean onBan = true;
    static boolean onPermList = true;

    static String L = "L";

    static void checkIfSayL(String msg) {
        if (onBountyClaimed) {
            /*
            if (msg.contains("BOUNTY CLAIMED!") && msg.contains(Minecraft.getMinecraft().thePlayer.getName()) {

            }*/
        }

        if (onBan) {
            if (msg.contains("A player has been removed") && !msg.contains(":") && msg.contains("!")) {
                sayL();
            }
        }

        if (onPermList) {
            if (msg.contains("KILL!") && msg.contains("[") && msg.contains("]") && !msg.contains(":")
                && PitUtils.permList.contains(msg.substring(msg.indexOf("]") + 1, msg.indexOf(" +") - 1))) {
                sayL();
            }
        }

    }

    static void sayL() {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(L);
    }

    static boolean isValid(String row) {
        String[] things = row.split(",");

        if (PitUtils.isBool(things[0]) &&
            PitUtils.isBool(things[1]) &&
            PitUtils.isBool(things[2]) &&
            PitUtils.isBool(things[3])) {
            return true;
        }
        return false;

                /*
        fw.write(permList.toString() + "\n" +
                MysticDropCounter.toggled + "," + MysticDropCounter.killCount + "," + MysticDropCounter.mysticDrops + "," + MysticDropCounter.sinceLastMysticDrop
                + "," + MysticDropCounter.guiLocation[0] + "," + MysticDropCounter.guiLocation[1] + "," + MysticDropCounter.align + "," + MysticDropCounter.color + "\n" +

                Cooldown.toggled + "," + Cooldown.guiLocation[0] + "," + Cooldown.guiLocation[1] + "," + Cooldown.align + "," + Cooldown.color + "\n" +

                AutoL.toggled + "," + AutoL.onBan + "," + AutoL.onPermList + "," + AutoL.onBountyClaimed
        );*/
    }

    static boolean setVars(String line) {
        if (isValid(line)) {
            String[] row = line.split(",");
            toggled = (row[0].equalsIgnoreCase("true")) ? true: false;
            onBan = (row[1].equalsIgnoreCase("true")) ? true: false;
            onPermList = (row[2].equalsIgnoreCase("true")) ? true: false;
            onBountyClaimed = (row[3].equalsIgnoreCase("true")) ? true: false;
            return true;
        }
        return false;
    }
}
