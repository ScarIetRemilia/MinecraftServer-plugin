package org.cposa.serverCposa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerCposa extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        try {
            saveDefaultConfig();
            getConfig().getString("no-permission");
            getLogger().info("ServerCposa 插件尝试读取 config.yml 检查完整性...");
            getServer().getPluginManager().registerEvents(this, this);
        } catch (Exception e) {
            getLogger().severe("[core] 读取 config.yml 失败！请联系插件作者/管理员");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
        getLogger().info("-===ServerCposa插件===-");
        getLogger().info("制作:");
        getLogger().info("MisonoMika");
        getLogger().info("ServerCposa 插件已启动，正在加载配置...");
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();

        if (getConfig().getString("no-permission") == null ||
                getConfig().getString("only-player") == null ||
                getConfig().getString("hello-message") == null ||
                getConfig().getString("console-denied-log") == null) {

            getLogger().severe("[core] 读取 config.yml 失败！请联系插件作者/管理员");
            getLogger().severe("[ServerCposa_Safe] 插件已关闭，避免服务器损坏!");

            // 强制禁用插件
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("ServerCposa 插件已启动");
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        getLogger().info("ServerCposa 插件已关闭");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        getLogger().info("玩家 " + player.getName() + " 加入了服务器");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        /* ========== /sc ========== */
        if (cmd.getName().equalsIgnoreCase("sc")) {

            // 只允许 OP
            if (!sender.isOp()) {
                sender.sendMessage(getConfig().getString("no-permission"));
                return true;
            }

            // 没有参数
            if (args.length == 0) {
                sender.sendMessage("§e用法: /sc <help|reload>");
                return true;
            }

            // /sc help
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§a管理员帮助");
                sender.sendMessage("§a/sc help是 管理员帮助");
                sender.sendMessage("§a/sc reload是 重新加载配置文件");
                sender.sendMessage("§a/hello 是废物 没啥用");
                return true;
            }

            // /sc reload
            if (args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                sender.sendMessage("§a配置已重载！");
                getLogger().info("[core] OP组成员: "+sender.getName()+" 执行了/sc reload命令,执行成功!");
                return true;
            }

            // 未知子指令
            sender.sendMessage("§c未知指令，用法: /sc <help|reload>");
            return true;
        }




        if (cmd.getName().equalsIgnoreCase("hello")) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage("§c只有管理员玩家才能使用,控制台无法使用");
                getLogger().warning("[core] 控制台使用了命令 /hello 但是控制台无法使用");
                return true;
            }

            if (!sender.isOp()) {
                sender.sendMessage("§c你没有权限使用这个指令！");
                getLogger().warning("游戏内玩家 "+sender.getName()+" 使用了这个指令,被不允许执行");
                getLogger().warning("服务器控制台可以使用  /tempban "+sender.getName()+" 进行封禁");
                return true;
            }

            sender.sendMessage("§a欢迎使用ServerCposa!管理员: "+sender.getName());
            sender.sendMessage("§a假如你需要帮助,请输入 /sc help  "+sender.getName());
            return true;
        }

        return false;
    }

}
