package mc.enderbro3d.goldixapi.sideboard;

public class SideboardLine <T> {

    private T line = null;
    private SideboardSection section;

    public SideboardLine(SideboardSection section, T line) {
        this.section = section;
        this.line = line;
    }

    public T getLine() {
        return line;
    }

    public SideboardSection getSection() {
        return section;
    }

    public String toString() {
        return line.toString();
    }


}
