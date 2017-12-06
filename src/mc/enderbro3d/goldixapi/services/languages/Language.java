package mc.enderbro3d.goldixapi.services.languages;

public enum Language {
    RU(0),
    EN(1),
    DE(2);

    public static Language getLanguage(int i) {
        switch(i) {
            case 0: return RU;
            case 1: return EN;
            case 2: return DE;
            default: return null;
        }
    }

    private int id;
    Language(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
