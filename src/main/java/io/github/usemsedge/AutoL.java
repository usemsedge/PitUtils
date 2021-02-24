package io.github.usemsedge;

import net.minecraft.client.Minecraft;

public class AutoL {
    static boolean toggled = true;

    static boolean onBountyClaimed = false;
    static boolean onBan = true;
    static boolean onPermList = true;

    static String L = "L";
    //test
    //gytrfdsauighfsdiu

    static void checkIfSayL(String msg) {
        if (onBountyClaimed) {

            if (msg.contains("BOUNTY CLAIMED!") && msg.contains(Minecraft.getMinecraft().thePlayer.getName()) &&
                    msg.contains("killed") && msg.indexOf(Minecraft.getMinecraft().thePlayer.getName()) < msg.indexOf("killed")) {
                    //You claimed a bounty, not the other way around
                sayL();
            }
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

        return PitUtils.isBool(things[0]) &&
                PitUtils.isBool(things[1]) &&
                PitUtils.isBool(things[2]) &&
                PitUtils.isBool(things[3]);
    }

    static boolean setVars(String line) {
        try {
            if (isValid(line)) {
                String[] row = line.split(",");
                toggled = row[0].equalsIgnoreCase("true");
                onBan = row[1].equalsIgnoreCase("true");
                onPermList = row[2].equalsIgnoreCase("true");
                onBountyClaimed = row[3].equalsIgnoreCase("true");
                PitUtils.saveLogInfo("Auto-L save info successfully loaded\n");
                return true;
            }
            PitUtils.saveLogInfo("Auto-L save info failed: here was the faulty data " + line + "\n");
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("Auto-L save info had some kind of error\n");
            return false;
        }
    }
}
