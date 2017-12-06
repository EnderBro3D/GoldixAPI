package mc.enderbro3d.goldixapi;

import mc.enderbro3d.goldixapi.user.User;
import mc.enderbro3d.goldixapi.utils.ReflectionUtil;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

public class CustomPermissible extends PermissibleBase {


    private User user;

    public CustomPermissible(User user) {
        super(new CustomOperator(user));
        this.user = user;
    }

    @Override
    public boolean isPermissionSet(String name) {
        return hasPermission(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return hasPermission(perm.getName());
    }

    @Override
    public boolean hasPermission(String inName) {
        return user.getGroup().hasPermission(inName) || (user.getGroup().hasPermission(getParent(inName) + ".*") ||
                isOp());
    }

    private String getParent(String permission) {
        String[] permissionSplit = permission.split(".");
        if(permissionSplit.length < 2) return permission;
        StringBuilder parent = new StringBuilder();
        for(int i = 0;i < permissionSplit.length - 1;i++) {
            parent.append(permissionSplit[i] + " ");
        }
        return parent.substring(0, -1);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return hasPermission(perm.getName());
    }

    public void inject(Player player) {
        ReflectionUtil.setField(CraftHumanEntity.class, (CraftHumanEntity) player,"perm", this);
    }
}
