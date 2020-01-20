package TelegramUI.Events;

import TelegramUI.Handler.DatabaseHandler;
import TelegramUI.Main;
import cn.nukkit.Player;
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
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (DatabaseHandler.query(uuid, "uuid") == null) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("uuid", uuid);
            objectMap.put("name", player.getName().toLowerCase());
            objectMap.put("mail", new ArrayList<>());
            DatabaseHandler.createNew(objectMap);
        }
        plugin.getServer().getScheduler().scheduleDelayedTask(new DelayedTask(player), 100, true);
    }
}

class DelayedTask extends Task {

    Player player;

    DelayedTask(Player player) {
        this.player = player;
    }

    @Override
    public void onRun(int currentTick) {
        Map<String, Object> query = DatabaseHandler.query(player.getUniqueId().toString(), "uuid");
        List<List<Object>> mailList = (List<List<Object>>) query.get("mail");
        int amount;

        if (mailList.size() <= 0) {
            amount = 0;
        } else {
            int i = 0;
            for (List<Object> mailData : mailList) {
                if ((boolean) mailData.get(0)) {
                    i++;
                }
            }
            amount = i;
        }


        player.sendMessage("\u2709" + TextFormat.ITALIC + TextFormat.GRAY + " You have " + TextFormat.WHITE + TextFormat.BOLD + amount + TextFormat.RESET + TextFormat.ITALIC + TextFormat.GRAY + " unread telegrams...\n/telegram view");
    }
}
