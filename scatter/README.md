#Scatter Addon

##Commands

###/sct

####Options (bold = required):

**-t** - The type of scatter to use, 'circle' or 'square' are valid options

**-c** - The center of the scatter in the format `world,x,z`

**-r** - The radius of the scatter

--min - The minimum radius from players when scattering

--minradius - The minimum radius from the centre

--teams - Scatter as teams instead of individually

All Other Args: Player names to scatter or `*` for all players

####Examples

`/sct -r 1000 -c=world,100,-200 -t circle *`

Scatters all players within a 1000 radius circle centered on 100,-200 in the world 'world'

`/sct -r=1000 -t square -c village,1000,2000 --minradius 100 ghowden fuzzboxx`

Scatters ghowden and fuzzboxx within a 1000 radius square centered on 1000,2000 in the world 'village' with a buffer of 100 from the center

####Permissions

UHC.scatter.command - Allows usage of the /sct command