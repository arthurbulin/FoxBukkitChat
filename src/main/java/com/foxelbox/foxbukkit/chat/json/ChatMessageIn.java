/**
 * This file is part of FoxBukkitChat.
 *
 * FoxBukkitChat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FoxBukkitChat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FoxBukkitChat.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.foxelbox.foxbukkit.chat.json;

import com.foxelbox.foxbukkit.chat.FoxBukkitChat;
import com.foxelbox.foxbukkit.chat.Messages;
import com.foxelbox.foxbukkit.chat.ProtobufUUID;
import com.foxelbox.foxbukkit.chat.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChatMessageIn {
    public ChatMessageIn(FoxBukkitChat plugin, CommandSender commandSender) {
        this(plugin);
        if(commandSender instanceof Player)
            this.from = new UserInfo(Utils.getCommandSenderUUID(commandSender), commandSender.getName());
        else
            this.from = new UserInfo(Utils.CONSOLE_UUID, commandSender.getName());
    }

    public ChatMessageIn(FoxBukkitChat plugin) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.context = UUID.randomUUID();
        this.server = plugin.configuration.getValue("server-name", "Main");
    }

    private ChatMessageIn() {

    }

    public String server;
    public UserInfo from;

    public Long timestamp;

    public UUID context;
    public Messages.MessageType type;

    public String contents;

    public Messages.ChatMessageIn toProtoBuf() {
        Messages.ChatMessageIn.Builder builder = Messages.ChatMessageIn.newBuilder();

        if(server != null) {
            builder.setServer(server);
        }
        if(from != null) {
            builder.setFrom(Messages.UserInfo.newBuilder()
                    .setUuid(ProtobufUUID.convertJavaToProtobuf(from.uuid))
                    .setName(from.name));
        }

        builder.setTimestamp(timestamp);

        builder.setContext(ProtobufUUID.convertJavaToProtobuf(context));
        if(type != null && type != Messages.MessageType.TEXT) {
            builder.setType(type);
        }

        if(contents != null) {
            builder.setContents(contents);
        }

        return builder.build();
    }

    public static ChatMessageIn fromProtoBuf(Messages.ChatMessageIn message) {
        ChatMessageIn ret = new ChatMessageIn();

        ret.server = message.getServer();
        if(message.getFrom() != null) {
            ret.from = new UserInfo(ProtobufUUID.convertProtobufToJava(message.getFrom().getUuid()), message.getFrom().getName());
        }

        ret.timestamp = message.getTimestamp();

        ret.context = ProtobufUUID.convertProtobufToJava(message.getContext());
        ret.type = message.getType();

        ret.contents = message.getContents();

        return ret;
    }
}
