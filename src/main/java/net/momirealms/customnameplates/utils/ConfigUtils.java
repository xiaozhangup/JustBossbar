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

package net.momirealms.customnameplates.utils;

import net.momirealms.customnameplates.CustomNameplates;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ConfigUtils {

    public static YamlConfiguration getConfig(String config_name) {
        File file = new File(CustomNameplates.getInstance().getDataFolder(), config_name);
        if (!file.exists()) CustomNameplates.getInstance().saveResource(config_name, false);
        return YamlConfiguration.loadConfiguration(file);
    }

    public static YamlConfiguration readData(File file) {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) {
                    AdventureUtils.consoleMessage("<red>[JustBossbar] Failed to generate data files!</red>");
                }
            } catch (IOException e) {
                e.printStackTrace();
                AdventureUtils.consoleMessage("<red>[JustBossbar] Failed to generate data files!</red>");
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static int rgbToDecimal(String rgba) {
        String[] split = rgba.split(",");
        int r = Integer.parseInt(split[0]);
        int g = Integer.parseInt(split[1]);
        int b = Integer.parseInt(split[2]);
        int a = Integer.parseInt(split[3]);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
