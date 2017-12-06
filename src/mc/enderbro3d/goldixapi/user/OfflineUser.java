package mc.enderbro3d.goldixapi.user;

import mc.enderbro3d.goldixapi.data.Data;
import mc.enderbro3d.goldixapi.data.types.GameType;
import mc.enderbro3d.goldixapi.data.types.GlobalValueType;
import mc.enderbro3d.goldixapi.data.values.Value;
import mc.enderbro3d.goldixapi.services.languages.Language;

public class OfflineUser implements User {

    private String name;
    private Data data;

    public OfflineUser(String name) {
        this.name = name;
        this.data = new Data(name);
        save();
    }


    @Override
    public Language getLanguage() {
        return Language.getLanguage(data.getData(GameType.GLOBAL, GlobalValueType.LANG).integerValue());
    }

    @Override
    public void load() {
        data.load(true);
    }

    @Override
    public void setLanguage(Language language) {
        data.setData(GameType.GLOBAL, GlobalValueType.LANG, new Value(language.getID()));
    }

    @Override
    public UserGroup getGroup() {
        return UserGroup.getGroup(data.getData(GameType.GLOBAL, GlobalValueType.GROUP).stringValue());
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
        data.save(true);
    }

    @Override
    public void setGroup(UserGroup group) {
        data.setData(GameType.GLOBAL, GlobalValueType.GROUP, new Value(group.getName()));
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
