# :speech_balloon: ChatChannels
### A clean and efficient way to organize your server chat
![Header image](http://image.prntscr.com/image/21438b93033c412b9547644d2089a3c3.png)

### Motivation
Chats tend to get pretty messy, especially on servers with large quantities of players. ChatChannels provides an easy mechanism to sort all conversations into various channels which players can easily view or hide at their own discretion.

For most servers, this solution can be quite helpful in organizing various server events, announcements, etc...

## Setup
1. Download the plugin jar from the [Spigot Page](https://www.spigotmc.org/resources/chatchannels.39100/)
2. Drag and drop (or upload) the file to your `plugins` folder
3. Configure the plugin to your specific needs
4. **Voilà**, it's that simple.

## Commands/Permissions
Channel configurations allow for custom permissions, selected at your discretion, additionally the plugin uses a few permissions for commands.

![Chat Commands](http://image.prntscr.com/image/6897b3cefb194a94a3142b39938f4267.png)
![Chat Admin Commands](http://image.prntscr.com/image/76c006594986493ca93c2a39aa87273c.png)

|       **Permission**       |                                            **Action**                                            |                                          **Nested Commands**                                          | **Who should this be applied to?** |
|:--------------------------:|:------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------:|:----------------------------------:|
| chatchannels.cmd.chat      | The root command for all /chat commands                                                          | `/chat focus <channel>` `/chat show <channel>` `/chat hide <channel>`                                 | Players                            |
| chatchannels.cmd.chatadmin | The root command for all /chatAdmin commands                                                     | `/chatAdmin mute <channel>` `/chatAdmin clear <channel>` `/chatAdmin reload` | Administrators                     |
| chatchannels.bypass-mute   | Allows a player to bypass a channel mute and continue to send messages                           | N/A                                                                                                   | Administrators                     |
| chatchannels.update-notify | Sends a message to a player when they join alerting them of a new plugin version (if applicable) | N/A                                                                                                   | Administrators                     |

## Configuration
The plugin's `config.yml` outlines exactly what is required to setup a channel (assuming you're using the default data strategy)

```yml
   ****************************
     EXAMPLE CHANNEL LAYOUT
   ****************************
  
    demo:
      ## The name of the channel you are creating
      ## This value is displayed in most plugin messages
      ## If your name contains spaces users will have to replace them with '_' in their command
      ## Ex: "You have hidden Demo Channel"
      name: "Demo Channel"
  
      ## The description of the channel
      ## This is only shown in select places in the plugin
      description: "A basic channel created for the purpose of demonstrating the plugin"
  
      ## The short-hand code for the channel
      ## Usually a single character (e.g. "A", "B", "C", "STAFF")
      identifier: "D"
  
      ## Aliases that can be used when running channel commands
      ## Primarily helpful when switching channels, (e.g. "/chat focus D")
      aliases:
        - "D"
        - "Test"
  
      properties:
        ## The color of the channel, will be applied to all references to the channel
        color: "&7"
  
        ## The color of the messages displayed in this channel
        chat-color: "&f"
  
        ## The enforced delay between messages
        ## Measured in seconds
        cooldown: 1.0
  
        ## How close must a player be to the sender for them to see their messages
        ## If this value is '-1' then this feature will be toggled off for this specific chat
        ## If toggled on, users will not be able to see channels across different worlds
        chat-radius: -1
  
        ## The required permission level needed to chat/focus/view this channel
        ## If the permission node is "*" then all users will be able to interact with the channel
        permission: "*"
  
        ## Should players automatically view this channel when they join the server?
        auto-view: true
  
        ## Should players automatically focus on this channel when they join the server?
        ## Note: If this option is toggled for multiple channels then the user will be focued on whatever channel was processed last
        auto-focus: false
  
```

_Note: This is based off the assumption you're utilizing the bundled "simple" registry. Use may vary based on how you have your plugin setup_

## Bug Reports/Tracking
If you have noticed a bug/issue in the plugin, then please fill out the form [here](https://github.com/codenameflip/ChatChannels/issues/new) and it will be dealt with by either myself, or an open-source junkie :heart:

## Developers
If you would like to utilize the plugin's channel API in your own plugins then take a look at the examples below!
Additionally you may access the project's javadocs [here]()

### Creating a channel
```java
// TODO: Update this with the finalized code structure
```

### ChannelChatEvent
If you would like to intercept a player chatting in a certain channel, there's an event for that!

```java
// TODO: Update this with the finalized code structure
```

## Potential Feature List
- ~~Auto update notifier~~  
  ![autoupdate screenshot](http://image.prntscr.com/image/440252d9f7c24751aca638bed4459ca5.png)
- Per world channels
- ~~Chat channel radius~~  
  ![chat radius screenshot](http://image.prntscr.com/image/acd11e5bf88842c1a7ee0fc0d0c9f693.png)  
  ![chat radius screenshot 2](http://image.prntscr.com/image/85c973df436146b8b8d951833ea3ebe6.png)
- WorldGuard channel integration
- Channel spam protection
- Permission-based chat cooldowns
- Highlighted names in chat (if you are mentioned in chat your name would be highlighted a different color)
- **Feel free to create an issue for a feature suggestion**
