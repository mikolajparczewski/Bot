package com.github.mikolajparczewski;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;

public final class Bot {
    public static void main(final String[] args) {
        if (args.length == 0) return; // jeśli token nie zostanie podany, bot nie uruchomi się
        final String token = args[0]; // zapisanie tokenu w zmiennej

        final DiscordClient client = DiscordClient.create(token); // utworzenie nowego klienta umożliwiającego komunikację z API Discorda

        client.login() // utworzenie połączenia z Discordem
                .flatMapMany(gateway -> gateway.on(MessageCreateEvent.class)) // bot będzie reagował za każdym razem, gdy zauważy nową wiadomość
                .map(MessageCreateEvent::getMessage) // wydobywamy wiadomość z całego MessageCreateEvent
                .filter(message -> message.getContent().equalsIgnoreCase("!ping")) // sprawdzamy, czy zawartość wiadomości to "!ping" niezależnie od wielkości znaków
                .flatMap(Message::getChannel) // wydobywamy kanał, na którym została wysłana wiadomość
                .flatMap(channel -> channel.createMessage(EmbedCreateSpec.create().withDescription("Pong!"))) // wysyłamy wiadomość typu Embed z opisem "Pong!" na kanał, na którym wysłane zostało "!ping"
                .blockLast(); // działanie i "oczekiwanie" do momentu zakończenia pracy bota (np. zabicie procesu)
    }
}
