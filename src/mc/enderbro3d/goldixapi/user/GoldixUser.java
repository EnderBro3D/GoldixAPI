package mc.enderbro3d.goldixapi.user;

import mc.enderbro3d.goldixapi.data.Data;
import mc.enderbro3d.goldixapi.data.Value;
import mc.enderbro3d.goldixapi.data.types.GameType;
import mc.enderbro3d.goldixapi.data.types.GlobalValueType;
import mc.enderbro3d.goldixapi.statistics.UserGroup;
import org.bukkit.entity.Player;

public class GoldixUser implements User {

    private String name;
    private UserGroup group;
    private Data data;

    public GoldixUser(Player player) {
        data = new Data(name = player.getName());
        group = UserGroup.getGroup(data.getData(GameType.GLOBAL, GlobalValueType.GROUP).stringValue());
    }


    @Override
    public UserGroup getGroup() {
        return group;
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
        this.group = group;
        data.setData(GameType.GLOBAL, GlobalValueType.GROUP, new Value(group.getName()));
    }

    @Override
    public boolean hasPermission(String s) {
        return group.hasPermission(s);
    }

    @Override
    public void addPermission(String s) {
        group.addPermission(s);
    }

    @Override
    public void removePermission(String s) {
        group.removePermission(s);
    }
}
