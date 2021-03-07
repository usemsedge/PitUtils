package io.github.usemsedge;

import java.io.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class PitUtilsEventHandler {
    private boolean firstJoin = true;
    private FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
    private int tick = 0;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPlayerClick(PlayerInteractEvent e) {
        if (Cooldown.toggled) {
            Cooldown.onPlayerClick(e);
        }

    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onChatMessageRecieved(ClientChatReceivedEvent e) {
        String msg = e.message.getUnformattedText();
        MysticDropCounter.onChatMessageReceived(msg);

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
                PermTracker.permedPlayersInServer = PermTracker.findPermedPlayersInServer();
                DarkChecker.playersUsingDarksInServer = DarkChecker.checkForDarks();
                CountingPlayers.checkGear();
                CountingPlayers.updateCount();
                LowLifeMystics.checkAllLives();
                

                tick = 0;
            }

            Cooldown.adjustCooldowns();
        }
    }

    @SubscribeEvent
    public void onPlayerJoinevent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        PitUtils.loggedIn = true;
        new ScheduledThreadPoolExecutor(1).schedule(() -> Minecraft.getMinecraft().thePlayer
                .addChatMessage(new ChatComponentText(EnumChatFormatting.RED +
                "Downloads not from github.com/usemsedge/mystic-counter are RATs.\n" + EnumChatFormatting.GREEN + "Type /pit help to get a list of commands.")), 3, TimeUnit.SECONDS);
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
        if (MysticDropCounter.toggled) {
            MysticDropCounter.renderStats(renderer);
        }
        if (Cooldown.toggled) {
            Cooldown.renderStats(renderer);
        }
        if (PermTracker.toggled) {
            PermTracker.renderStats(renderer);
        }
        if (DarkChecker.toggled) {
            DarkChecker.renderStats(renderer);
        }
        if (CountingPlayers.toggled) {
            CountingPlayers.renderStats(renderer);
        }
        if (LowLifeMystics.toggled) {
            LowLifeMystics.renderStats(renderer);
        }
    }
}