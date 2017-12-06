package mc.enderbro3d.goldixapi;

import mc.enderbro3d.goldixapi.user.User;
import org.bukkit.permissions.ServerOperator;

public class CustomOperator implements ServerOperator {

    private User user;

    public CustomOperator(User user) {
        this.user = user;
    }



    @Override
    public boolean isOp() {
        return user.hasPermission("*");
    }

    @Override
    public void setOp(boolean b) {
        if(b) user.getGroup().addPermission("*");
        else user.getGroup().removePermission("*");
    }
}
