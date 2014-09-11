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
