# scene

Syntax: `scene`<br>
Description: Displays information about the selected scene.<br>

## list

Syntax: `scene list`<br>
Description: Lists all scenes in the selected campaign.<br>

## select

Syntax: `scene select <id>`<br>
Description: Selects a scene with the given id.<br>
Parameters:

- UUID `id`: The id of the scene.

## new

Syntax: `scene new`<br>
Description: Creates a new scene in the selected campaign and returns its id.<br>

## name

Syntax: `scene name`<br>
Description: Displays the name of the selected scene.<br>

Syntax: `scene name <name>`<br>
Description: Changes the name of the selected scene.<br>
Parameters:

- String `name`: The new name of the scene.

## addcharacter

Syntax: `scene addcharacter <id>`<br>
Description: Adds a character to the selected scene. <br>
Paramters:

- UUID `id`: The id of the character
