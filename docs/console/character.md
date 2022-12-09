# character

Syntax: `character`<br>
Aliases: `char`<br>
Description: Displays information about the selected character.<br>

## list

Syntax: `character list`<br>
Description: Lists all existing characters.<br>

## select

Syntax: `character select <id>`<br>
Description: Selects a character with the given id.<br>
Parameters:
- UUID `id`: The id of the character.

## new

Syntax: `character new`<br>
Description: Creates a new character and returns its id.

## name

Syntax: `character name`<br>
Description: Displays the name of the selected character.<br>

Syntax: `character name <name>`<br>
Description: Changes the name of the selected character.<br>
Parameters:
- String `name`: The new name of the character.

## ability

Syntax: `character ability`<br>
Description: Displays the selected character's ability scores

Syntax: `character ability <ability> <score>`<br>
Description: Changes the ability score of the selected character.<br>
Parameters:
- Ability `ability`: The ability to change, `str|dex|con|int|wis|cha`
- String `score`: The ability score