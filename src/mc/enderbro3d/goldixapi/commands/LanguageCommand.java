package mc.enderbro3d.goldixapi.commands;

import mc.enderbro3d.goldixapi.Main;
import mc.enderbro3d.goldixapi.services.UserService;
import mc.enderbro3d.goldixapi.services.commands.Command;
import mc.enderbro3d.goldixapi.services.languages.Language;
import mc.enderbro3d.goldixapi.services.languages.LanguageData;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageCommand extends Command {
    public LanguageCommand() {
        super("language", 8, "lang");
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§eLanguage §8| §fПомощь по языку");
        sender.sendMessage("§eДобавить строчку в перевод §7- §a/language set [Язык] [Ключ] [Значение]");
        sender.sendMessage(" §7§o* Пример: /language set ru no_perms &4Недостаточно прав");
        sender.sendMessage("§eПолучить перевод строки §7- §a/language get [Язык] [Ключ]");
        sender.sendMessage(" §7§o* Пример: /language get ru no_perms");
        sender.sendMessage("§eУстанавливает вам язык §7- §a/language setlang [Язык]");
        sender.sendMessage(" §7§o* Пример: /language setlang ru");
        sender.sendMessage("§eСохранить изменения §7- §a/language save [Асинхронность]");
        sender.sendMessage(" §7§o* Пример: /language save true");
        sender.sendMessage("§eЗаного загрузить данные из базы данных §7- §a/language load [Асинхронность]");
        sender.sendMessage(" §7§o* Пример: /language load true");
        sender.sendMessage(" §7§o* P.S Асинхронность более производительна при большом объёме данных");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sendHelp(sender);
            return;
        }

        LanguageData data = Main.getLanguage().getData();
        switch(args[0].toLowerCase()) {
            case "set": {
                if (args.length < 4) {
                    sender.sendMessage("§cLanguage §8| §fНедостаточно аргументов");
                    return;
                }
                StringBuilder message = new StringBuilder();
                for (int i = 3; i < args.length; i++) {
                    message.append(args[i] + " ");
                }
                data.setMessage(Language.valueOf(args[1].toUpperCase()), args[2], message.substring(0, message.length() - 1));
                sender.sendMessage("§aLanguage §8| §fИзменения внесены в систему");
                break;
            }
            case "get": {
                if (args.length < 3) {
                    sender.sendMessage("§cLanguage §8| §fНедостаточно аргументов");
                    return;
                }
                sender.sendMessage("§aLanguage §8| §fЗначение: " + data.getMessage(Language.valueOf(args[1].toUpperCase()), args[2]));
                break;
            }
            case "setlang": {
                if (args.length < 2) {
                    sender.sendMessage("§cLanguage §8| §fНедостаточно аргументов");
                    return;
                }
                if(!(sender instanceof Player)) return;
                Language lang = Language.valueOf(args[1].toUpperCase());
                UserService.getUser((Player) sender).setLanguage(lang);
                sender.sendMessage("§aLanguage §8| §fТы выбрал язык " + lang);
                break;
            }
            case "load": {
                if (args.length < 2) {
                    sender.sendMessage("§cLanguage §8| §fНедостаточно аргументов");
                    return;
                }
                boolean async = Boolean.valueOf(args[1]);
                long current = 0;
                if(!async) current = System.currentTimeMillis();
                sender.sendMessage("§aLanguage §8| §fНачинаем загрузку данных..." + (!async ? " Таймер запущен" : ""));
                data.load(async);
                sender.sendMessage("§aLanguage §8| §fЗагрузка данных завершена." + (!async ? " Таймер показывает " + (System.currentTimeMillis() - current) + "ms" : ""));
                break;
            }
            case "save": {
                if (args.length < 2) {
                    sender.sendMessage("§cLanguage §8| §fНедостаточно аргументов");
                    return;
                }
                boolean async = Boolean.valueOf(args[1]);
                long current = 0L;
                if(!async) current = System.currentTimeMillis();
                sender.sendMessage("§aLanguage §8| §fНачинаем сохранение данных..." + (!async ? " Таймер запущен" : ""));
                data.save(async);
                sender.sendMessage("§aLanguage §8| §fСохранение данных завершено." + (!async ? " Таймер показывает " + (System.currentTimeMillis() - current) + "ms" : ""));
                break;
            }
            default: {
                sendHelp(sender);
                break;
            }

        }
    }

}
