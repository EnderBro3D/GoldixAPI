package mc.enderbro3d.goldixapi.sideboard;


import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Sideboard {

    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();

    private String title;
    private String name;
    private Scoreboard scoreboard;

    private Set<SideboardUpdater> updater = Sets.newConcurrentHashSet();

    private ConcurrentHashMap<String, SideboardSection<String>> sections = new ConcurrentHashMap<>();

    public Sideboard(String title, String name) {
        this.title = title;
        this.name = name;
        scoreboard();
        update();
    }

    public void addSection(SideboardSection<String> section) {
        sections.put(section.getSectionName(), section);
        update();
    }

    public void setTitle(String s) {
        title = s;
    }

    public void removeSection(String section) {
        sections.remove(section);
        update();
    }

    public void update() {
        if(name.length() > 16) name = name.substring(0, 16);
        String s = "goldix_" + name;
        if(s.length() > 16) s = s.substring(0, 16);
        Objective obj = scoreboard.getObjective(s);
        if(obj != null) obj.unregister();

        Objective objective = scoreboard.registerNewObjective(s, name);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(title);



        int sectionPos = 0;
        int linePos = 0;

        for(SideboardSection<String> section:sections.values()) {
            objective.getScore("§7[" + section.getDisplay() + "§7]").setScore(-(sectionPos + linePos + 1));

            for(SideboardLine<String> line:section.getLines().values()) {
                String text = check(" " + line.getLine(), 40);
                objective.getScore(text).setScore(-(sectionPos + linePos + 2));
                linePos++;
            }
            sectionPos ++;
        }
        updater.forEach(u -> u.update(this));
    }

    public void addUpdater(SideboardUpdater u) {
        updater.add(u);
    }

    public String check(String s, int i1) {
        if(s.length() > i1) s = s.substring(0, i1);
        StringBuilder append = new StringBuilder();
        for(int i = 0;i < i1 - s.length();i++) {
            append.append(" ");
        }
        return s + append;
    }

    public void setSideboard(Player player) {
        player.setScoreboard(scoreboard);
    }

    public SideboardSection<String> getSection(String section) {
        return sections.get(section);
    }

    public void scoreboard() {
        scoreboard = manager.getMainScoreboard();
    }
}
