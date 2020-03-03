package TelegramUI.API;

import NukkitDB.NukkitDB;
import PlayerAPI.Overrides.PlayerAPI;
import TelegramUI.Main;
import cn.nukkit.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Telegram {

    public static Map<String, Object> getTelegramPlayerData(String uuid) {
        String database = Main.getInstance().getConfig().getString("database");
        String collection = Main.getInstance().getConfig().getString("collection");
        return NukkitDB.query(
                uuid, "uuid", database, collection
        );
    }

    public static List<Object> getMail(String uuid) {
        return (List<Object>) getTelegramPlayerData(uuid).get("mail");
    }

    public static void send(Map<String, Object> to, PlayerAPI from, String subject, String content) {
        String database = Main.getInstance().getConfig().getString("database");
        String collection = Main.getInstance().getConfig().getString("collection");
        if (getMail(to.get("uuid").toString()) != null) {
            List<Object> telegram = getMail(to.get("uuid").toString());
            List<Object> mailData = new ArrayList<>();
            mailData.add(0, false);
            mailData.add(1, from.getAlias());
            mailData.add(2, subject);
            mailData.add(3, content);
            telegram.add(mailData);
            NukkitDB.updateDocument(
                    to.get("uuid").toString(), "uuid", "mail", telegram, database, collection
            );
        }
    }

    public static void delete(Player player, int id) {
        List<Object> mailList = getMail(player.getUniqueId().toString());
        
        for (int i = 0; i < mailList.size(); i++) {
            List<Object> mailData = (List<Object>) mailList.get(i);
            System.out.println(mailList.indexOf(mailData));
        }
    }

}
