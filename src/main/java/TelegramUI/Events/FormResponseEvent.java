package TelegramUI.Events;

import TelegramUI.Main;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.utils.TextFormat;

public class FormResponseEvent implements Listener {

    private Main plugin;

    public FormResponseEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler()
    public void onFormSubmit(PlayerFormRespondedEvent event) {
        if (event.getResponse() instanceof FormResponseCustom) {
            FormResponseCustom formResponseCustom = (FormResponseCustom) event.getResponse();
            String args0 = formResponseCustom.getInputResponse(0);
            String args1 = formResponseCustom.getInputResponse(1);
            Player sender = event.getPlayer();
            Player receiver = plugin.getServer().getPlayer(args0);
            if (receiver != null) {

                sender.sendMessage(plugin.prefix);
                sender.sendMessage(TextFormat.ITALIC+""+TextFormat.GREEN+"TO=>"+TextFormat.GRAY+receiver.getName());
                sender.sendMessage(TextFormat.ITALIC+""+TextFormat.RED+"FROM=>"+TextFormat.GRAY+sender.getName());
                sender.sendMessage(args1);
                sender.sendMessage(plugin.prefix);

                receiver.sendMessage(plugin.prefix);
                receiver.sendMessage(TextFormat.ITALIC+""+TextFormat.GREEN+"TO=>"+TextFormat.GRAY+receiver.getName());
                receiver.sendMessage(TextFormat.ITALIC+""+TextFormat.RED+"FROM=>"+TextFormat.GRAY+sender.getName());
                receiver.sendMessage(args1);
                receiver.sendMessage(plugin.prefix);

            } else {
                sender.sendMessage(plugin.prefix);
                sender.sendMessage(args1 + " Does not appear to be online!");
            }
        }
    }
}
