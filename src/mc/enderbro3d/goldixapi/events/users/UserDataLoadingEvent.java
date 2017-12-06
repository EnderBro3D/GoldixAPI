package mc.enderbro3d.goldixapi.events.users;

import mc.enderbro3d.goldixapi.user.User;

import java.net.InetAddress;

public class UserDataLoadingEvent extends UserEvent {

    private InetAddress address;

    public UserDataLoadingEvent(User user, InetAddress address) {
        super(user);
        this.address = address;
    }

    /**
     * Возвращает адрес пользователя
     * @return Адрес
     */
    public InetAddress getAddress() {
        return address;
    }
}
