package TelegramUI.Events;

import NukkitDB.Provider.MongoDB;
import PlayerAPI.Overrides.PlayerAPI;
import TelegramUI.API.Telegram;
import TelegramUI.Main;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.TextFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinEvent implements Listener {

    private Main plugin;

    public JoinEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler()
    public void onJoin(PlayerJoinEvent event) {
        String collection = plugin.getConfig().getString("collection");
        PlayerAPI player = (PlayerAPI) event.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (MongoDB.getDocument(MongoDB.getCollection(collection), "uuid", uuid) == null) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("uuid", uuid);
            objectMap.put("mail", new ArrayList<>());
            MongoDB.insertOne(objectMap, MongoDB.getCollection(collection));

        }
        plugin.getServer().getScheduler().scheduleDelayedTask(new DelayedTask(player, collection), 100, true);
    }
}

class DelayedTask extends Task {

    PlayerAPI player;
    String collection;

    DelayedTask(PlayerAPI player, String collection) {
        this.player = player;
        this.collection = collection;
    }

    @Override
    public void onRun(int currentTick) {
        Map<String, Object> query = MongoDB.getDocument(MongoDB.getCollection(collection), "uuid", player.getUuid());
        List<List<Object>> mailList = (List<List<Object>>) Telegram.getTelegramPlayerData(player.getUuid()).get("mail");
        int amount;

        if (mailList.size() <= 0) {
            amount = 0;
        } else {
            int i = 0;
            for (List<Object> mailData : mailList) {
                if (!(boolean) mailData.get(0)) {
                    i++;
                }
            }
            amount = i;
        }

        player.sendMessage("\u2709" + TextFormat.ITALIC + TextFormat.GRAY + " You have " + TextFormat.WHITE + TextFormat.BOLD + amount + TextFormat.RESET + TextFormat.ITALIC + TextFormat.GRAY + " unread telegrams...\n/telegram view");
    }
}
