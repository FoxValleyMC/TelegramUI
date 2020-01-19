package TelegramUI;

import TelegramUI.Commands.TelegramCommand;
import TelegramUI.Events.FormResponseEvent;
import TelegramUI.Events.JoinEvent;
import cn.nukkit.command.CommandMap;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;
import java.util.Map;

public class Main extends PluginBase {

    public static Main getInstance() {
        return instance;
    }

    private static Main instance;
    public Map<String, FormWindow> formWindowMap = new HashMap<>();
    public String prefix = "["+TextFormat.BOLD+""+TextFormat.BLUE+"Telegram" + " " + TextFormat.DARK_AQUA+"Menu"+TextFormat.RESET+"]";

    @Override
    public void onLoad() {
        CommandMap commandMap = this.getServer().getCommandMap();
        commandMap.register("TelegramUI", new TelegramCommand("Telegram", this));
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new FormResponseEvent(this), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        registerSendMessageFormWindow();
        registerTelegramSendFormWindow();
        registerToggleFormWindow();
        saveDefaultConfig();
        instance = this;
    }

    private void registerSendMessageFormWindow() {
        FormWindowCustom formWindowCustom = new FormWindowCustom(prefix);
        ElementInput elementInput1 = new ElementInput("Enter part of player name to auto complete!", "Player name");
        ElementInput elementInput2 = new ElementInput("", "Message...");
        formWindowCustom.addElement(elementInput1);
        formWindowCustom.addElement(elementInput2);
        formWindowMap.put("message_send", formWindowCustom);
    }

    private void registerToggleFormWindow() {
        String content = "Would you like to send a DM to a player who is currently online?\n\n **OR**\n\nSend a telegram to a player's inbox?";
        String btn1 = "Send DM";
        String btn2 = "Send telegram";
        FormWindowModal formWindowModal = new FormWindowModal(prefix, content, btn1, btn2);
        formWindowMap.put("toggle_window", formWindowModal);
    }

    private void registerTelegramSendFormWindow() {
        FormWindowCustom formWindowCustom = new FormWindowCustom(prefix);
        ElementInput elementInput1 = new ElementInput("Please enter player full xBox gamer tag!", "name");
        ElementInput elementInput2 = new ElementInput("", "Title");
        ElementInput elementInput3 = new ElementInput("", "Message");
        formWindowCustom.addElement(elementInput1);
        formWindowCustom.addElement(elementInput2);
        formWindowCustom.addElement(elementInput3);
        formWindowMap.put("telegram_send", formWindowCustom);
    }

}
