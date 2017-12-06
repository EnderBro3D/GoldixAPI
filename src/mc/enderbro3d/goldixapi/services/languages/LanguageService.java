package mc.enderbro3d.goldixapi.services.languages;

import mc.enderbro3d.goldixapi.Main;
import mc.enderbro3d.goldixapi.services.Service;
import mc.enderbro3d.goldixapi.services.UserService;
import mc.enderbro3d.goldixapi.user.User;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageService implements Service {

    private LanguageData data;

    public LanguageData getData() {
        return data;
    }

    public String getLocalizedMessage(User u, String s) {
        return getLocalizedMessage(u.getLanguage(), s);
    }

    public String getLocalizedMessage(Language lang, String s) {
        return data.getMessage(lang, s);
    }

    public String getLocalizedMessage(CommandSender sender, String s) {
        Language lang = Language.EN;
        if (sender instanceof Player) lang = UserService.getUser((Player) sender).getLanguage();
        String s1 = Main.getLanguage().getLocalizedMessage(lang, s);
        if (s1 == null) s1 = s;
        return ChatColor.translateAlternateColorCodes('&',s1);
    }

    @Override
    public void enableService() {
        data = new LanguageData();

        data.load(true);
    }

    @Override
    public void disableService() {
        data.unload();
    }
}
