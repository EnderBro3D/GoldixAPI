package mc.enderbro3d.goldixapi.data.types;

public enum GameType {
    BEDWARS("bw"),
    SKYWARS("sw"),
    GLOBAL("global");

    private String server;

    GameType(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }
}
