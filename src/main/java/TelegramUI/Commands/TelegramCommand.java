package TelegramUI.Commands;

import TelegramUI.Handler.DatabaseHandler;
import TelegramUI.Main;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;

import java.util.List;
import java.util.Map;

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
                    ((Player) sender).showFormWindow(plugin.formAPI.get("toggleMode"), plugin.formAPI.getId("toggleMode"));
                    break;
                case 1:
                    switch (args[0]){
                        case "read":
                        case "view":
                            String uuid = ((Player) sender).getUniqueId().toString();
                            Map<String, Object> objectMap = DatabaseHandler.query(uuid, "uuid");
                            List<List<Object>> mailData = (List<List<Object>>) objectMap.get("mail");
                            FormWindowSimple formWindowSimple = new FormWindowSimple("Your inbox...", "");
                            formWindowSimple.setContent(TextFormat.RED+"Unread Mail..."+ TextFormat.GRAY + " - " +TextFormat.DARK_GREEN+"Saved Mail...");
                            if (mailData.size() >= 1) {
                                for (List<Object> telegram : mailData) {
                                    String format = (Boolean) telegram.get(0) ? TextFormat.DARK_GREEN+"" : TextFormat.RED+"";
                                    ElementButton button = new ElementButton(format + telegram.get(2).toString());
                                    formWindowSimple.addButton(button);
                                }
                            }
                            plugin.formAPI.add("readMail", formWindowSimple);
                            ((Player) sender).showFormWindow(plugin.formAPI.get("readMail"), plugin.formAPI.getId("readMail"));
                            break;
                    }
                    break;
            }
        } else {
            sender.sendMessage("Command can only be executed from in-game!");
        }
        return true;
    }
}
