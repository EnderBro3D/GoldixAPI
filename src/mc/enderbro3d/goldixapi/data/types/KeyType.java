package mc.enderbro3d.goldixapi.data.types;

public enum KeyType {
    BEDWARS("bw"),
    SKYWARS("sw"),
    EVERYWHERE("everywhere");

    private String server;

    KeyType(String server) {
        this.server = server;
    }

    /**
     * Возвращает сервер, на котором данное значение актуально
     * @return Сервер
     */
    public String getServer() {
        return server;
    }
}
