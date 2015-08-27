This is a simple implementation of a console text-based Cluedo Game.
The game has implemented all the rules stated in the Assignment Description.

To run the game in Eclipse, you need to manually drag gameBoard.txt file 
under the project level and run the game, otherwise it would not find the 
game board file. Alternatively, you can run the game from terminal under bin 
file type the command:
	% java cluedo.TextClient gameBoard.txt
to start the game.

Due to the undetailed rules given in the hand out, some assumptions were made-
during the implementation. For example, a room is considered as one square, 
that is when a player is in a room, he/she can exit from any exits by only one 
move. Also when player chooses to exit the room, they actually go to the door 
first and the following move make them totally move out from the room.
When a player enters to the entrance, the token will be put to a random square 
in the room, this is to prevent the situation where one player is at the
entrance, another will have to go on top of it if he/she wants to enter.
However there is still a chance that more than one player or token will be
randomly put on the same square.

Some other assumptions were also made either by common sense or the real game
rules, overall it is easy to follow.
