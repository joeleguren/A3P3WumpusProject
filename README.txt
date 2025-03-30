En tots els mètodes de totes les Classes hem seguit l'enunciat, excepte si ha quedat obert a interpretació:

Observacions WumpusLaberynth:
- Mètode "void moveBats()", els bats poden moure's en qualsevol NormalCell deshabitada (no pot sobre el jugador), tant si esta oberta o tancada. En cas de cel·la oberta, el jugador no podrà veure al Bat/s.
- Mètode "boolean startleWumpus()", en cas de que el Wumpus s'asusti, caurà en qualsevol NormalCell deshabitada (no pot sobre el jugador), tant si esta oberta o tancada. En cas de cel·la oberta, el jugador no podrà veure al Wumpus.
- Mètode "int[] batKidnapping()", en cas de poder-se fer el segrest del jugador, el bat el podrà portar a qualsevol cel·la dins del tauler (inclou, PowerUpCell, WellCell, o qualsevol NormalCell), excepte sobre seu.
- Mètode "toString()", tenir en compte els comentaris integrats en el codi en cas que es volgués mode debugar.

Observacions LaberynthController:
En una ronda (jugador es mou), en cas que fos segrestat per un BAT, mètode "void traverseCell()" fa una crida recursiva.
Segons l'enunciat, aquest mètode inicialitza l’atribut messageTraverse, per tant, mai podrem veure per consola si hem sigut segrestats, solament l'última posició on es troba el jugador.
Hem decidit fer el següent:

- Mètode "void movePlayer(MovementDirection dir)", s'encarregarà d'esborrar traverseMessage per cada ronda (en cas de moure's).
- Mètode "void huntTheWumpus(ShootDirection dir)", s'encarregarà d'inicialitzar traverseMessage a "laberynth.currentCellMessage()" per cada ronda (en cas de disparar). D'aquesta manera si hem sigut anteriorment segrestats per un BAT, i immediatament disparem una fletxa, traverseMessage mostrarà la informació correcta.
- Mètode "void traverseCell()", ja no inicialitzarà traverseMessage, ara concatenarà. Així quan es faci la crida recursiva, no s'esborraran els missatges de segrest de ratpenat.

