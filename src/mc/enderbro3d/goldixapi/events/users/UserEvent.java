package mc.enderbro3d.goldixapi.events.users;

import mc.enderbro3d.goldixapi.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    private User user;

    public UserEvent(User user) {
        this.user = user;
    }

    /**
     * Возвращает пользователя
     * @return Пользователь
     */
    public User getUser() {
        return user;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
