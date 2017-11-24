package mc.enderbro3d.goldixapi.statistics;

import com.google.common.collect.Lists;
import mc.enderbro3d.goldixapi.Main;
import mc.enderbro3d.goldixapi.database.MySQLWorker;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UserGroup {
    private static HashMap<String, UserGroup> groups = new HashMap<>();
    private int level;
    private List<String> permissions = Lists.newArrayList();
    private String name, prefix, display;


    public static UserGroup makeGroup(int level, String name, String display, String prefix, String... permissions) {
        Main.getGlobal().info("Registering new group with name: " + name + "");
        return new UserGroup(level, name, display, prefix, permissions);
    }

    public static void loadGroups() {
        ResultSet rs = MySQLWorker.executeQuery("SELECT * FROM `goldix`.`groups`");
        while (MySQLWorker.next(rs)) {
            String permissionsString = MySQLWorker.get(rs, "permissions").stringValue();
            permissionsString = permissionsString.substring(1, permissionsString.length() - 1);
            String[] permissions = permissionsString.split(", ");
            register(makeGroup(
                    MySQLWorker.get(rs, "level").integerValue(),
                    MySQLWorker.get(rs, "name").stringValue(),
                    MySQLWorker.get(rs, "display").stringValue(),
                    MySQLWorker.get(rs, "prefix").stringValue(),
                    permissions));
        }
    }

    public static void updateGroups() {
        groups.clear();
        loadGroups();
    }

    public static UserGroup getGroup(String group) {
        return groups.get(group);
    }

    public static String[] loadPermissions(ConfigurationSection section) {
        List<String> list = section.getStringList("permissions");
        return list.toArray(new String[list.size()]);
    }

    public static void register(UserGroup g) {
        groups.put(g.name.toLowerCase(), g);
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
        save();
    }

    private UserGroup(int level, String name, String display, String prefix, String... permissions) {
        this.level = level;
        this.permissions = Arrays.asList(permissions);
        this.name = name;
        this.prefix = prefix;
        this.display = display;
    }




    static {
        updateGroups();
    }

    public void save() {
        MySQLWorker.execute("INSERT INTO `goldix`.`groups`(`name`, `prefix`, `level`, `permissions`) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE `prefix`=?, `level`=?, `permissions`=?", name, prefix, level, permissions.toString(),
                prefix, level, permissions.toString());
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
        save();
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
        save();
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void addPermission(String s) {
        permissions.add(s);
        save();
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
