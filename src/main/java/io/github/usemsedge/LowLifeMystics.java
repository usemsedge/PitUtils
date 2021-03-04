package io.github.usemsedge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LowLifeMystics {
    static int[] guiLocation = new int[]{300, 50};
    static boolean toggled = true;
    static int color = 0x00ffff;
    static String align = "left";

    static int livesToAlert = 3;
    static int lowLifePants = 0, lowLifeSwords = 0, lowLifeBows = 0, lowLifeArmor = 0, lowLifeItems = 0;


    static int getLives(String itemNBT) {
        if (itemNBT.contains("Tier") ||
            itemNBT.contains("Golden Helmet") ||
            itemNBT.contains("Archangel") ||
            itemNBT.contains("Armageddon")) {
            int slash = itemNBT.indexOf("/");
            int colon = itemNBT.indexOf(":");
            String lives = itemNBT.substring(colon + 2, slash); //lives look like      Lives: 12/46
            return Integer.parseInt(lives);
        }
        else {
            return Integer.MAX_VALUE; //it's fresh
        }

    }

    static void checkAllLives() {
        InventoryPlayer inv = Minecraft.getMinecraft().thePlayer.inventory;

        ItemStack item;
        String itemNBT, itemName;
        int lives, swords = 0, pants = 0, bows = 0, armor = 0, items = 0;
        for (int i = 0; i < 36; i++) {
            try {
                item = inv.getStackInSlot(i);
                itemNBT = PitUtils.getNBT(item);
                itemName = item.getDisplayName();
                lives = getLives(itemNBT);
                PitUtils.saveLogInfo(lives + " " + itemName + "\n");
                if (lives < livesToAlert) {
                    if (itemName.contains("Pants")) {
                        pants += 1;
                    } else if (itemName.contains("Sword")) {
                        swords += 1;
                    } else if (itemName.contains("Bow")) {
                        bows += 1;
                    } else if (itemName.contains("Golden Helmet") ||
                            itemName.contains("Archangel") ||
                            itemName.contains("Armageddon")) {
                        armor += 1;
                    } else {
                        items += 1;
                    }
                    PitUtils.saveLogInfo(itemName + " " + lives + " has low lives\n");
                }
            }
            catch (Exception e) {
                //no item int hat slot
            }

        }
        lowLifeArmor = armor;
        lowLifeItems = items;
        lowLifeSwords = swords;
        lowLifeBows = bows;
        lowLifePants = pants;
    }
    static void renderStats(FontRenderer renderer) {
        List<String> strings = new ArrayList<>();
        strings.add("Low Life Mystics");
        if (lowLifeArmor > 0) {
            strings.add("Armor Pieces: " + lowLifeArmor);
        }
        if (lowLifePants > 0) {
            strings.add("Mystic Pants: " + lowLifePants);
        }
        if (lowLifeSwords > 0) {
            strings.add("Mystic Swords: " + lowLifeSwords);
        }
        if (lowLifeBows > 0) {
            strings.add("Mystic Bows: " + lowLifeBows);
        }
        if (lowLifeItems > 0) {
            strings.add("Other Items: " + lowLifeItems);
        }
        if (strings.size() == 1) {
            strings.add("None");
        }


        String longest = "";
        for (String s : strings) {
            if (renderer.getStringWidth(s) > renderer.getStringWidth(longest)) {
                longest = s;
            }
        }

        if (align.equalsIgnoreCase("right")) {
            for (int i = 0; i < strings.size(); i++) {
                renderer.drawString(strings.get(i), guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(strings.get(i)),
                        guiLocation[1] + renderer.FONT_HEIGHT * i, color, true);
            }
        }
        else {
            for (int i = 0; i < strings.size(); i++) {
                renderer.drawString(strings.get(i), guiLocation[0],
                        guiLocation[1] + renderer.FONT_HEIGHT * i, color, true);
            }
        }
    }

    static boolean isValid(String row) {
        String[] things = row.split(",");
        //PermTracker.toggled + ","say in chat + PermTracker.guiLocation[0] + "," + PermTracker.guiLocation[1] + PermTracker.align + "," + PermTracker.color
        return PitUtils.isInteger(things[2]) && //guiLocation
                PitUtils.isInteger(things[3]) && //guiLocation
                PitUtils.isInteger(things[5]) && //color
                PitUtils.isBool(things[0]) &&  //toggled
                PitUtils.isInteger(things[1]) && //livesToAlert
                things[4].equalsIgnoreCase("left") || things[4].equalsIgnoreCase("right");
    }

    static boolean setVars(String line) {
        try {
            if (isValid(line)) {
                String[] row = line.split(",");
                toggled = row[0].equalsIgnoreCase("true");
                livesToAlert = Integer.parseInt(row[1]);
                guiLocation = new int[]{Integer.parseInt(row[2]), Integer.parseInt(row[3])};
                align = row[4];
                color = Integer.parseInt(row[5]);
                PitUtils.saveLogInfo("Low Life Mystics save info successfully loaded\n");
                return true;
            }
            PitUtils.saveLogInfo("Low Life Mystics save info failed: here was the faulty data " + line + "\n");
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("Low Life Mystics save info had some kind of error\n");
            return false;
        }
    }
}
