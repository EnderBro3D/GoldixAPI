package mc.enderbro3d.goldixapi.commands;

import mc.enderbro3d.goldixapi.Main;
import mc.enderbro3d.goldixapi.user.UserGroup;
import mc.enderbro3d.goldixapi.services.UserService;
import mc.enderbro3d.goldixapi.services.commands.Command;
import mc.enderbro3d.goldixapi.user.User;
import org.bukkit.command.CommandSender;

public class PermissionsCommand extends Command {
    public PermissionsCommand() {
        super("permissions", 10, "perms");
    }


    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§ePermissions §8| §fПомощь по правам");
        sender.sendMessage("§eДобавить право в группу §7- §a/perms addperm [Группа] [Право]");
        sender.sendMessage(" §7§o* Пример: /perms add default chat.use");
        sender.sendMessage("§eОтнять право §7- §a/perms removeperm [Группа] [Право]");
        sender.sendMessage(" §7§o* Пример: /perms remove default chat.use");
        sender.sendMessage("§eУстановить префикс §7- §a/perms prefix [Группа] [Префикс]");
        sender.sendMessage(" §7§o* Пример: /perms prefix default &f[&7Игрок&f]");
        sender.sendMessage("§eУстановить отображаемое имя §7- §a/perms display [Группа] [Имя]");
        sender.sendMessage(" §7§o* Пример: /perms display default &7Игрок");
        sender.sendMessage("§eСоздать группу §7- §a/perms create [Группа] [Уровень доступа] [Префикс]");
        sender.sendMessage(" §7§o* Пример: /perms create default 0 &f[&7Игрок&f]");
        sender.sendMessage("§eУдалить группу §7- §a/perms delete [Группа]");
        sender.sendMessage(" §7§o* Пример: /perms delete default");
        sender.sendMessage("§eУстановить группу для игрока §7- §a/perms setgroup [Игрок] [Группа]");
        sender.sendMessage(" §7§o* Пример: /perms setgroup WhileInside default");
        sender.sendMessage("§eСохранить изменения §7- §a/perms save [Асинхронность]");
        sender.sendMessage(" §7§o* Пример: /perms save true");
        sender.sendMessage("§eЗаного загрузить данные из базы данных §7- §a/perms load [Асинхронность]");
        sender.sendMessage(" §7§o* Пример: /perms load true");
        sender.sendMessage(" §7§o* P.S Асинхронность более производительна при большом объёме данных");
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
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "arg_err"));
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g == null) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "group_err"));
                    return;
                }
                g.addPermission(args[2]);
                sender.sendMessage("§aPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "addperm_suc"));
                break;
            }

            case "removeperm": {
                if (args.length < 3) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "arg_err"));
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g == null) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "group_err"));
                    return;
                }
                g.removePermission(args[2]);
                sender.sendMessage("§aPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "removeperm_suc"));
                break;
            }

            case "prefix": {
                if (args.length < 3) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "arg_err"));
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g == null) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "group_err"));
                    return;
                }


                StringBuilder b = new StringBuilder();

                for (int i = 2; i < args.length; i++) b.append(args[i] + " ");

                g.setPrefix(b.toString());
                sender.sendMessage("§aPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "setprefix_suc"));
                break;
            }

            case "display": {
                if (args.length < 3) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "arg_err"));
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g == null) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "group_err"));
                    return;
                }


                StringBuilder b = new StringBuilder();

                for (int i = 2; i < args.length; i++) b.append(args[i] + " ");

                g.setDisplay(b.toString());
                sender.sendMessage("§aPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "setdisplay_suc"));
                break;
            }

            case "create": {
                if (args.length < 4) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "arg_err"));
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g != null) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "group_err0"));
                    return;
                }
                StringBuilder b = new StringBuilder();

                for (int i = 3; i < args.length; i++) b.append(args[i] + " ");

                String pref = b.substring(0, b.length() - 1);
                UserGroup.makeGroup(Integer.parseInt(args[2]), args[1], pref, pref);
                sender.sendMessage("§aPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "creategroup_suc"));
                break;
            }

            case "delete": {
                if (args.length < 2) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "arg_err"));
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[1]);
                if (g == null) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "group_err"));
                    return;
                }
                g.delete(true);
                sender.sendMessage("§aPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "deletegroup_suc"));
                break;
            }
            case "setgroup": {
                if (args.length < 3) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "arg_err"));
                    return;
                }
                UserGroup g = UserGroup.getGroup(args[2]);
                if (g == null) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "group_err"));
                    return;
                }
                User user = UserService.getOffline(args[1]);
                user.setGroup(g);
                sender.sendMessage("§aPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "setgroup_suc"));
                break;
            }
            case "load": {
                if (args.length < 2) {
                    sender.sendMessage("§cPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender, "arg_err"));
                    return;
                }
                boolean async = Boolean.valueOf(args[1]);
                long current = System.currentTimeMillis();
                sender.sendMessage("§aPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender,"grouploading"));
                UserGroup.loadGroups(async);
                sender.sendMessage("§aPermissions §8| " + String.format(Main.getLanguage().getLocalizedMessage(sender, "groupload"), System.currentTimeMillis() - current));
                break;
            }
            case "save": {
                if (args.length < 2) {
                    sender.sendMessage("§cPermissions §8| §fНедостаточно аргументов");
                    return;
                }
                boolean async = Boolean.valueOf(args[1]);
                long current = System.currentTimeMillis();
                sender.sendMessage("§aPermissions §8| " + Main.getLanguage().getLocalizedMessage(sender,"groupsaving"));
                UserGroup.saveGroups(async);
                sender.sendMessage("§aPermissions §8| " + String.format(Main.getLanguage().getLocalizedMessage(sender, "groupsave"), System.currentTimeMillis() - current));
                break;
            }
            default: {
                sendHelp(sender);
                break;
            }
        }
    }
}