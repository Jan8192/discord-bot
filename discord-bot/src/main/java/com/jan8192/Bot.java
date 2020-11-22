package com.jan8192;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * Hello world!
 *
 */
public class Bot extends ListenerAdapter {
    
    Random rng = new Random(); 
    
    
    
    public static void main(String[] args) throws LoginException {
        JDABuilder
                .createLight("NzgwMDUzMDg4MzcwNjg4MDIw.X7pfDA.u7nY3ZIwwV1JmH1db2SnxMwU0iU",
                        GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new Bot()).setActivity(Activity.watching("your mum playing with herself")).build();
    }



    public List<String> getAndLoadInsult() throws IOException {
        List<String> insults = new ArrayList<>();
        String line = "";

        var br = new BufferedReader(new FileReader("persistance"));
            while ((line = br.readLine()) != null) {
                var country = Arrays.asList(line.split(","));

                insults.addAll(country);
                
            }

        return insults;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        var user = event.getAuthor();
        var userTag = user.getId();

        if (user.isBot()) {
            return;
        }

        if(msg.getContentRaw().equals("man insult")){
            MessageChannel channel = event.getChannel();

            var respMessage = "These options are available: \n" + 
                              "!insutls - lists all insults \n" +
                              "!insultadd <arg> - adds a new insult, where arg represents the respective insult";

            channel.sendMessage(respMessage).queue();

            return;
        }

        if (msg.getContentRaw().equals("!insultls")) {
            try {
                var insults = getAndLoadInsult();

                MessageChannel channel = event.getChannel();

                var responseMessage = "Stored Insults: \n";

                for (var insult : insults) {
                    responseMessage += insult + "\n";
                }


                channel.sendMessage(responseMessage).queue();


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return;
        }

        if (msg.getContentRaw().contains("!insultadd")) {

            String insult = msg.getContentRaw().replaceAll("!insultadd", "");
            BufferedWriter writer;
            MessageChannel channel = event.getChannel();

            try {
                writer = new BufferedWriter(new FileWriter("persistance", true));
                writer.append(insult + ",");
                writer.close();
                    
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            channel.sendMessage(insult + " added.").queue(); /* => RestAction<Message> */
            return;
        }

        if (msg.getContentRaw() != null) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();

            String respMessage = "<@" + userTag + "> ist ein hurensohn";

            System.out.println(user.getName());

            try {
                var insults = getAndLoadInsult();

                var responseMessage = "<@" + userTag + ">" + insults.get(rng.nextInt(insults.size() - 1));

                channel.sendMessage(responseMessage).queue();

            } catch (IOException e) {
                e.printStackTrace();
            }

            
        }

    }
}
