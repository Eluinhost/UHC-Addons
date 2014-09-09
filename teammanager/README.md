Commands
--------

### Create teams

Creates teams with the given names

`/teamm create [-r] teamName1 teamName2 ...` OR `create [-r] -n number`

-r = Give the teams random display names, if not set will use the given name
-n = Number of teams to create with auto generated names (UHCxxx)

Permissions:

UHC.teams.create default op - required to run the command

### Delete teams

Deletes the teams with the given names

`/teamm delete [-a] [teamName1 teamName2] ...`

-a = Remove all teams

Permissions:

UHC.teams.delete default op - required to run the command

### Join team

Join a specfic team

`/teamm join teamName`

Permissions:

UHC.teams.join default false - required to run the command

### Leave team

Leave the team you are in

`/teamm leave`

Permissions:

UHC.teams.leave default false - required to run the command

### Add to team

Adds the player to the team

`/teamm add -t teamName player1 player2 player3 ...`

-t = name of the team to add to, required

Permissions:

UHC.teams.add default op - required to run the command

### Remove from team

Removes players from their teams

`/teamm remove player1 player2 player3 ...`

Permissions:

UHC.teams.remove default op - required to run the command

### List teams

Lists all of the players in each team

`/teamm list [-a]`

-a = Show empty teams too

Permissions:

UHC.teams.list default true - required to run the command

### Teamup

Adds all of the players given into a new team

`/teamm teamup [-t teamName] [-n display] name1 name2 name3 name4`

-t = team name to create, if not provided with auto-generate a new one
-n = display name for the new team, if not provided will use a random one

Permissions:

UHC.teams.teamup default op - required to run the command

### Random Teams

Generates n amount of teams with as even as possible amount of memebers.
Uses all players online excluding those already in a team.

`/randomteams [-n amount]`

-n = Amount of teams to create, will make team sizes as even as possible, default 4, must be <= amount of online players
already in a team

Permissions:

UHC.teams.random default op - required to run the command

No team
-------

Lists all online players without a team

`/noteam`

Permissions:

UHC.teams.noteam default true - required to run the command

Pm team
-------

Send a message to everyone in your team

`/pmt message goes here`

Permissions:

UHC.teams.pm default true - required to run the command






Team requests
-------------

### Request team

Request to team with players

`/reqteam request name1 name2 name3 name4 ...`

### Accept team request

`/reqteam accept -t teamName [-d  displayName] id1 id2 id3 ...`

### Deny team request

`/reqteam deny id1 id2 id3 ...`

### List requests

`/reqteam list`