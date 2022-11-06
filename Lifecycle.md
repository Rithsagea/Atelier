# Lifecycle
This document details the execution of this bot. I am too lazy to make a mermaid graph, so a text wall should suffice

## Startup
- Load Config File (`config.properties`)
- Initialize Console
	- Register Console Commands
	- Start Console Thread
- Initialize DB
	- Register Serialization Codecs
	- Connect to Mongo database
	- Download and Initialize database objects
		- Load Characters
			- `LoadCharacterEvent`
			- Load Character Classes + Features
				- `LoadCharacterClassEvent`
			- Load Proficiencies
				- `LoadSavingProficiencyEvent`
				- `LoadSkillProficiencyEvent`
- Initialize Discord
	- Connect to Discord Bot
	- Register Discord Listeners
		- Load Commands
			- Register Discord Commands
			- Upsert Guild and Global Commands
- Start DB sync task