/*
 *  Copyright (C) <2022> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customnameplates;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.momirealms.customnameplates.command.NameplateCommand;
import net.momirealms.customnameplates.helper.VersionHelper;
import net.momirealms.customnameplates.manager.*;
import net.momirealms.customnameplates.utils.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomNameplates extends JavaPlugin {

    private static CustomNameplates plugin;
    private static BukkitAudiences adventure;
    private static ProtocolManager protocolManager;
    private ResourceManager resourceManager;
    private BossBarManager bossBarManager;
    private PlaceholderManager placeholderManager;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private FontManager fontManager;
    private VersionHelper versionHelper;
    private BackgroundManager backgroundManager;
    private ImageManager imageManager;

    @Override
    public void onLoad(){
        plugin = this;
    }

    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);
        protocolManager = ProtocolLibrary.getProtocolManager();
        AdventureUtils.consoleMessage("[JustBossbar] Running on <white>" + Bukkit.getVersion());
        this.fix();
        this.versionHelper = new VersionHelper(this);
        this.configManager = new ConfigManager();
        this.messageManager = new MessageManager();
        this.placeholderManager = new PlaceholderManager(this);
        this.bossBarManager = new BossBarManager(this);
        this.resourceManager = new ResourceManager(this);
        this.backgroundManager = new BackgroundManager(this);
        this.fontManager = new FontManager(this);
        this.imageManager = new ImageManager(this);
        this.registerCommands();
        this.reload();
        AdventureUtils.consoleMessage("[JustBossbar] Plugin Enabled!");
        if (ConfigManager.checkUpdate) this.versionHelper.checkUpdate();
    }

    @Override
    public void onDisable() {
        if (bossBarManager != null) bossBarManager.unload();
        if (placeholderManager != null) placeholderManager.unload();
        if (fontManager != null) fontManager.unload();
        if (imageManager != null) imageManager.unload();
        if (backgroundManager != null) backgroundManager.unload();
        if (adventure != null) adventure.close();
    }

    private void registerCommands() {
        NameplateCommand nameplateCommand = new NameplateCommand();
        PluginCommand main = Bukkit.getPluginCommand("customnameplates");
        if (main != null) {
            main.setExecutor(nameplateCommand);
            main.setTabCompleter(nameplateCommand);
        }
    }

    private void fix() {
        //Don't delete this, a temp fix for a certain version of ProtocolLib
        new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
    }

    public void reload() {
        configManager.unload();
        messageManager.unload();
        bossBarManager.unload();
        placeholderManager.unload();
        imageManager.unload();
        fontManager.unload();
        backgroundManager.unload();
        configManager.load();
        messageManager.load();
        imageManager.load();
        fontManager.load();
        backgroundManager.load();
        bossBarManager.load();
        placeholderManager.load();

        resourceManager.generateResourcePack();
    }

    public static CustomNameplates getInstance() {
        return plugin;
    }

    public static BukkitAudiences getAdventure() {
        return adventure;
    }

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public ResourceManager getResourceManager() {
        return this.resourceManager;
    }

    public PlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }

    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }

    public VersionHelper getVersionHelper() {
        return versionHelper;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }
}
