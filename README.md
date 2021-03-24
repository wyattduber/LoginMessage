# How to Use

Simply Drop the plugin into your server, start/reload the server and let the config generate, then configure your custom messages!
Once you have added your custom message using the format noted below, simply add a permission node to the player/group of your choice 
with the formatting `lm.message.<message name>`. Here is an example of the config and the associated permission nodes:
```yml
check-for-updates: true
messages:
  - playerMOTD
  - staffMOTD
enable-first-time-message: false
first-time-message:
  - "&d&lWelcome to the Server, &a%PLAYER%&d!"
playerMOTD:
  - "&5============================="
  - "&f - &6Remember to Be Respectful to All Players."
  - "&f - &6Remember to Read the &e/rules&6!"
  - "&5============================="

staffMOTD:
  - "&5============================="
  - "&f - &6Big Changes Coming Next Month!"
  - "&f - &6Make sure to consult the punishment guide, do &e/punguide&6!"
  - "&5============================="
```

The associated permission nodes for each of these messages would be `lm.message.playerMOTD` and `lm.message.staffMOTD` respectively.

A few other things to note here: 
1. The `check-for-updates` boolean is simply to let the plugin automatically check for updates.
2. The `first-time-message` section has an enable boolean and is disabled by default.
3. __**If the first time message is enabled, none of the other messages will show on first join regardless of permission nodes**__.
4. The messages will run in the order that they are put in the config, so if you want one message to always run before others, it must be at the top of the list in the config.
