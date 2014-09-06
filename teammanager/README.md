Commands
--------

All start with teamm. e.g. `teamm create`

### Create teams

Creates teams with the given names

`create [-r] teamName1 teamName2 ...` OR `create [-r] -n number`

-r = Give the teams random display names, if not set will use the given name
-n = Number of teams to create with auto generated names (UHCxxx)

### Remove teams

Deletes the teams with the given names

`remove [-ra] teamName1 teamName2 ...`

-r = Treat the team names as regex to match the team names
-a = Remove all teams

### Join Team

Join a team

### Leave Team

Leave a team

### Empty Team

Empties the team of all of its players and leave it intact

`empty [-a] teamName1 teamName2 ...`

-a = Empty all teams

### List teams

`list [-e]`

-e = Show empty teams too

### Random Teams

`random -n amount`

-n = Amount of teams to create, will make team sizes as even as possible

### Teamup

Adds all of the players given into a new team

`teamup -t teamName [-n display] name1 name2 name3 name4`

-t = team name to add into or create
-n = display name for the new team

No team
-------

Lists all online players without a team

`/noteam`

Pm team
-------

Send a message to everyone in your team

`/pmt message goes here`

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