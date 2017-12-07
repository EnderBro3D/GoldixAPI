package mc.enderbro3d.goldixapi.commands;

import mc.enderbro3d.goldixapi.Main;
import mc.enderbro3d.goldixapi.services.languages.LanguageService;
import mc.enderbro3d.goldixapi.user.UserGroup;
import mc.enderbro3d.goldixapi.services.UserService;
import mc.enderbro3d.goldixapi.services.commands.Command;
import mc.enderbro3d.goldixapi.user.User;
import org.bukkit.command.CommandSender;

public class PermissionsCommand extends Command {
    public PermissionsCommand() {
        super("group", 10);
    }


    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§ePermissions §8| §fПомощь по правам");
        sender.sendMessage("§eДобавить право в группу §7- §a/group addperm [Группа] [Право]");
        sender.sendMessage(" §7§o* Пример: /group add default chat.use");
        sender.sendMessage("§eОтнять право §7- §a/group removeperm [Группа] [Право]");
        sender.sendMessage(" §7§o* Пример: /group remove default chat.use");
        sender.sendMessage("§eУстановить префикс §7- §a/group prefix [Группа] [Префикс]");
        sender.sendMessage(" §7§o* Пример: /group prefix default &f[&7Игрок&f]");
        sender.sendMessage("§eУстановить отображаемое имя §7- §a/group display [Группа] [Имя]");
        sender.sendMessage(" §7§o* Пример: /group display default &7Игрок");
        sender.sendMessage("§eСоздать группу §7- §a/group create [Группа] [Уровень доступа] [Префикс]");
        sender.sendMessage(" §7§o* Пример: /group create default 0 &f[&7Игрок&f]");
        sender.sendMessage("§eУдалить группу §7- §a/group delete [Группа]");
        sender.sendMessage(" §7§o* Пример: /group delete default");
        sender.sendMessage("§eУстановить группу для игрока §7- §a/group setgroup [Игрок] [Группа]");
        sender.sendMessage(" §7§o* Пример: /group setgroup WhileInside default");
        sender.sendMessage("§eСохранить изменения §7- §a/group save");
        sender.sendMessage("§eЗагрузить данные из базы данных §7- §a/group load");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "addperm": {
                if (args.length < 3) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "arg_err");
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g == null) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "group_err");
                    return;
                }
                g.addPermission(args[2]);
                LanguageService.sendMessage(sender, "§aGroups §8| ", "addperm_suc");
                break;
            }

            case "removeperm": {
                if (args.length < 3) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "arg_err");
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g == null) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "group_err");
                    return;
                }
                g.removePermission(args[2]);
                LanguageService.sendMessage(sender, "§aGroups §8| ", "removeperm_suc");
                break;
            }

            case "prefix": {
                if (args.length < 3) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "arg_err");
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g == null) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "group_err");
                    return;
                }


                StringBuilder b = new StringBuilder();

                for (int i = 2; i < args.length; i++) b.append(args[i] + " ");

                g.setPrefix(b.toString());
                LanguageService.sendMessage(sender, "§aGroups §8| ", "setprefix_suc");
                break;
            }

            case "display": {
                if (args.length < 3) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "arg_err");
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g == null) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "group_err");
                    return;
                }


                StringBuilder b = new StringBuilder();

                for (int i = 2; i < args.length; i++) b.append(args[i] + " ");

                g.setDisplay(b.toString());
                LanguageService.sendMessage(sender, "§aGroups §8| ", "setdisplay_suc");
                break;
            }

            case "create": {
                if (args.length < 4) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "arg_err");
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g != null) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "group_err0");
                    return;
                }
                StringBuilder b = new StringBuilder();

                for (int i = 3; i < args.length; i++) b.append(args[i] + " ");

                String pref = b.substring(0, b.length() - 1);
                UserGroup.makeGroup(Integer.parseInt(args[2]), args[1], pref, pref);
                LanguageService.sendMessage(sender, "§aGroups §8| ", "creategroup_suc");
                break;
            }

            case "delete": {
                if (args.length < 2) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "arg_err");
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g == null) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "group_err");
                    return;
                }
                g.delete(true);
                LanguageService.sendMessage(sender, "§aGroups §8| ", "deletegroup_suc");
                break;
            }
            case "setgroup": {
                if (args.length < 3) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "arg_err");
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[2]);
                if (g == null) {
                    LanguageService.sendMessage(sender, "§cGroups §8| ", "group_err");
                    return;
                }
                User user = UserService.getOffline(args[1]);
                user.setGroup(g);
                LanguageService.sendMessage(sender, "§aGroups §8| ", "setgroup_suc");
                break;
            }
            case "load": {
                LanguageService.sendMessage(sender, "§aGroups §8| ", "grouploading");
                UserGroup.loadGroups();
                LanguageService.sendMessage(sender, "§aGroups §8| ", "groupload");
                break;
            }
            case "save": {
                LanguageService.sendMessage(sender, "§aGroups §8| ", "groupsaving");
                UserGroup.saveGroups();
                LanguageService.sendMessage(sender, "§aGroups §8| ", "groupsave");
                break;
            }
            default: {
                sendHelp(sender);
                break;
            }
        }
    }
}