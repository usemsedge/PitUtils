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

public class PitUtilsEventHandler {
    private boolean firstJoin = true;
    private FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
    private boolean tempSuspend = false;
    private int tick = 0;

    private int timeOfLastEggAlert = 0;
    private int timeOfLastSteakAlert = 0;
    private int timeOfLastAuraAlert = 0;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPlayerClick(PlayerInteractEvent e) throws IOException {
        if (Cooldown.toggled) {
            Cooldown.onPlayerClick();
        }

    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onChatMessageRecieved(ClientChatReceivedEvent e) {
        String msg = e.message.getUnformattedText();
        if (MysticDropCounter.toggled) {
            MysticDropCounter.onChatMessageReceived(msg);
        }
        if (AutoL.toggled) {
            AutoL.checkIfSayL(msg);
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

                List players = PitUtils.getPlayers();
                PitUtils.saveLogInfo(players.toString());
                tick = 0;
            }

            Cooldown.adjustCooldowns();
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

        if (MysticDropCounter.toggled) {
            MysticDropCounter.renderStats(renderer);
        }
        if (Cooldown.toggled) {
            Cooldown.renderStats(renderer);
        }
    }

}