# ChatChannels :speech_balloon:
A clean and efficient way to organize your server chat
![Header image](http://image.prntscr.com/image/21438b93033c412b9547644d2089a3c3.png)

### Why?
Chats tend to get pretty messy, especially on servers with large quantities of players. ChatChannels easily sorts all conversations into various channels which players can easily view or hide at their own will.

Servers which have an open-ended survival aspect may find this plugin more useful, for things like auctioning/trading, KOTH chats, public server events, staff chats, etc...

## Setup
1. Download the plugin jar from the [Spigot Page](https://www.spigotmc.org/resources/chatchannels.39100/)
2. Drag and drop the file in your `plugins` folder
3. Configure the plugin to your specific needs
4. **Voilà**, it's that simple.

## Permissions/Commands Used
Channel configurations allow for custom permissions, selected at your discretion, additionally the plugin uses a few permissions for commands.

- `chatchannels.cmd.chat` Gives players permission to use `/chat` (to focus, hide, and show channels)
- `chatchannels.cmd.chatadmin` Gives staff permission to use `/chatAdmin` (to clear, mute and see channel data)
- `chatchannels.bypass-mute` Gives players/staff permission to bypass channel mutes

_(Aliases for all commands are indicated by the **bold** highlighting in the command usage, ex: "f", "s", "h")_

![Chat Commands](http://image.prntscr.com/image/6897b3cefb194a94a3142b39938f4267.png)
![Chat Admin Commands](http://image.prntscr.com/image/76c006594986493ca93c2a39aa87273c.png)

_(This list will be updated accordingly as more permissions are introduced/modified)_

## Configuration
The plugin's [`config.yml`](https://github.com/codenameflip/ChatChannels/blob/master/src/main/resources/config.yml) looks a little bit intimidating, so let's break it down into 2 sections

There is the **chat-settings** section and the **channels** section, both are crucial to the plugin

### chat-settings
In the chat settings there are values for the following:
- `format`
- `announce-permissions`
- `log-messages`

  #### format
  This is the value that the plugin uses when deciding how the chat should look, there are placeholders for each component to a chat message. Place holders inclue: `(COLOR) (IDENTIFIER) (CHANNEL) (PLAYER) (MESSAGE)`
  
  Ex: `"[(COLOR)(IDENTIFIER)] (PLAYER): (MESSAGE)"`
  _**(Note: Color codes work inside the format value, ex: "&c")**_
  
  In the example above, if a player sent a message in the game chat, it would look like: `"[ANNOUNCEMENTS] codenameflip: Test message!"`
  
  #### announce-permissions
  This value toggles whether or not the plugin tells the player what permissions they need to chat in a certain channel
  
  ![announce-permissions image](http://image.prntscr.com/image/0df426357364498fa66b9f826e992c93.png)
  
  #### log-messages
  This value toggles whether all chat messages from channels are logged to console
  
  ![log-messages image](http://image.prntscr.com/image/64366f7358ca483e9000c836f415fc42.png)
  
### channels
The channels section configures all the values for your various chat channels.

Refer to the `config.yml` notes for more clarification on what the various values do 

## Bug Reports/Tracking
If you have noticed a bug/issue in the plugin, then please fill out the form [here](https://github.com/codenameflip/ChatChannels/issues/new) and it will be dealt with by either myself, or an open-source junkie :heart:

## Developers
If you would like to utilize the plugin's channel API in your own plugins then take a look at the examples below!

### Creating a channel
```java
Channel channel = new Channel("XD", "My super cool channel name!", "A channel for some dank memers"); // Identifier, Name, Description

channel.setPermission(...);
channel.setColor("&d&l");
channel.setChatColor("&5");
channel.setCooldown(420.0);
// ...and so on...
```

_(Note: Channel objects will not automatically save the configuration file; you will have to do that manually)_

### ChannelChatEvent
If you would like to intercept a player chatting in a certain channel, there's an event for that!

```java
@EventHandler
public void onChat(ChannelChatEvent event) {
  Player player = event.getSender(); // The player who sent the message
  Channel channel = event.getTargetChannel(); // The channel the message is sent to
  String message = event.getMessage(); // The message the player sent
  
  if (!channel.getName().equalsIgnoreCase("My super cool channel name!"))
    event.setCancelled(true); // You can also stop the message from sending to the channel as well!
}
```

## Potential Feature List
- ~~Auto update notifier~~
- Per world channels
- Chat channel radiuses
- WorldGuard channel integration
- Channel spam protection
- Permission-based chat cooldowns
- Highlighted names in chat (if you are mentioned in chat your name would be highlighted a different color)
- **Feel free to PM me on Spigot to request a new feature**
