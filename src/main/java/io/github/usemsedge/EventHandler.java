package io.github.usemsedge;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class EventHandler {
    private boolean firstJoin = true;
    private FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
    private boolean tempSuspend = false;
    private int tick = 0;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPlayerClick(PlayerInteractEvent e) throws IOException {
        if (PitUtils.toggledCooldown) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            //if (player.getHeldItem().toString())
            //PitUtils.saveLogInfo(player.getHeldItem().toString() + "\n");

            String name;
            try {
                name = player.getHeldItem().getDisplayName();
            }
            catch (Exception ex) {
                FileWriter fw = new FileWriter("exception.txt", false);
                PrintWriter pw = new PrintWriter(fw);
                ex.printStackTrace(pw);
                return;
            }


            PitUtils.saveLogInfo(name + "\n");
            if (name.equalsIgnoreCase("AAA-Rated Steak")) {
                PitUtils.saveLogInfo("Steak Used\n");
                if (PitUtils.currentSteakCooldownInTicks < PitUtils.steakCooldownInTicks) {
                    PitUtils.chat(player, EnumChatFormatting.RED + "Steak on cooldown!");
                    //steaks on cooldown
                }
                else {
                    PitUtils.currentSteakCooldownInTicks = PitUtils.steakCooldownInTicks;
                }

            }
            else if (name.equalsIgnoreCase("Aura of Protection")) {
                PitUtils.saveLogInfo("Aura Used");
                if (PitUtils.currentAuraCooldownInTicks < PitUtils.auraCooldownInTicks) {
                    //Aura cooldown
                    PitUtils.chat(player, EnumChatFormatting.RED + "Aura on cooldown!");
                }
                else {
                    PitUtils.currentAuraCooldownInTicks = PitUtils.auraCooldownInTicks;
                }

            }
            else if (name.equalsIgnoreCase("First-Aid Egg")) {
                PitUtils.saveLogInfo("Egg Used");
                if (PitUtils.currentEggCooldownInTicks < PitUtils.eggCooldownInTicks) {
                    //First aid egg
                    PitUtils.chat(player, EnumChatFormatting.RED + "Egg on cooldown!");
                }
                else {
                    PitUtils.currentEggCooldownInTicks = PitUtils.eggCooldownInTicks;
                }
            }
            else {
                PitUtils.saveLogInfo(name + " used\n");
            }
        }

    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onChatMessageRecieved(ClientChatReceivedEvent e) {
        String msg = e.message.getUnformattedText();
        long d = System.currentTimeMillis();

        if (msg.contains("MYSTIC ITEM!") && !msg.contains(":")) {
            PitUtils.mysticDrops++;
            PitUtils.sinceLastMysticDrop = 0;
        }

        else if (msg.contains("KILL!") && !msg.contains(":")) {
            PitUtils.killCount += 0.5;
            PitUtils.sinceLastMysticDrop += 0.5;

        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            tick++;
            if (tick > 9 && Minecraft.getMinecraft() != null
                && Minecraft.getMinecraft().thePlayer != null) {
                if (Minecraft.getMinecraft().getCurrentServerData() != null
                    && Minecraft.getMinecraft().getCurrentServerData().serverIP != null
                    && Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1)
                    != null) {
                    PitUtils.isInPit = (stripString(StringUtils.stripControlCodes(
                            Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1)
                                    .getDisplayName())).contains("THE HYPIXEL PIT") && Minecraft.getMinecraft()
                            .getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net"));
                }

                tick = 0;
            }
            if (PitUtils.currentAuraCooldownInTicks > 0) {
                PitUtils.currentAuraCooldownInTicks--;
            }
            if (PitUtils.currentSteakCooldownInTicks > 0) {
                PitUtils.currentSteakCooldownInTicks--;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoinevent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        PitUtils.loggedIn = true;
        new ScheduledThreadPoolExecutor(1).schedule(() -> {
            Minecraft.getMinecraft().thePlayer
                    .addChatMessage(new ChatComponentText(EnumChatFormatting.RED +
                    "Downloads not from github.com/usemsedge/mystic-counter are RATs.\n" + EnumChatFormatting.GREEN + "Type /pit to get a list of commands."));
        }, 3, TimeUnit.SECONDS);
    }

    @SubscribeEvent
    public void OnPlayerLeaveEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        PitUtils.loggedIn = false;
    }

    @SubscribeEvent
    public void renderLabymodOverlay(RenderGameOverlayEvent event) {
        if (event.type == null && isUsingLabymod() && PitUtils.isInPit) {
            renderStats();
        }
    }

    @SubscribeEvent
    public void renderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
        if ((event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE
                || event.type == RenderGameOverlayEvent.ElementType.JUMPBAR) && PitUtils.isInPit
                && !isUsingLabymod()) {
            renderStats();
        }
    }

    private String stripString(String s) {
        char[] nonValidatedString = StringUtils.stripControlCodes(s).toCharArray();
        StringBuilder validated = new StringBuilder();
        for (char a : nonValidatedString) {
            if ((int) a < 127 && (int) a > 20) {
                validated.append(a);
            }
        }
        return validated.toString();
    }

    private boolean isUsingLabymod() {
        return PitUtils.usingLabyMod;
    }

    private void renderStats() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();

        if (PitUtils.toggledCooldown) {

        }
        if (PitUtils.toggledMyst) {
            String killsPerMystic =
                    "Kills/Mystic: " + ((PitUtils.mysticDrops == 0) ? PitUtils.mysticDrops
                            : new DecimalFormat("#.##")
                    .format(PitUtils.killCount / (PitUtils.mysticDrops * 1.0d)));
            String kills = "Kills: " + (int)PitUtils.killCount;
            String mystics = "Mystic Drops: " + (int)PitUtils.mysticDrops;
            String lastMystic = "Kills since last Mystic Drop: " + (int)PitUtils.sinceLastMysticDrop;
            String eggCD, steakCD, auraCD;
            if (PitUtils.displayCooldownInTicks) {
                eggCD = "First-Aid Egg cool down: " + PitUtils.currentEggCooldownInTicks + " ticks";
                steakCD = "AAA-Rated Steak cool down: " + PitUtils.currentSteakCooldownInTicks + "ticks";
                auraCD = "Aura Of Protection cool down: " + PitUtils.currentAuraCooldownInTicks + "ticks";
            }
            else {
                eggCD = "First-Aid Egg cool down: " + PitUtils.currentEggCooldownInTicks + " seconds";
                steakCD = "AAA-Rated Steak cool down: " + PitUtils.currentSteakCooldownInTicks + "seconds";
                auraCD = "Aura Of Protection Cool down: " + PitUtils.currentAuraCooldownInTicks + "seconds";
            }

            String longest = auraCD;
            //auraCD is the longest string, because you are not getting a 20 digit number of kills
            if (PitUtils.align.equals("right")) {
                renderer.drawString(mystics, PitUtils.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(mystics),
                        PitUtils.guiLocation[1], PitUtils.color, true);
                renderer.drawString(kills, PitUtils.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(kills),
                        PitUtils.guiLocation[1] + renderer.FONT_HEIGHT, PitUtils.color, true);
                renderer.drawString(killsPerMystic, PitUtils.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(killsPerMystic),
                        PitUtils.guiLocation[1] + renderer.FONT_HEIGHT * 2, PitUtils.color, true);
                renderer.drawString(lastMystic, PitUtils.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(lastMystic),
                        PitUtils.guiLocation[1] + renderer.FONT_HEIGHT * 3, PitUtils.color, true);

                renderer.drawString(steakCD, PitUtils.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(steakCD),
                        PitUtils.guiLocation[1] + renderer.FONT_HEIGHT * 5, PitUtils.color, true);
                renderer.drawString(eggCD, PitUtils.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(eggCD),
                        PitUtils.guiLocation[1] + renderer.FONT_HEIGHT * 6, PitUtils.color, true);
                renderer.drawString(auraCD, PitUtils.guiLocation[0] +
                                renderer.getStringWidth(longest) -
                                renderer.getStringWidth(auraCD),
                        PitUtils.guiLocation[1] + renderer.FONT_HEIGHT * 7, PitUtils.color, true);

            }
            else {
                renderer.drawString(mystics, PitUtils.guiLocation[0],
                            PitUtils.guiLocation[1], PitUtils.color, true);
                renderer.drawString(kills, PitUtils.guiLocation[0],
                            PitUtils.guiLocation[1] + renderer.FONT_HEIGHT, PitUtils.color, true);
                renderer.drawString(killsPerMystic, PitUtils.guiLocation[0],
                            PitUtils.guiLocation[1] + renderer.FONT_HEIGHT * 2, PitUtils.color, true);
                renderer.drawString(lastMystic, PitUtils.guiLocation[0],
                            PitUtils.guiLocation[1] + renderer.FONT_HEIGHT * 3, PitUtils.color, true);

                renderer.drawString(steakCD, PitUtils.guiLocation[0],
                        PitUtils.guiLocation[1] + renderer.FONT_HEIGHT * 5, PitUtils.color, true);
                renderer.drawString(eggCD, PitUtils.guiLocation[0],
                            PitUtils.guiLocation[1] + renderer.FONT_HEIGHT * 6, PitUtils.color, true);
                renderer.drawString(auraCD, PitUtils.guiLocation[0],
                            PitUtils.guiLocation[1] + renderer.FONT_HEIGHT * 7, PitUtils.color, true);
                 }
        }
    }

}