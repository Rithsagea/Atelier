# Commands

## stop

Syntax: `stop`<br>
Description: Stops the bot.<br>

## user

Syntax: `user`<br>
Description: Displays information about the selected user.<br>

### user list

Syntax: `user list`<br>
Description: Lists all existing users.<br>

### user select

Syntax: `user select <id>`<br>
Description: Selects a user with the given id.<br>
Parameters:
- long `id`: The discord id of the user.

### user addcharacter

Syntax: `user addcharacter <id>`<br>
Description: Adds a character to the selected user.<br>
Options:
- UUID `id`: The id of the character

### user removecharacter

Syntax: `user removecharacter <id>`<br>
Description: Removes a character from the selected user.<br>
Options:
- UUID `id`: The id of the character

### user listcharacter

Syntax: `user listcharacter`<br>
Description: Lists a user's characters. <br>

## campaign

Syntax: `campaign`<br>
Description: Displays information about the selected campaign.<br>

### campaign list

Syntax `campaign list`<br>
Description: Lists all existing campaigns.<br>

### campaign select

Syntax: `campaign select <id>`<br>
Description: Selects a campaign with the given id.<br>
Parameters:
- UUID `id`: The id of the campaign.

### campaign new

Syntax: `campaign new`<br>
Description: Creates a new campaign and returns its id.

### campaign addcharacter

Syntax: `campaign addcharacter <id>`<br>
Description: Adds a character to the selected campaign.<br>
Options:
- UUID `id`: The id of the character

### campaign removecharacter

Syntax: `campaign removecharacter <id>`<br>
Description: Removes a character from the selected campaign.<br>
Options:
- UUID `id`: The id of the character

### campaign listcharacter

Syntax: `campaign listcharacter`<br>
Description: Lists all character's in a campaign. <br>

## character

Syntax: `character`<br>
Aliases: `char`<br>
Description: Displays information about the selected character.<br>

### character list

Syntax: `character list`<br>
Description: Lists all existing characters.<br>

### character select

Syntax: `character select <id>`<br>
Description: Selects a character with the given id.<br>
Parameters:
- UUID `id`: The id of the character.

### character new

Syntax: `character new`<br>
Description: Creates a new character and returns its id.

### character name

Syntax: `character name`<br>
Description: Displays the name of the selected character.<br>

Syntax: `character name <name>`<br>
Description: Changes the name of the selected character.<br>
Parameters:
- String `name`: The new name of the character.

### character ability

Syntax: `character ability`<br>
Description: Displays the selected character's ability scores

Syntax: `character ability <ability> <score>`<br>
Description: Changes the ability score of the selected character.<br>
Parameters:
- Ability `ability`: The ability to change, `str|dex|con|int|wis|cha`
- String `score`: The ability score

## campaign

### campaign new

Syntax: `scene new`<br>
Description: Creates a new scene in the selected campaign and returns its id.<br>

### campaign list

Syntax: `scene list`<br>
Description: Lists all scenes in the selected campaign.<br>