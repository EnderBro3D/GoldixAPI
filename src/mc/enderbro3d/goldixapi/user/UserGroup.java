package mc.enderbro3d.goldixapi.user;

import com.google.common.collect.Lists;
import mc.enderbro3d.goldixapi.data.MySQLWorker;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class UserGroup {
    private static HashMap<String, UserGroup> groups = new HashMap<>();
    private int level;
    private List<String> permissions = Lists.newArrayList();
    private String name, prefix, display;

    public static UserGroup makeGroup(int level, String name, String display, String prefix, String... permissions) {
        UserGroup g = new UserGroup(level, name, display, prefix, permissions);
        groups.put(name.toLowerCase(), g);
        return g;
    }

    public static void saveGroups() {
        groups.values().forEach(UserGroup::save);

    }

    public static void loadGroups() {
        groups.clear();

        ResultSet rs = MySQLWorker.executeQuery(true, "SELECT * FROM `goldix`.`groups`");
        while (MySQLWorker.next(rs)) {
            String permissionsString = MySQLWorker.get(rs, "permissions").stringValue();
            permissionsString = permissionsString.substring(1, permissionsString.length() - 1);
            String[] permissions = permissionsString.split(", ");
            makeGroup(
                    MySQLWorker.get(rs, "level").integerValue(),
                    MySQLWorker.get(rs, "name").stringValue(),
                    MySQLWorker.get(rs, "display").stringValue(),
                    MySQLWorker.get(rs, "prefix").stringValue(),
                    permissions);
        }
    }

    public static UserGroup getGroup(String group) {
        return groups.get(group.toLowerCase());
    }

    public static List<UserGroup> findGroupByLevel(int level) {
        return groups.values()
                .stream()
                .filter(group -> group.level >= level)
                .collect(Collectors.toList());
    }

    public static void addPermission(String group, String permission) {
        groups.get(group)
                .addPermission(permission);
    }

    public static void removePermission(String group, String permission) {
        groups.get(group)
                .removePermission(permission);
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }

    private UserGroup(int level, String name, String display, String prefix, String... permissions) {
        this.level = level;
        this.permissions = Arrays.asList(permissions);
        this.name = name;
        this.prefix = prefix;
        this.display = display;
    }




    static {
        loadGroups();
    }

    public void save() {
        MySQLWorker.execute(true,"INSERT INTO `goldix`.`groups`(`name`, `prefix`, `level`, `display`, `permissions`) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE `prefix`=?, `level`=?, `display`=?, `permissions`=?", name, prefix, level, display, permissions.toString(),
                prefix, level, display, permissions.toString());
    }

    public void delete(boolean async) {
        groups.remove(name);
        MySQLWorker.execute(async, "DELETE FROM goldix.groups WHERE `name`=?", name);
        name = null;
        prefix = null;
        level = 0;
        permissions = null;
        prefix = null;
        display = null;
    }


    public int getLevel() {
        return level;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void addPermission(String s) {
        permissions.add(s);
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
