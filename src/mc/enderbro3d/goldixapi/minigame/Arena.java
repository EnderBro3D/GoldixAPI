package mc.enderbro3d.goldixapi.minigame;


import java.util.concurrent.ConcurrentHashMap;

public class Arena {
    private ConcurrentHashMap<String, MinigameUser> users = new ConcurrentHashMap<>();
    public void addUser(MinigameUser user) {
        users.put(user.getName().toLowerCase(), user);
    }

    public boolean hasUser(MinigameUser user) {
        return hasUser(user.getName());
    }

    public boolean hasUser(String s) {
        return users.containsKey(s.toLowerCase());
    }



    public void removeUser(String s) {
        users.remove(s.toLowerCase());
    }
}
