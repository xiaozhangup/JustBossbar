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

package net.momirealms.customnameplates.helper;

import net.momirealms.customnameplates.CustomNameplates;
import net.momirealms.customnameplates.manager.ConfigManager;
import net.momirealms.customnameplates.utils.AdventureUtils;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class VersionHelper {

    private boolean isNewerThan1_19_R2;
    private boolean isNewerThan1_20;
    private String serverVersion;
    private final int pack_format;

    public VersionHelper() {
        this.initialize();
        this.pack_format = getPack_format(serverVersion);
    }

    public void initialize() {
        if (serverVersion == null) {
            this.serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            String[] split = serverVersion.split("_");
            int main_ver = Integer.parseInt(split[1]);
            if (main_ver >= 20) isNewerThan1_19_R2 = true;
            else if (main_ver == 19) isNewerThan1_19_R2 = Integer.parseInt(split[2].substring(1)) >= 2;
            else isNewerThan1_19_R2 = false;
            isNewerThan1_20 = main_ver >= 20;
        }
    }

    public boolean isVersionNewerThan1_19_R2() {
        return isNewerThan1_19_R2;
    }

    public boolean isVersionNewerThan1_20() {
        return isNewerThan1_20;
    }

    private int getPack_format(String version) {
        switch (version) {
            case "v1_20_R1" -> {
                return 15;
            }
            case "v1_19_R3" -> {
                return 13;
            }
            case "v1_19_R2" -> {
                return 12;
            }
            case "v1_19_R1" -> {
                return 9;
            }
            case "v1_18_R1", "v1_18_R2" -> {
                return 8;
            }
            default -> {
                return 7;
            }
        }
    }

    public int getPack_format() {
        return pack_format;
    }

}
