Shyv
---
Webscoket Game Base idk yet xd


Technologies
------------
- `Spring Boot`
- `WebSocket`  for getting changes and refreshing game board
- `MongoDB` for persisting the Game information
- `HTML and js` for providing simple UI and calling rest service
- `Docker` for containerization of services
- `Docker-Compose`  to link the containers


### How to launch
First, you should build and package jar file with Maven.
Then,  I provided a docker file, so you should create an image.
Finally, by the docker-compose file, you can launch it.
Via the home page (http://localhost:8080/) you can access to the game UI.

[Base Source](https://ehsanasadev.github.io/Create_interactive_game_with_Spring_Boot_and_WebSocket/)
