package mc.enderbro3d.goldixapi.minigame.team;

import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Equivalence;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.webkit.Timer;
import mc.enderbro3d.goldixapi.minigame.Arena;
import mc.enderbro3d.goldixapi.user.MinigamePlayer;
import mc.enderbro3d.goldixapi.wrapper.WrapperPlayServerScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Team {

    private ConcurrentHashMap<String, MinigamePlayer> players = new ConcurrentHashMap<>();

    private Arena arena;

    private String name;

    public Team(String name, Arena arena) {
        this.name = name;
        this.arena = arena;
    }

    public String getName() {
        return name;
    }

    public void sendPackets() {

    }

    private Collection<String> names() {
        List<String> list = Lists.newArrayList();
        players.values().forEach(p -> list.add(p.getName()));
        return list;
    }

    private Collection<String> names(Collection<? extends OfflinePlayer> players) {
        List<String> list = Lists.newArrayList();
        players.forEach(p -> list.add(p.getName()));
        return list;
    }

    private PacketContainer buildPacket_friend() {
        WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam();
        team.setFriendlyFire((byte) 0);
        team.setPlayers(names());
        team.setPacketMode((byte) WrapperPlayServerScoreboardTeam.Modes.PLAYERS_ADDED);
        team.setTeamPrefix("§a");
        team.setTeamName("friends");
        team.setTeamSuffix("[YOUR TEAM]");
        return team.getHandle();
    }

    private PacketContainer buildPacket_enemy() {
        WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam();
        team.setFriendlyFire((byte) 0);
        team.setPlayers(names());
        team.setTeamName("enemy");
        team.setPacketMode((byte) WrapperPlayServerScoreboardTeam.Modes.PLAYERS_ADDED);
        team.setTeamPrefix("§c");
        team.setTeamSuffix("[YOUR ENEMY]");
        return team.getHandle();
    }

    private PacketContainer buildPacket_leave() {
        WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam();
        team.setFriendlyFire((byte) 0);
        team.setPlayers(names());
        team.setPacketMode((byte) WrapperPlayServerScoreboardTeam.Modes.PLAYERS_REMOVED);
        return team.getHandle();
    }


    public Set<MinigamePlayer> getPlayers() {
        return Sets.newConcurrentHashSet(players.values());
    }
}
