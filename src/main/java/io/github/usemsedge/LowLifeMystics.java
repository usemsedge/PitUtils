package io.github.usemsedge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import java.util.*;

public class LowLifeMystics {
    static int[] guiLocation = new int[]{300, 50};
    static boolean toggled = true;
    static int color = 0x00ffff;
    static String align = "left";

    static int livesToAlert = 3;
    static int lowLifePants = 0, lowLifeSwords = 0, lowLifeBows = 0, lowLifeArmor = 0, lowLifeItems = 0;


    static int getLives(ItemStack item) {
        String itemNBT = PitUtils.getLore(item);
        String itemName = item.getDisplayName();
        if (itemName.contains("Tier") ||
            itemName.contains("Golden Helmet") ||
            itemName.contains("Archangel") ||
            itemName.contains("Armageddon")) {
            int slash = itemNBT.indexOf("/");
            int colon = itemNBT.indexOf(":");
            String lives = itemNBT.substring(colon + 4, slash - 2); //lives look like      Lives: §c12§8/46 (§8 is a color code)
            return Integer.parseInt(lives);
        }
        else {
            return Integer.MAX_VALUE; //it's fresh
        }

    }

    static ArrayList<String> checkAllLives() {
        ArrayList<ItemStack> inv = new ArrayList<>();
        ItemStack[] invItems = Minecraft.getMinecraft().thePlayer.inventory.mainInventory.clone();
        Collections.addAll(inv, invItems);
        ItemStack[] armorItems = Minecraft.getMinecraft().thePlayer.inventory.armorInventory.clone();
        Collections.addAll(inv, armorItems);
        
        ItemStack item;
        String itemName;
        int lives;
        StringBuilder enchantName = new StringBuilder();
        ArrayList<String> itemNames = new ArrayList<>();
        HashMap<String, Integer> enchants = new HashMap<>();
        for (int i = 0; i < inv.size(); i++) {
            try {
                item = inv.get(i);
                itemName = item.getDisplayName();
                lives = getLives(item);
                                                                  
                if (lives <= livesToAlert) {

                    enchantName = new StringBuilder();
                    if (itemName.contains("Arch")) {
                        enchantName.append("Archangel Chestplate");
                    }
                    else if (itemName.contains("Arma")) {
                        enchantName.append("Armageddon Boots");
                    }
                    else if (itemName.contains("Golden")) {
                        enchantName.append("Golden Helmet");
                    }
                    else {
                            //an actual mystic item
                        enchants = PitUtils.getEnchants(item);
                        enchantName.append(itemName.substring(2)); //format code
                        for (String key : enchants.keySet()) {
                            if (PitUtils.enchantsLoaded) {
                                if (PitUtils.useShortEnchants) {
                                    enchantName.append(" ").append(PitUtils.enchants_short.get(key)).append(" ").append(enchants.get(key));
                                }
                                else {
                                    enchantName.append(" ").append(PitUtils.enchants.get(key)).append(" ").append(enchants.get(key));
                                }
                            }
                            else {
                                enchantName.append(" ").append(key).append(" ").append(enchants.get(key));
                            }
                        }
                    }
                    enchantName.append(" is on ").append(lives).append(" lives");
                    itemNames.add(enchantName.toString());
                }
            }
            catch (Exception e) {
                //no item int hat slot
            }

        }
        return itemNames;

    }
    static void renderStats(FontRenderer renderer) {
        ArrayList<String> strings = checkAllLives();
        strings.add(0, "Low Life Mystics");

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
