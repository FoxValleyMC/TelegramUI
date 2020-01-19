package TelegramUI.Events;

import TelegramUI.Handler.DatabaseHandler;
import TelegramUI.Main;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.utils.TextFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormResponseEvent implements Listener {

    private Main plugin;

    public FormResponseEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler()
    public void onFormSubmit(PlayerFormRespondedEvent event) {
        if (event.getResponse() instanceof FormResponseModal) {
            FormResponseModal response = (FormResponseModal) event.getResponse();
            if (event.getFormID() == 0) {
                int button = response.getClickedButtonId();
                switch (button) {
                    case 0:
                        event.getPlayer().showFormWindow(plugin.formWindowMap.get("message_send"), 2);
                    case 1:
                        event.getPlayer().showFormWindow(plugin.formWindowMap.get("telegram_send"), 3);
                        break;
                }
            }
            return;
        }

        if (event.getResponse() instanceof FormResponseCustom) {
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            if (event.getFormID() == 2) {
                String args0 = response.getInputResponse(0);
                String args1 = response.getInputResponse(1);
                Player sender = event.getPlayer();
                Player receiver = plugin.getServer().getPlayer(args0);
                sender.sendMessage(plugin.prefix);
                if (receiver != null) {
                    sender.sendMessage(TextFormat.ITALIC+""+TextFormat.GREEN+"TO=>"+TextFormat.GRAY+receiver.getName());
                    sender.sendMessage(TextFormat.ITALIC+""+TextFormat.RED+"FROM=>"+TextFormat.GRAY+sender.getName());
                    sender.sendMessage(args1);
                    receiver.sendMessage(plugin.prefix);
                    receiver.sendMessage(TextFormat.ITALIC+""+TextFormat.GREEN+"TO=>"+TextFormat.GRAY+receiver.getName());
                    receiver.sendMessage(TextFormat.ITALIC+""+TextFormat.RED+"FROM=>"+TextFormat.GRAY+sender.getName());
                    receiver.sendMessage(args1);
                } else {
                    sender.sendMessage(args1 + " Does not appear to be online!");
                }
                return;
            }

            if (event.getFormID() == 3) {
                String name = response.getInputResponse(0).trim();
                String title = response.getInputResponse(1);
                String message = response.getInputResponse(2);
                Player player = event.getPlayer();
                String sender = player.getName();
                String uuid = null;
                Map<String, Object> objectMap;
                List<Object> telegram = null;
                List<Object> mailData = new ArrayList<>();

                String noPLayer = TextFormat.RED+"Player "+TextFormat.GOLD+name+TextFormat.RED+" was not found in our database...";

                if (DatabaseHandler.query(name.toLowerCase(), "name") == null) {
                    try {
                        Map<String, Object> aliasMap = AliasPro.AliasPro.getPlayer(name);
                        if (aliasMap != null) {
                            objectMap = DatabaseHandler.query(aliasMap.get("uuid").toString(), "uuid");
                            if (objectMap != null) {
                                uuid = objectMap.get("uuid").toString();
                                telegram = (List<Object>) objectMap.get("mail");
                            } else {
                                player.sendMessage(noPLayer);
                            }
                        } else {
                            player.sendMessage(noPLayer);
                        }
                    } catch (NoClassDefFoundError error) {
                        return;
                    }
                } else {
                    objectMap = DatabaseHandler.query(name.toLowerCase(), "name");
                    if (objectMap != null) {
                        uuid = objectMap.get("uuid").toString();
                        telegram = (List<Object>) objectMap.get("mail");
                    } else {
                        player.sendMessage(noPLayer);
                    }
                }

                if (telegram != null) {
                    mailData.add(0, true);
                    mailData.add(1, sender);
                    mailData.add(2, title);
                    mailData.add(3, message);
                    telegram.add(mailData);
                    System.out.println(telegram);
                    DatabaseHandler.update(uuid, "mail", telegram);
                    player.sendMessage("Sent telegram to => " + name);
                }
            }
            //return;
        }
    }
}
