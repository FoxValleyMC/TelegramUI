package TelegramUI.Events;

import PlayerAPI.Overrides.PlayerAPI;
import TelegramUI.Handler.DatabaseHandler;
import TelegramUI.Main;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowModal;
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

        PlayerAPI sender = (PlayerAPI) event.getPlayer();

        if (event.getResponse() instanceof FormResponseCustom) {

            FormResponseCustom response = (FormResponseCustom) event.getResponse();

            if (event.getFormID() == plugin.formAPI.getId("sendMessage")) {

                String args0 = response.getInputResponse(0);
                String args1 = response.getInputResponse(1);
                PlayerAPI receiver = PlayerAPI.get(args0);
                sender.sendMessage(plugin.prefix);
                if (receiver != null) {
                    sender.sendMessage(TextFormat.ITALIC+""+TextFormat.GREEN+"TO=>"+TextFormat.GRAY+receiver.getAlias());
                    sender.sendMessage(TextFormat.ITALIC+""+TextFormat.RED+"FROM=>"+TextFormat.GRAY+sender.getAlias());
                    sender.sendMessage(args1);
                    receiver.sendMessage(plugin.prefix);
                    receiver.sendMessage(TextFormat.ITALIC+""+TextFormat.GREEN+"TO=>"+TextFormat.GRAY+receiver.getAlias());
                    receiver.sendMessage(TextFormat.ITALIC+""+TextFormat.RED+"FROM=>"+TextFormat.GRAY+sender.getAlias());
                    receiver.sendMessage(args1);
                } else {
                    sender.sendMessage(args1 + " Does not appear to be online!");
                }

            } else if (event.getFormID() == plugin.formAPI.getId("sendTelegram")) {


                String receiverName = response.getInputResponse(0).trim();
                String subject = response.getInputResponse(1);
                String message = response.getInputResponse(2);
                Map<String, Object> offlinePlayer = PlayerAPI.getOfflinePlayer(receiverName);

                String noPLayer = TextFormat.RED+"Player "+TextFormat.GOLD+receiverName+TextFormat.RED+" was not found in our database...";

                if (offlinePlayer != null) {
                    sender.sendTelegram(offlinePlayer, subject, message);
                } else {
                    sender.sendMessage(noPLayer);
                }

            }

        } else if (event.getResponse() instanceof FormResponseModal) {

            FormResponseModal response = (FormResponseModal) event.getResponse();
            FormWindowModal formWindow = (FormWindowModal) event.getWindow();
            int button = response.getClickedButtonId();

            if (event.getFormID() == plugin.formAPI.getId("toggleMode")) {

                switch (button) {
                    case 0:
                        event.getPlayer().showFormWindow(plugin.formAPI.get("sendMessage"), plugin.formAPI.getId("sendMessage"));
                        break;
                    case 1:
                        event.getPlayer().showFormWindow(plugin.formAPI.get("sendTelegram"), plugin.formAPI.getId("sendTelegram"));
                        break;
                }

            } else if (event.getFormID() == plugin.formAPI.getId("viewing")) {

                List<Object> mailList = sender.getMail();

                switch (button) {
                    case 0:
                        for (int i = 0; i < mailList.size(); i++) {
                            List<Object> mailData = (List<Object>) mailList.get(i);
                            String subject = TextFormat.UNDERLINE+mailData.get(2).toString();
                            String body = mailData.get(3).toString();
                            String content = subject+"\n\n"+body;
                            if (formWindow.getContent().equals(content)) {
                                mailData.set(0, true);
                                mailList.set(i, mailData);
                            }
                        }
                        sender.sendMessage(TextFormat.GREEN+"Saved" + TextFormat.WHITE + " 1 " + TextFormat.GREEN + "message...");
                        DatabaseHandler.update(sender.getUuid(), "mail", mailList);
                        break;
                    case 1:
                        for (int i = 0; i < mailList.size(); i++) {
                            List<Object> mailData = (List<Object>) mailList.get(i);
                            String subject = TextFormat.UNDERLINE+mailData.get(2).toString();
                            String body = mailData.get(3).toString();
                            String content = subject+"\n\n"+body;
                            if (formWindow.getContent().equals(content)) {
                                mailList.remove(i);
                            }
                        }
                        event.getPlayer().sendMessage(TextFormat.RED+"Discarded" + TextFormat.WHITE + " 1 " + TextFormat.RED + "message...");
                        DatabaseHandler.update(event.getPlayer().getUniqueId().toString(), "mail", mailList);
                        break;
                }
            }

        } else if (event.getResponse() instanceof FormResponseSimple) {

            FormResponseSimple response = (FormResponseSimple) event.getResponse();

            if (event.getFormID() == plugin.formAPI.getId("readMail")) {
                List<List<Object>> mail = (List<List<Object>>) sender.getTelegramData().get("mail");
                int button = response.getClickedButtonId();
                String from = mail.get(button).get(1).toString();
                String subject = "From... "+TextFormat.ITALIC+from;
                String headline = TextFormat.UNDERLINE+mail.get(button).get(2).toString();
                String body = mail.get(button).get(3).toString();
                String content = headline+"\n\n"+body;
                viewTelegram(sender, subject, content);
            }

        }

    }

    private void viewTelegram(PlayerAPI player, String title, String content) {
        String bt1 = TextFormat.DARK_GREEN+"Save";
        String bt2 = TextFormat.DARK_RED+"Discard";
        FormWindowModal formWindowModal = new FormWindowModal(title, content, bt1, bt2);
        plugin.formAPI.add("viewing", formWindowModal);
        player.showFormWindow(plugin.formAPI.get("viewing"), plugin.formAPI.getId("viewing"));
    }
}
