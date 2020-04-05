### Description
A better chat rate limit.  
Instead of a hard limit of 1 message every 2 seconds, its 5 messages every 15 seconds.  
This allows for short burst of messages but not spamm.  

### Building a Jar

1) download src
2) run gradlew.bat
3) go to the plugin folder in cmd. (example: `cd C:\user\one\desk\pluginfolder\`)
4) type `gradlew jar` and execute
5) done, look for plugin.jar in pluginfolder\build\libs\

Note: Highly recommended to use Java 8.

### Installing

Simply place the output jar from the step above in your server's `config/mods` directory and restart the server.
List your currently installed plugins/mods by running the `mods` command.

If you want to adjust any settings, just change the settings.json

It should look like this
```
{
  "bcl":
  {
	  "messages": <max messages>,
	  "timespan": <every this seconds>
  }
}
```
If you have more than one mod using settings.json, it should look like this
```
{
  "mod1":
  {
    "stuff": ""
  },
  "bcl":
  {
	  "messages": <max messages>,
	  "timespan": <every this seconds>
  }
}
```
### Self Promotion
Our discord server: http://cn-discord.ddns.net  
Our game servers:  
chaotic-neutral.ddns.net:1111  
chaotic-neutral.ddns.net:2222  
