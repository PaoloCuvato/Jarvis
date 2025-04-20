package org.example.Bot;

import Config.Config;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import java.util.Arrays;
import java.util.EnumSet;

public class Bot extends ListenerAdapter {
public Bot(){
     // Get The Token from Properties File
     Config config = new Config();
     String token = config.getToken();
     System.out.println( token);

     DefaultShardManagerBuilder manager = DefaultShardManagerBuilder.createDefault(token);
     manager.setStatus(OnlineStatus.ONLINE);
     //enable all privilage to do things
     manager.enableIntents(EnumSet.allOf(GatewayIntent.class));
     manager.setMemberCachePolicy(MemberCachePolicy.ALL);
     // enable all The cache
     manager.enableCache(Arrays.asList(CacheFlag.values()));

 }
}