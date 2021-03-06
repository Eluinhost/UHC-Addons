Player Head Addon
=================

### Give head command

`/givehead [-p playerName] [-n amount] player1 player2 player3 player4`

-p = Player name to give the head to, defaults to the player issuing the command

nonOptions = list of names to give a head for

-n = Amount of heads to give, default 1

-g = Give golden heads instead

Permissions:

UHC.heads.give default op - required to use the command

### Healing amount command

`/headhealth [-a amount]`

-a = Amount of half hearts to heal from golden heads, if not provided just tells you how much it is set to

NOTE: if half hearts is set below a regular golden apple it will still heal the amount of a golden apple

Permissions:

UHC.heads.sethealth default op - required to run the command

### Head drop feature

Feature Name: PlayerHeads

Players will drop their heads on death

Permissions:

UHC.heads.onStake default true, heads will attempt to be placed on a stake on death
UHC.heads.drop default true, heads will drop on death

Configuration:

```
attempt on stake: true          # Attempt to place stakes
chance to drop: 100             # Chance to drop on death
drop on pvp death only: true    # Limit heads to drop on PvP only
ignore teamkills: true          # Never drop on teamkills
```

### Golden heads feature

Feature Name: GoldenHeads

Can craft golden heads by surrounding a player head in gold

Permissions:

UHC.heads.craft default true, will be allowed to craft

Configuration:

```
golden heads heal total: 6    # Amount of HALF hearts to heal total. If less than a regular golden apple only the golden apple will apply
```