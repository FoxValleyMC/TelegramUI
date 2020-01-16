package TelegramUI;

import TelegramUI.Commands.TelegramCommand;
import TelegramUI.Events.FormResponseEvent;
import cn.nukkit.command.CommandMap;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class Main extends PluginBase {

    public FormWindow formWindow;
    public String prefix = "["+TextFormat.BOLD+""+TextFormat.BLUE+"Telegram" + " " + TextFormat.DARK_AQUA+"Menu"+TextFormat.RESET+"]";

    @Override
    public void onLoad() {
        CommandMap commandMap = this.getServer().getCommandMap();
        commandMap.register("TelegramUI", new TelegramCommand("Telegram", this));
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new FormResponseEvent(this), this);
        FormWindowCustom formWindowCustom = new FormWindowCustom(prefix);
        ElementInput elementInput1 = new ElementInput("Enter part of player name to auto complete!", "Player name");
        ElementInput elementInput2 = new ElementInput("", "Message...");
        formWindowCustom.addElement(elementInput1);
        formWindowCustom.addElement(elementInput2);
        this.formWindow = formWindowCustom;
    }

}
