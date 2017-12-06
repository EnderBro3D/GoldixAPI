package mc.enderbro3d.goldixapi.minigame;

import mc.enderbro3d.goldixapi.data.Data;
import mc.enderbro3d.goldixapi.user.UserGroup;
import mc.enderbro3d.goldixapi.services.languages.Language;
import mc.enderbro3d.goldixapi.user.GoldixUser;
import mc.enderbro3d.goldixapi.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MinigameUser implements User {

    private GoldixUser user;

    public MinigameUser(GoldixUser user) {
        this.user = user;
        if(checkOnline()) throw new IllegalStateException("User must be online");
    }

    public boolean checkOnline() {
        Player p = Bukkit.getPlayer(getName());
        return p == null || !p.isOnline();
    }

    public Arena getArena(ArenaManager manager) {
        if(checkOnline()) return null;
        return manager.getPlayerArena(this);
    }


    @Override
    public Language getLanguage() {
        return user.getLanguage();
    }

    @Override
    public void load() {
        user.load();
    }

    @Override
    public void setLanguage(Language language) {
        user.setLanguage(language);
    }

    @Override
    public UserGroup getGroup() {
        return user.getGroup();
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public Data getData() {
        return user.getData();
    }

    @Override
    public void save() {
        user.save();
    }

    @Override
    public void setGroup(UserGroup group) {
        user.setGroup(group);
    }

    @Override
    public boolean hasPermission(String s) {
        return user.hasPermission(s);
    }

    @Override
    public void addPermission(String s) {
        user.addPermission(s);
    }

    @Override
    public void removePermission(String s) {
        user.removePermission(s);
    }
}
