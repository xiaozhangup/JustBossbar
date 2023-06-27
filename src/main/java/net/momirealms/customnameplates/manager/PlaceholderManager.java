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

package net.momirealms.customnameplates.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import net.momirealms.customnameplates.CustomNameplates;
import net.momirealms.customnameplates.object.Function;
import net.momirealms.customnameplates.object.font.OffsetFont;
import net.momirealms.customnameplates.object.placeholders.*;
import net.momirealms.customnameplates.utils.ConfigUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderManager extends Function {

    private final Pattern placeholderPattern = Pattern.compile("%([^%]*)%");
    private final HashSet<Integer> descent_fonts;
    private final HashSet<Integer> descent_unicode_fonts;
    private final HashMap<String, BackGroundText> backGroundTextMap;
    private final HashMap<String, StaticText> stringStaticTextMap;
    private final HashMap<String, DescentText> descentTextMap;
    private final HashMap<String, DescentText> descentUnicodeMap;
    private final HashMap<String, VanillaHud> vanillaHudMap;
    private CustomNameplates plugin;

    public PlaceholderManager(CustomNameplates plugin) {
        this.plugin = plugin;
        this.descent_fonts = new HashSet<>();
        this.descent_unicode_fonts = new HashSet<>();
        this.backGroundTextMap = new HashMap<>();
        this.stringStaticTextMap = new HashMap<>();
        this.descentTextMap = new HashMap<>();
        this.vanillaHudMap = new HashMap<>();
        this.descentUnicodeMap = new HashMap<>();
    }

    @Override
    public void load() {
        this.loadConfig();
    }

    @Override
    public void unload() {
        this.descent_fonts.clear();
        this.backGroundTextMap.clear();
        this.stringStaticTextMap.clear();
        this.descentTextMap.clear();
        this.vanillaHudMap.clear();
        this.descent_unicode_fonts.clear();
        this.descentUnicodeMap.clear();
    }

    private void loadConfig() {
        YamlConfiguration config = ConfigUtils.getConfig("configs" + File.separator + "custom-placeholders.yml");

        ConfigurationSection backgroundSection = config.getConfigurationSection("background-text");
        if (backgroundSection != null && ConfigManager.enableBackground) {
            loadBackgroundText(backgroundSection);
        }

        ConfigurationSection staticTextSection = config.getConfigurationSection("static-text");
        if (staticTextSection != null) {
            loadStaticText(staticTextSection);
        }

        ConfigurationSection descentSection = config.getConfigurationSection("descent-text");
        if (descentSection != null) {
            loadDescentText(descentSection);
        }

        ConfigurationSection vanillaHudSection = config.getConfigurationSection("vanilla-hud");
        if (vanillaHudSection != null) {
            loadVanillaHud(vanillaHudSection);
        }

        if (plugin.getVersionHelper().isVersionNewerThan1_20() && !ConfigManager.enable1_20_Unicode) {
            return;
        }

        ConfigurationSection descentUnicodeSection = config.getConfigurationSection("descent-unicode");
        if (descentUnicodeSection != null) {
            loadDescentUnicode(descentUnicodeSection);
        }
    }

    private void loadVanillaHud(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            vanillaHudMap.put(key, new VanillaHud(
               PlaceholderAPI.setPlaceholders(null, section.getString(key + ".images.empty", "")) + ConfigManager.surroundWithFont(String.valueOf(OffsetFont.NEG_2.getCharacter())),
               PlaceholderAPI.setPlaceholders(null, section.getString(key + ".images.half", "")) + ConfigManager.surroundWithFont(String.valueOf(OffsetFont.NEG_2.getCharacter())),
               PlaceholderAPI.setPlaceholders(null, section.getString(key + ".images.full", "")) + ConfigManager.surroundWithFont(String.valueOf(OffsetFont.NEG_2.getCharacter())),
                    section.getString(key + ".placeholder.value"),
                    section.getString(key + ".placeholder.max-value"),
                    section.getBoolean(key + ".reverse", true)
            ));
        }
    }

    private void loadDescentText(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            descent_fonts.add(8 - section.getInt(key + ".descent"));
            descentTextMap.put(key, new DescentText(section.getString(key + ".text"), 8 - section.getInt(key + ".descent")));
        }
    }

    private void loadDescentUnicode(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            descent_unicode_fonts.add(8 - section.getInt(key + ".descent"));
            descentUnicodeMap.put(key, new DescentText(section.getString(key + ".text"), 8 - section.getInt(key + ".descent")));
        }
    }

    private void loadStaticText(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            stringStaticTextMap.put(key, new StaticText(section.getString(key + ".text"), section.getInt(key + ".value"), StaticText.StaticState.valueOf(section.getString(key + ".position", "left").toUpperCase(Locale.ENGLISH))));
        }
    }

    private void loadBackgroundText(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            backGroundTextMap.put(key, new BackGroundText(
                    section.getString(key + ".text"),
                    section.getString(key + ".background"),
                    section.getBoolean(key + ".remove-shadow", true)
                    )
            );
        }
    }

    public List<String> detectPlaceholders(String text){
        List<String> placeholders = new ArrayList<>();
        Matcher matcher = placeholderPattern.matcher(text);
        while (matcher.find()) placeholders.add(matcher.group());
        return placeholders;
    }

    public BackGroundText getBackgroundText(String key) {
        return backGroundTextMap.get(key);
    }

    public StaticText getStaticText(String key) {
        return stringStaticTextMap.get(key);
    }

    public DescentText getDescentText(String key) {
        return descentTextMap.get(key);
    }

    public VanillaHud getVanillaHud(String key) {
        return vanillaHudMap.get(key);
    }

    public HashSet<Integer> getDescent_fonts() {
        return descent_fonts;
    }

    public HashSet<Integer> getDescent_unicode_fonts() {
        return descent_unicode_fonts;
    }

    public DescentText getDescentUnicode(String key) {
        return descentUnicodeMap.get(key);
    }
}
