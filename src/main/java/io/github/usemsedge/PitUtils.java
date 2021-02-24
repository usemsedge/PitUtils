package io.github.usemsedge;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import scala.tools.nsc.doc.model.ModelFactory;

@Mod(modid = PitUtils.MODID, version = PitUtils.VERSION)
public class PitUtils {
    static final String MODID = "PitUtils";
    static final String VERSION = "1.0";
    private static final String PIT_UTILS_PATH = "PitUtils.dat";
    static boolean loggedIn = false;
    static boolean usingLabyMod = false;
    static boolean isInPit = false;

    static ArrayList<String> permList = new ArrayList<>();

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

    static void scheduleFileSave(int delay) {
        if (autoSaveExecutor != null && !autoSaveExecutor.isShutdown()) {
            autoSaveExecutor.shutdownNow();
        }

        autoSaveExecutor = Executors.newSingleThreadScheduledExecutor();
        autoSaveExecutor.scheduleAtFixedRate(() -> {
            if (loggedIn && isInPit) {
                saveInfo();
            }
        }, 0, delay, TimeUnit.SECONDS);

    }

    static String getNBT(ItemStack item) {
        return item.getTagCompound().getTagList("lore", 8).getStringTagAt(1);
    }

    static List<String> getPlayerNames() {
        List<String> x = new ArrayList<>();
        for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {
            x.add(player.getName());
        }
        return x;
    }

    static void saveInfo() {
        new Thread(() -> {
            File util_file = new File(PIT_UTILS_PATH);
            StringBuilder permListString = new StringBuilder();
            for (String s : permList) {
                permListString.append(s).append(",");
            }


            try {
                FileWriter fw = new FileWriter(util_file, false);
                fw.write(permListString + ";" +
                        MysticDropCounter.toggled + "," + MysticDropCounter.killCount + "," + MysticDropCounter.mysticDrops + "," + MysticDropCounter.sinceLastMysticDrop
                                + "," + MysticDropCounter.guiLocation[0] + "," + MysticDropCounter.guiLocation[1] + "," + MysticDropCounter.align + "," + MysticDropCounter.color + ";" +

                         Cooldown.toggled + "," + Cooldown.guiLocation[0] + "," + Cooldown.guiLocation[1] + "," + Cooldown.align + "," + Cooldown.color + ";" +

                         AutoL.toggled + "," + AutoL.onBan + "," + AutoL.onPermList + "," + AutoL.onBountyClaimed + ";" +

                         PermTracker.toggled + "," + PermTracker.sayInChat + "," + PermTracker.guiLocation[0] + "," + PermTracker.guiLocation[1] + "," + PermTracker.align + "," + PermTracker.color + ";" +

                         DarkChecker.toggled + "," + DarkChecker.sayInChat + "," + DarkChecker.guiLocation[0] + "," + DarkChecker.guiLocation[1] + "," + DarkChecker.align + "," + DarkChecker.color + ";"
                );
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

    static boolean isBool(String s) {
        return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
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

    static void messagePlayer(EntityPlayer player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

    static boolean checkUsername(String name) {
        return name.matches("^[A-Za-z0-9_]*$");
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        ClientCommandHandler.instance.registerCommand(new PitUtilsCommand());
        MinecraftForge.EVENT_BUS.register(new PitUtilsEventHandler());
        if (new File(PIT_UTILS_PATH).isFile()) {
            try {
                String[] content = new BufferedReader(new FileReader(PIT_UTILS_PATH)).readLine().split(";");
                saveLogInfo("Opened file, her was the content of the file");
                for (String s : content) {
                    saveLogInfo(s + "\n");
                }

                String[] c = content[0].split(",");
                PitUtils.permList.addAll(Arrays.asList(c));

                saveLogInfo("perm list set \n");
                MysticDropCounter.setVars(content[1]);
                saveLogInfo("mystic drop set \n");
                Cooldown.setVars(content[2]);
                saveLogInfo("cooldown set \n");
                AutoL.setVars(content[3]);
                saveLogInfo("autol set \n");
                PermTracker.setVars(content[4]);
                saveLogInfo("perm tracker set");
                DarkChecker.setVars(content[5]);
                saveLogInfo("darkchecker set");

            }
            catch (Exception e) {
                e.printStackTrace();
                saveLogInfo("opening data file failed\n");
                saveInfo();
            }
        }
        else {
            saveLogInfo("no data file exists\n");
            saveInfo();
        }
        scheduleFileSave(120);
    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {usingLabyMod = Loader.isModLoaded("labymod");}
}