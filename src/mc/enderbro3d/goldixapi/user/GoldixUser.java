package mc.enderbro3d.goldixapi.user;

import mc.enderbro3d.goldixapi.data.Data;
import mc.enderbro3d.goldixapi.data.types.EverywhereValueType;
import mc.enderbro3d.goldixapi.data.types.KeyType;
import mc.enderbro3d.goldixapi.data.values.Value;
import mc.enderbro3d.goldixapi.services.languages.Language;
import org.bukkit.entity.Player;

public class GoldixUser implements User {

    private String name;
    private Data data;

    public GoldixUser(Player player) {
        this(player.getName());
    }

    public GoldixUser(String name) {
        this.name = name;
        data = new Data(name);
    }


    @Override
    public Language getLanguage() {
        return Language.getLanguage(data.getData(KeyType.EVERYWHERE, EverywhereValueType.LANG).integerValue());
    }

    @Override
    public void load() {
        data.load();
    }

    @Override
    public void setLanguage(Language language) {
        data.setData(KeyType.EVERYWHERE, EverywhereValueType.LANG, new Value(language.getID()));
    }

    @Override
    public UserGroup getGroup() {
        return UserGroup.getGroup(data.getData(KeyType.EVERYWHERE, EverywhereValueType.GROUP).stringValue());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Data getData() {
        return data;
    }

    @Override
    public void save() {
        data.save();
    }

    @Override
    public void setGroup(UserGroup group) {
        data.setData(KeyType.EVERYWHERE, EverywhereValueType.GROUP, new Value(group.getName()));
    }

    @Override
    public boolean hasPermission(String s) {
        return getGroup().hasPermission(s);
    }

    @Override
    public void addPermission(String s) {
        getGroup().addPermission(s);
    }

    @Override
    public void removePermission(String s) {
        getGroup().removePermission(s);
    }
}
