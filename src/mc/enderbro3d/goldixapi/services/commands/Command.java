package mc.enderbro3d.goldixapi.services.commands;

import com.google.common.collect.Sets;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Set;

public abstract class Command {

    private Set<String> aliases = Sets.newConcurrentHashSet();
    private String name;
    private int level;

    public String toString() {
        return getName();
    }

    public void addAlias(String alias) {
        aliases.add(alias);
    }

    public void removeAlias(String alias) {
        aliases.remove(alias);
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public Command(String name, int level, String... aliases) {
        this.aliases = Sets.newConcurrentHashSet(Arrays.asList(aliases));
        this.name = name;
        this.level = level;
    }

    public abstract void execute(CommandSender sender, String[] args) throws Exception;
}
