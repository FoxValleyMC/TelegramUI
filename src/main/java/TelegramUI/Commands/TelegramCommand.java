package TelegramUI.Commands;

import TelegramUI.Main;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;

public class TelegramCommand extends PluginCommand {

    private Main plugin;

    public TelegramCommand(String name, Main plugin) {
        super(name, plugin);
        this.plugin = plugin;
        this.description = "Send private message to specified player in UI";
        this.setAliases(new String[]{"tg"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            switch (args.length) {
                case 0:
                    ((Player) sender).showFormWindow(plugin.formWindowMap.get("toggle_window"), 0);
                    break;
                case 1:
                    break;
            }
        } else {
            sender.sendMessage("Command can only be executed from in-game!");
        }
        return true;
    }
}
