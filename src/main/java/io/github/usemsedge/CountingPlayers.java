package io.github.usemsedge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class CountingPlayers {

    static boolean isWearingSoli = false;
    static int soliLevel = 0;
    static int playersWithin7 = 0;

    static boolean isWearingNotGlad = false;
    static int notGladLevel = 0;
    static int playersWithin12 = 0;
    //not glad is 12 i believe

    static boolean isHoldingShark = false;
    static int sharkLevel = 0;
    static int playersWithin12Under6 = 0;


    static boolean isWearingSybil = false;
    static int sybilLevel = 0;

    static int[] guiLocation = new int[]{300, 2};
    static boolean toggled = true;
    static int color = 0x00ffff;
    static String align = "left";

    static void checkGear() {
        try {
            String pants = PitUtils.getLore(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[1]);

            if (pants.contains("Solitude III")) {
                isWearingSoli = true;
                soliLevel = 3;
            } else if (pants.contains("Solitude II")) {
                isWearingSoli = true;
                soliLevel = 2;
            } else if (pants.contains("Solitude")) {
                isWearingSoli = true;
                soliLevel = 1;
            } else {
                isWearingSoli = false;
                soliLevel = 0;
            }

            if (pants.contains("Gladiator III")) {
                isWearingNotGlad = true;
                notGladLevel = 3;
            } else if (pants.contains("Gladiator II")) {
                isWearingNotGlad = true;
                notGladLevel = 2;
            } else if (pants.contains("Gladiator")) {
                isWearingNotGlad = true;
                notGladLevel = 1;
            } else {
                isWearingNotGlad = false;
                notGladLevel = 0;
            }

            if (pants.contains("Sybil III")) {
                isWearingSybil = true;
                sybilLevel = 3;
            }
            if (pants.contains("Sybil II")) {
                isWearingSybil = true;
                sybilLevel = 2;
            }
            if (pants.contains("Sybil")) {
                isWearingSybil = true;
                sybilLevel = 1;
            }
            else {
                isWearingSybil = false;
                sybilLevel = -1;
            }


            String sword = PitUtils.getLore(Minecraft.getMinecraft().thePlayer.getHeldItem());

            if (sword.contains("Shark III")) {
                isHoldingShark = true;
                sharkLevel = 3;
            } else if (sword.contains("Shark II")) {
                isHoldingShark = true;
                sharkLevel = 2;
            } else if (sword.contains("Shark")) {
                isHoldingShark = true;
                sharkLevel = 1;
            } else {
                isHoldingShark = false;
                sharkLevel = 0;
            }
        }
        catch (Exception e) {}
    }

    static List<List<Double>> getAllPlayerLocations() {
        List<EntityPlayer> players = new ArrayList<>(Minecraft.getMinecraft().theWorld.playerEntities);
        List<List<Double>> positions = new ArrayList<>();
        List<Double> x;
        for (EntityPlayer player : players){
            x = new ArrayList<>();
            x.add(player.posX);
            x.add(player.posZ);
            positions.add(x);
        }
        return positions;
    }

    static List<Float> getAllPlayerHealth() {
        List<EntityPlayer> players = new ArrayList<>(Minecraft.getMinecraft().theWorld.playerEntities);
        List<Float> positions = new ArrayList<>();
        for (EntityPlayer player : players){
            positions.add(player.getHealth());
        }
        return positions;
    }

    static void updateCount () {
        double playerX = Minecraft.getMinecraft().thePlayer.posX;
        double playerZ = Minecraft.getMinecraft().thePlayer.posZ;

        List<List<Double>> locs = getAllPlayerLocations();

        int bPlayersWithin12 = 0; //shark & notglad
        int bPlayersWithin7 = 0; //soli
        for (int i = 0; i < locs.size(); i++) {
            if (Math.pow(playerX - locs.get(i).get(0), 2) + Math.pow(playerZ - locs.get(i).get(1), 2) < 49) {
                bPlayersWithin7 += 1;
                bPlayersWithin12 += 1;
            }
            else if (Math.pow(playerX - locs.get(i).get(0), 2) + Math.pow(playerZ - locs.get(i).get(1), 2) < 144) {
                bPlayersWithin12 += 1;
            }
        }

        List<Float> health = getAllPlayerHealth();
        int bPlayersWithin12Under6 = 0; //shark
        for (Float h : health) {
            if (h < 6) {
                bPlayersWithin12Under6 += 1;
            }
        }
        playersWithin7 = bPlayersWithin7 + 1 + sybilLevel;
        playersWithin12 = bPlayersWithin12 + 1 + sybilLevel;
        if (playersWithin12 > 10) {playersWithin12 = 10;}
        playersWithin12Under6 = bPlayersWithin12Under6 + 1 + sybilLevel;
    }



    static void renderStats(FontRenderer renderer) {
        String soli, notGlad, shark;


        if (soliLevel == 1 && playersWithin7 <= 1) {
            soli = "Solitude: ✔ -40%";
        }
        else if (soliLevel == 2 && playersWithin7 <= 2) {
            soli = "Solitude: ✔ -50%";
        }
        else if (soliLevel == 3 && playersWithin7 <= 2) {
            soli = "Solitude: ✔ -60%";
        }
        else {
            soli = "Solitude: X";
        }

        if (notGladLevel == 1) {
            notGlad = "Not Glad: -" + (double) playersWithin12 + "%";
        }
        else if (notGladLevel == 2) {
            notGlad = "Not Glad: -" + (1.5d * playersWithin12) + "%";
        }
        else if (notGladLevel == 3) {
            notGlad = "Not Glad: -" + (2d * playersWithin12) + "%";
        }
        else {
            notGlad = "Not Glad: X";
        }

        if (sharkLevel == 1) {
            shark = "Shark: +" + 2d * playersWithin12Under6 + "%";
        }
        else if (sharkLevel == 2) {
            shark = "Shark: +" + 4d * playersWithin12Under6 + "%";
        }
        else if (sharkLevel == 3) {
            shark = "Shark: +" + 7d * playersWithin12Under6 + "%";
        }
        else {
            shark = "Shark: X";
        }

        int longest = Math.max(Math.max(renderer.getStringWidth(shark), renderer.getStringWidth(notGlad)), renderer.getStringWidth(soli));


        if (align.equals("right")) {
            renderer.drawString(soli, guiLocation[0] +
                            longest - renderer.getStringWidth(soli),
                    guiLocation[1], color, true);
            renderer.drawString(notGlad, guiLocation[0] +
                            longest - renderer.getStringWidth(notGlad),
                    guiLocation[1] + renderer.FONT_HEIGHT, color, true);
            renderer.drawString(shark, guiLocation[0] +
                            longest - renderer.getStringWidth(shark),
                    guiLocation[1] + renderer.FONT_HEIGHT * 2, color, true);
        }
        else {
            renderer.drawString(soli, guiLocation[0],
                    guiLocation[1], color, true);
            renderer.drawString(notGlad, guiLocation[0],
                    guiLocation[1] + renderer.FONT_HEIGHT, color, true);
            renderer.drawString(shark, guiLocation[0],
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
                PitUtils.saveLogInfo("CountingPlayer save info successfully loaded\n");

                return true;
            }
            PitUtils.saveLogInfo("Counting Player save info failed: here was the faulty data " + line + "\n");
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("CountingPlayer save info had some kind of error\n");

            return false;
        }
    }
}
