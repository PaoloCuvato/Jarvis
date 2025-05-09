package org.example.Log;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.GenericChannelUpdateEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.events.user.update.GenericUserUpdateEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class Logger extends ListenerAdapter {

    //Channel
    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        super.onChannelCreate(event);
    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        super.onChannelDelete(event);
    }

    @Override
    public void onGenericChannelUpdate(@NotNull GenericChannelUpdateEvent<?> event) {
        super.onGenericChannelUpdate(event);
    }

    // Guild
    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        super.onGuildBan(event);
    }

    @Override
    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        super.onGuildUnban(event);
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
            Guild guild = event.getGuild();
            User user = event.getUser();

            // Get the log channel (replace with your actual channel ID)
            TextChannel logChannel = guild.getTextChannelById("YOUR_LOG_CHANNEL_ID");

            if (logChannel != null) {
                // Retrieve audit logs for "kick" action
                guild.retrieveAuditLogs()
                        .type(ActionType.KICK)
                        .limit(5) // Checking the last 5 kick events
                        .queue(logs -> {
                            boolean wasKicked = false;

                            // Check if the user was kicked
                            for (AuditLogEntry entry : logs) {
                                if (entry.getTargetId().equals(user.getId())) {
                                    // Check if the kick happened recently (within 5 seconds)
                                    OffsetDateTime time = entry.getTimeCreated();
                                    OffsetDateTime now = OffsetDateTime.now();
                                    if (time.plusSeconds(5).isAfter(now)) {
                                        wasKicked = true;
                                        break; // Exit loop once we find the kick
                                    }
                                }
                            }

                            // Create embed for kick or left event
                            MessageEmbed embed;
                            if (wasKicked) {
                                // If the user was kicked
                                embed = new EmbedBuilder()
                                        .setTitle("Player Kicked")
                                        .setDescription(user.getAsTag() + " has been kicked from the server.")
                                        .setColor(Color.RED)
                                        .setTimestamp(OffsetDateTime.now())
                                        .setFooter(user.getName(), user.getAvatarUrl()) // Foto profilo nel footer
                                        .build();
                            } else {
                                // If the user left voluntarily
                                embed = new EmbedBuilder()
                                        .setTitle("Member Banned from the Server")
                                        .setDescription(user.getAsTag() + " has get banned.")
                                        .setColor(Color.red)
                                        .setTimestamp(OffsetDateTime.now())
                                        .setFooter(user.getName(), user.getAvatarUrl()) // Foto profilo nel footer
                                        .build();
                            }

                            // Send embed to the log channel
                            logChannel.sendMessageEmbeds(embed).queue();
                        });
            } else {
                System.out.println("Log channel not found!");
            }
        }

    // user  stuff
    @Override
    public void onUserUpdateAvatar(@NotNull UserUpdateAvatarEvent event) {
        User user = event.getUser();
        String newAvatarUrl = event.getNewAvatarUrl();       // Il nuovo avatar
        String timeFormatted = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"));

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("▬▬ User Avatar Updated ▬▬")
                .setColor(Color.decode("#ff6347"))
                .setDescription(
                        "**" + user.getAsMention() + "** has updated his avatar!\n\n" +
                                "> **Time:** `" + timeFormatted + "`\n" +
                                "> **User ID:** `" + event.getUser().getId() + "`"
                )
                .setImage(newAvatarUrl)
                .setFooter("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");


        // Ottieni il canale tramite l'ID (sostituisci con l'ID del canale desiderato)
        TextChannel logChannel = event.getJDA().getTextChannelById(1370349729182908566L);

        if (logChannel != null) {
            logChannel.sendMessageEmbeds(embed.build()).queue();
        } else {
            System.out.println("Il canale non esiste o non è accessibile.");
        }
    }

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {
        super.onUserUpdateName(event);
    }

    @Override
    public void onGenericUserUpdate(@NotNull GenericUserUpdateEvent event) {
        super.onGenericUserUpdate(event);
    }

    // message stuff
    @Override
    public void onGenericMessage(@NotNull GenericMessageEvent event) {
        super.onGenericMessage(event);
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        super.onMessageUpdate(event);
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        super.onMessageDelete(event);
    }

    @Override
    public void onMessageBulkDelete(@NotNull MessageBulkDeleteEvent event) {
        super.onMessageBulkDelete(event);
    }

    // reaction part
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        super.onMessageReactionRemove(event);
    }

    @Override
    public void onGenericMessageReaction(@NotNull GenericMessageReactionEvent event) {
        super.onGenericMessageReaction(event);
    }

    //Role
    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        super.onGuildMemberRoleAdd(event);
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        super.onGuildMemberRoleRemove(event);
    }

    @Override
    public void onRoleCreate(@NotNull RoleCreateEvent event) {
        super.onRoleCreate(event);
    }

    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event) {
        super.onRoleDelete(event);
    }

    @Override
    public void onRoleUpdateName(@NotNull RoleUpdateNameEvent event) {
        super.onRoleUpdateName(event);
    }
    // invites
    @Override
    public void onGuildInviteCreate(@NotNull GuildInviteCreateEvent event) {
        super.onGuildInviteCreate(event);
    }

    @Override
    public void onGuildInviteDelete(@NotNull GuildInviteDeleteEvent event) {
        super.onGuildInviteDelete(event);
    }

    // Voice
    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        super.onGuildVoiceUpdate(event);
    }

    @Override
    public void onGuildVoiceStream(@NotNull GuildVoiceStreamEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();
        boolean isStreaming = event.getVoiceState().isStream();

        TextChannel logChannel = guild.getTextChannelById(1370410340331945994L); // Replace with your log channel ID

        if (logChannel != null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(isStreaming ? Color.green : Color.red)
                    .setTitle("VC Stream Status Updated")
                    .setDescription("> **" + member.getAsMention() + "** " +
                            (isStreaming ? "started streaming!" : "stopped streaming."))
                    .setTimestamp(OffsetDateTime.now())
                    .setFooter(member.getNickname(), member.getAvatarUrl());

            logChannel.sendMessageEmbeds(embed.build()).queue();
        }
    }
    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        super.onGenericEvent(event);
    }
}
