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

package net.momirealms.customnameplates.object.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.momirealms.customnameplates.CustomNameplates;
import net.momirealms.customnameplates.manager.ConfigManager;
import net.momirealms.customnameplates.manager.FontManager;
import net.momirealms.customnameplates.manager.PlaceholderManager;
import net.momirealms.customnameplates.object.SimpleChar;
import net.momirealms.customnameplates.utils.AdventureUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NameplatePlaceholders extends PlaceholderExpansion {

    private final PlaceholderManager placeholderManager;
    private final CustomNameplates plugin;

    public NameplatePlaceholders(CustomNameplates plugin, PlaceholderManager placeholderManager) {
        this.placeholderManager = placeholderManager;
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "nameplates";
    }

    @Override
    public @NotNull String getAuthor() {
        return "XiaoMoMi";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.2";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        String[] mainParam = params.split("_", 2);
        switch (mainParam[0]) {
            case "background" -> {
                return getBackground(mainParam[1], player);
            }
            case "descent" -> {
                return getDescent(mainParam[1], player);
            }
            case "image" -> {
                return getImage(mainParam[1]);
            }
            case "unicode" -> {
                return getUnicodeDescent(mainParam[1], player);
            }
        }
        return null;
    }

    private String getImage(String param) {
        SimpleChar simpleChar = plugin.getImageManager().getImage(param);
        if (simpleChar == null) return param + " NOT FOUND";
        return ConfigManager.surroundWithFont(String.valueOf(simpleChar.getChars()));
    }

    private String getBackground(String param, Player player) {
        BackGroundText backGroundText = placeholderManager.getBackgroundText(param);
        if (backGroundText == null) return param + " NOT FOUND";
        String parsed = PlaceholderAPI.setPlaceholders(player, backGroundText.text());
        String background = plugin.getBackgroundManager().getBackGroundImage(backGroundText, AdventureUtils.stripAllTags(parsed));
        if (backGroundText.remove_shadow()) background = "<#FFFEFD>" + background + "</#FFFEFD>";
        return ConfigManager.surroundWithFont(background) + parsed;
    }

    private String getDescent(String param, Player player) {
        DescentText descentText = placeholderManager.getDescentText(param);
        if (descentText == null) return param + " NOT FOUND";
        String parsed = PlaceholderAPI.setPlaceholders(player, descentText.text());
        return "<font:" + ConfigManager.namespace + ":" + "ascent_" + descentText.ascent() + ">" + parsed + "</font>";
    }

    private String getUnicodeDescent(String param, Player player) {
        if (!ConfigManager.enable1_20_Unicode && plugin.getVersionHelper().isVersionNewerThan1_20()) {
            return "Not Available on 1.20";
        }
        DescentText descentText = placeholderManager.getDescentUnicode(param);
        if (descentText == null) return param + " NOT FOUND";
        String parsed = PlaceholderAPI.setPlaceholders(player, descentText.text());
        return "<font:" + ConfigManager.namespace + ":" + "unicode_ascent_" + descentText.ascent() + ">" + parsed + "</font>";
    }
}
