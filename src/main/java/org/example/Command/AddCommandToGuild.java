package org.example.Command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AddCommandToGuild extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        Long guildId = 856147888550969345L; // Sostituisci con l'ID della tua guild
        Guild guild = event.getJDA().getGuildById(guildId);

        if (guild != null) { // Verifica che la guild e
            // Aggiunta dei comandi locali
            guild.upsertCommand("info", "Info about the bot").queue();
            guild.upsertCommand("clear", "Clear the chat of a specific channel").queue();
            guild.upsertCommand("commands", "Info about all the commands of the bot").queue();
            guild.upsertCommand("vc", "Create a Temporary VC").queue();
            guild.upsertCommand("report_embeded", "This Command will send the embeded of the ticket system").queue();


            System.out.println("Commands updated successfully for guild: " + guild.getName());
        } else {
            System.err.println("Guild with ID " + guildId + " not found.");
        }
    }
}
