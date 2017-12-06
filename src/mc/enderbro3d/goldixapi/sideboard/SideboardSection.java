package mc.enderbro3d.goldixapi.sideboard;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SideboardSection<T> {

    private String sectionName;
    private String display;
    private Sideboard sideboard;
    private ConcurrentHashMap<Integer, SideboardLine<T>> lines = new ConcurrentHashMap<>();

    public Sideboard getSideboard() {
        return sideboard;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getDisplay() {
        return display;
    }

    public SideboardSection(Sideboard sideboard, String sectionName, String display) {
        this.sideboard = sideboard;
        this.sectionName = sectionName;
        this.display = display;
    }

    public void addLine(int position, SideboardLine<T> line) {
        setLine(position, line);
    }

    public void addLine(SideboardLine<T> line) {
        setLine(lastIndex(), line);
    }

    public Map<Integer, SideboardLine<T>> getLines() {
        return lines;
    }

    public void removeLine(int position) {
        lines.remove(position);
    }

    public int lastIndex() {
        Iterator<Integer> lineIterator = lines.keySet().iterator();

        int i = 0;
        while(lineIterator.hasNext()) {
            int newI = lineIterator.next();
            if(i + 1 < newI) {
                i = i + 1;
                break;
            }
            i = newI + 1;
        }
        return i;
    }

    public boolean hasLine(SideboardLine<T> line) {
        for(SideboardLine<T> line1:lines.values()) {
            if(line1.toString().equals(line.toString())) return true;
        }
        return false;
    }

    public int getPosition(SideboardLine<T> line) {
        if(!hasLine(line)) return -1;
        for(int i : lines.keySet()) {
            if(getLine(i).toString().equals(line.toString())) return i;
        }
        return -1;
    }

    public void setLine(int position, SideboardLine<T> line) {
        if(position < 0) position = 0;
        lines.put(position, line);
    }

    public SideboardLine<T> getLine(int position) {
        return lines.get(position);
    }

    public void moveLine(SideboardLine<T> line, int position) {
        SideboardLine<T> line1 = getLine(position);
        if(line1 == null) {
            setLine(position, line);
        } else {
            int position1 = getPosition(line);
            if(position1 == -1) {
                moveLine(line1, position + 1);
            } else {
                setLine(position, line);
                setLine(position1, line1);
            }
        }
    }
}
