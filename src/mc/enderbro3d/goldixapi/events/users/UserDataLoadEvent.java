package mc.enderbro3d.goldixapi.events.users;

import mc.enderbro3d.goldixapi.data.Data;
import mc.enderbro3d.goldixapi.user.User;

import java.net.InetAddress;

public class UserDataLoadEvent extends UserEvent {

    private Data data;

    public UserDataLoadEvent(User user, Data data) {
        super(user);
        this.data = data;
    }


    /**
     * Возвращает данные, которые были загружены
     * @return Данные
     */
    public Data getData() {
        return data;
    }
}
