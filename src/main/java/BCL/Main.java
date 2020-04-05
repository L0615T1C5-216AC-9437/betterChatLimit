package BCL;

import arc.Core;
import arc.Events;
import arc.util.CommandHandler;
import arc.util.Log;
import mindustry.entities.traits.Entity;
import mindustry.entities.type.Player;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.plugin.Plugin;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static mindustry.Vars.*;

public class Main extends Plugin {
    private JSONObject alldata;
    private JSONObject data; //token, channel_id, role_id
    private Thread mt = Thread.currentThread();
    private int messages;
    private int timespan;
    private HashMap<String, Integer> pcl = new HashMap<>();

    public Main() throws InterruptedException {
        try {
            String pureJson = Core.settings.getDataDirectory().child("mods/settings.json").readString();
            alldata = new JSONObject(new JSONTokener(pureJson));
            if (!alldata.has("bcl")){
                Log.err("settings.json missing bcl");
                //this.makeSettingsFile("settings.json");
                return;
            } else {
                data = alldata.getJSONObject("bcl");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("File not found: config\\mods\\settings.json")){
                Log.err("bcl: settings.json file is missing.");
                return;
            } else {
                Log.err("bcl: Error reading JSON.");
                e.printStackTrace();
                return;
            }
        }

        Thread A = new Thread() {
            public void run() {
                Log.info("BCL  started successfully!");
                try {
                    Thread.sleep(15 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (mt.isAlive()) {
                    try {
                        TimeUnit.SECONDS.sleep((long) data.getInt("timespan")/data.getInt("messages"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (Player player : playerGroup.all()) {
                        if (pcl.containsKey(player.uuid)) {
                            if (pcl.get(player.uuid) > 0) {
                                pcl.replace(player.uuid, pcl.get(player.uuid) - 1);
                            }
                        } else {
                            player.getInfo().timesKicked--;
                            player.con.kick("ERROR - BCL - not in pcl Array - please report this issue", 1);
                            Log.err("BCL: Player not in pcl.");
                        }
                    }
                }
                Log.info("BCL Stopped.");
            }
        };
        if (data.has("messages") && data.has("timespan")) {
            messages = data.getInt("messages");
            timespan = data.getInt("timespan");
            Log.info("BCL set to " + messages + " messages every " + timespan + " seconds.");
            A.setDaemon(false);
            A.start();
            Log.info("Attempting to start BCL in 1 min...");
        } else {
            if (!data.has("messages")) {
                Log.err("BCL missing key `messages`");
            }
            if (!data.has("timespan")) {
                Log.err("BCL missing key `timespan`");
            }
            if (!data.has("messages") || !data.has("timespan")) {
                Log.info("BCL not started");
                return;
            }
        }

        Events.on(EventType.PlayerJoin.class, event -> {
            Player player = event.player;
            pcl.put(player.uuid, 0);
        });
        Events.on(EventType.PlayerLeave.class, event -> {
            Player player = event.player;
            pcl.remove(player.uuid);
        });
        Events.on(EventType.PlayerChatEvent.class, event -> {
            Player player = event.player;
            if (pcl.get(player.uuid) > data.getInt("messages")) player.sendMessage("You've sent too many messages! calm down.\nYou can send a message in 3 seconds.");

        });

        Events.on(EventType.ServerLoadEvent.class, event -> {
            netServer.admins.addChatFilter((player, text) -> {
                if (!data.has("messages")) {
                    Call.sendMessage("settings.json missing key `messages`.");
                } else if (pcl.get(player.uuid) > data.getInt("messages")) {
                    return null;
                }
                pcl.replace(player.uuid, pcl.get(player.uuid)+1);
                return text;
            });
        });
    }
}