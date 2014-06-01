/**
 * This file is part of FoxBukkitChatComponent.
 *
 * FoxBukkitChatComponent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FoxBukkitChatComponent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FoxBukkitChatComponent.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.foxelbox.foxbukkit.chatcomponent;

import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerHelper {
    public static Map<String,String> playerNameToUUID = FBChatComponent.instance.redisManager.createCachedRedisMap("playerNameToUUID");
    public static Map<String,String> playerUUIDToName = FBChatComponent.instance.redisManager.createCachedRedisMap("playerUUIDToName");
    public static void refreshUUID(Player player) {
        playerUUIDToName.put(Utils.getPlayerUUID(player).toString(), player.getName());
        playerNameToUUID.put(player.getName().toLowerCase(), Utils.getPlayerUUID(player).toString());
    }

    public static void refreshPlayerListRedis(Player ignoreMe) {
        Player[] players = FBChatComponent.instance.getServer().getOnlinePlayers();
        final String keyName = "playersOnline:" + FBChatComponent.instance.configuration.getValue("server-name", "Main");
        FBChatComponent.instance.redisManager.del(keyName);
        for(Player ply : players) {
            if(ply.equals(ignoreMe))
                continue;
            FBChatComponent.instance.redisManager.lpush(keyName, Utils.getPlayerUUID(ply).toString());
        }
    }
}