package io.github.usemsedge;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = PitUtils.MODID, version = PitUtils.VERSION)
public class PitUtils {
    static final String MODID = "PitUtils";
    static final String VERSION = "1.0";
    private static final String PIT_UTILS_PATH = "PitUtils.dat";
    static boolean loggedIn = false;
    static boolean usingLabyMod = false;
    static boolean toggledMyst = true;
    static boolean toggledCooldown = true;
    static int color = 0x55FFFF;
    static String align = "left";
    static float killCount = 0;
    static float mysticDrops = 0;
    static float sinceLastMysticDrop = 0;
    static boolean isInPit = false;
    static int[] guiLocation = new int[]{2, 2};
    static int steakCooldownInTicks = 200;
    static int auraCooldownInTicks = 300;
    static int eggCooldownInTicks = 200;
    static int currentSteakCooldownInTicks = 0;
    static int currentAuraCooldownInTicks = 0;
    static int currentEggCooldownInTicks = 0;
    static boolean displayCooldownInTicks = false;
    private static ScheduledExecutorService autoSaveExecutor;

    static String LOG_PATH = "PitUtils.log";


    static void saveLogInfo(String log) {
        new Thread(() -> {
            File mystic_file = new File(LOG_PATH);
            try {
                FileWriter fw = new FileWriter(mystic_file, true);
                fw.write(log);
                fw.close();
            }

            catch(IOException e){
                e.printStackTrace();
            }}).start();
    }

    static void scheduleFileSave(boolean toggle, int delay) {
        if (autoSaveExecutor != null && !autoSaveExecutor.isShutdown()) {
            autoSaveExecutor.shutdownNow();
        }
        if (toggle) {
            autoSaveExecutor = Executors.newSingleThreadScheduledExecutor();
            autoSaveExecutor.scheduleAtFixedRate(() -> {
                if (loggedIn && isInPit) {
                    saveInfo(killCount, mysticDrops, sinceLastMysticDrop, steakCooldownInTicks,
                            auraCooldownInTicks);
                }
            }, 0, delay, TimeUnit.SECONDS);
        }
    }

    static void saveInfo(float kills, float drops, float last, int steak, int aura) {
        new Thread(() -> {
            File mystic_file = new File(PIT_UTILS_PATH);
            try {
                FileWriter fw = new FileWriter(mystic_file, false);
                fw.write((int)kills + "," + (int)drops + "," + (int)last + "," + guiLocation[0]
                        + "," + guiLocation[1] + "," + Integer.toHexString(color) + "," + align
                        + "," + steak + "," + aura);
                fw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length() - 1; i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }
        return true;
    }

    static List<String> getSidebarLines() {
        List<String> lines = new ArrayList<>();
        Scoreboard sb = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (sb == null) {
            return lines;
        }

        ScoreObjective obj = sb.getObjectiveInDisplaySlot(1);

        if (obj == null) {
            return lines;
        }

        Collection<Score> scores = sb.getSortedScores(obj);
        List<Score> list = Lists.newArrayList(scores.stream()
                .filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName()
                        .startsWith("#")).collect(Collectors.toList())) ;

        if (list.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
        }
        else {
            scores = list;
        }

        for (Score score : scores) {
            ScorePlayerTeam team = sb.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
        }

        return lines;
    }

    static void chat(EntityPlayer player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        ClientCommandHandler.instance.registerCommand(new PitUtilsCommand());
        MinecraftForge.EVENT_BUS.register(new io.github.usemsedge.EventHandler());
        if (new File(PIT_UTILS_PATH).isFile()) {
            try {
                String[] input = new BufferedReader(new FileReader(PIT_UTILS_PATH)).readLine().split(",");
                if (input.length == 9 && isInteger(input[0]) && isInteger(input[1]) && isInteger(input[2])
                        && isInteger(input[3]) && isInteger(input[4]) && isInteger(input[5], 16)
                        && isInteger(input[7]) && isInteger(input[8])) {
                    killCount = Integer.parseInt(input[0]);
                    mysticDrops = Integer.parseInt(input[1]);
                    sinceLastMysticDrop = Integer.parseInt(input[2]);
                    guiLocation = new int[]{Integer.parseInt(input[3]), Integer.parseInt(input[4])};
                    color = Integer.parseInt(input[5], 16);
                    align = input[6];
                    auraCooldownInTicks = Integer.parseInt(input[8]);
                    steakCooldownInTicks = Integer.parseInt(input[7]);

                    saveLogInfo("data file loading actually works\n");
                }
                else {
                    saveInfo(0, 0, 0, 10, 15);
                    saveLogInfo("input data file is wrong\n");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                saveLogInfo("opening data file failed\n");
            }
        }
        else {
            saveLogInfo("no data file exists\n");
            saveInfo(0, 0, 0, 10, 15);
        }
        scheduleFileSave(true, 120);
    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {usingLabyMod = Loader.isModLoaded("labymod");}
}