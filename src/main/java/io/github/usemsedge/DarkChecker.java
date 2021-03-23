package io.github.usemsedge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DarkChecker {
    static final List<String> DARK_ENCHANTS = Arrays.asList("spite", "sanguisuge", "misery", "needless_suffering", "mind_assault", "grim_reaper", "hedge_fund", "golden_handcuffs", "heartripper", "venom", "nostalgia", "lycanthropy");

    static boolean toggled = true;
    static int[] guiLocation = new int[]{100, 2};
    static boolean sayInChat = false;
    static String align = "left";
    static int color = 0x00ffff;

    static List<List<String>> playersUsingDarksInServer = new ArrayList<>();

    static List<List<String>> checkForDarks() {
        //edit
        ItemStack item;
        List<List<String>> playersUsingDarks = new ArrayList<>();
        List<EntityPlayer> players = Minecraft.getMinecraft().theWorld.playerEntities;
        List<String> p;
        for (EntityPlayer player : players) {
            try {
                item = player.inventory.armorInventory[1]; //pants?? third slot in r
                if ((item.getDisplayName().contains("Dark") || item.getDisplayName().contains("Evil")) && item.getDisplayName().contains("Tier")) {
                    //enchanted dark pants, must have tier I in it
                    p = new ArrayList<>();
                    p.add(player.getName());
                    if (item.getDisplayName().contains("Tier I ")) {
                        p.add("Plain Somber");
                    }
                    else {
                        for (String enchant : DARK_ENCHANTS) {
                            PitUtils.saveLogInfo("trying to see if item has " + enchant + "\n");
                            if (PitUtils.getEnchants(item).toString().contains(enchant)) {
                                p.add(enchant);
                                break;
                            }
                        }
                        if (p.size() == 1) {
                            p.add("Other");
                        }
                    }
                    playersUsingDarks.add(p);

                }
            }
            catch (Exception e) {} //no pants
        }
        return playersUsingDarks;
    }

    static void renderStats(FontRenderer renderer) {
        List<List<String>> e = new ArrayList<>(playersUsingDarksInServer);
        List<String> strings = new ArrayList<>();
        strings.add("List of dark users");

        String longest = "";
        for (List<String> player : e) {
            strings.add(player.get(0) + ": " + player.get(1));
            if (renderer.getStringWidth(player.get(0) + ": " + player.get(1)) > renderer.getStringWidth(longest)) {
                longest = player.get(0) + ": " + player.get(1);
            }
        }

        if (renderer.getStringWidth("List of dark users") > renderer.getStringWidth(longest)) {
            longest = "List of dark users";
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
                PitUtils.saveLogInfo("Dark Tracker save info successfully loaded\n");
                return true;
            }
            PitUtils.saveLogInfo("Dark Tracker save info failed: here was the faulty data " + line + "\n");
            return false;
        }
        catch (Exception e) {
            PitUtils.saveLogInfo("Dark Tracker save info had some kind of error\n");
            return false;
        }
    }
}
