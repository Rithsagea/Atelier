# campaign

Syntax: `campaign`<br>
Description: Displays information about the selected campaign.<br>

## list

Syntax `campaign list`<br>
Description: Lists all existing campaigns.<br>

## select

Syntax: `campaign select <id>`<br>
Description: Selects a campaign with the given id.<br>
Parameters:
- UUID `id`: The id of the campaign.

## new

Syntax: `campaign new`<br>
Description: Creates a new campaign and returns its id.

## addcharacter

Syntax: `campaign addcharacter <id>`<br>
Description: Adds a character to the selected campaign.<br>
Options:
- UUID `id`: The id of the character

## removecharacter

Syntax: `campaign removecharacter <id>`<br>
Description: Removes a character from the selected campaign.<br>
Options:
- UUID `id`: The id of the character

## listcharacter

Syntax: `campaign listcharacter`<br>
Description: Lists all character's in a campaign. <br>