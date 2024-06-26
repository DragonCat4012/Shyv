package ex.com.challenge.service;

import ex.com.challenge.exception.GameException;
import ex.com.challenge.model.Game;
import ex.com.challenge.model.Player;
import ex.com.challenge.model.Sow;
import ex.com.challenge.repository.GameRepository;
import ex.com.challenge.model.GameStatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author: e.shakeri
 */

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final SowService sowService;

    public Game createGame(Player player) {
        Game game = new Game();
        game.setId(UUID.randomUUID().toString());
        game.setFirstPlayer(player);
        game.setConnectedPlayers(Arrays.asList(player));

        game.setStatus(GameStatusEnum.NEW);
        gameRepository.save(game);
        return game;
    }

    public Game connectToGame(Player player, String gameId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);

        optionalGame.orElseThrow(() -> new GameException("Game with provided id doesn't exist"));
        Game game = optionalGame.get();

        /*   if (game.getSecondPlayer() != null) {
            throw new GameException("Game is not valid anymore");
        }*/

        game.setSecondPlayer(player);
        var arr = game.getConnectedPlayers();
        arr.add(player);
        game.setConnectedPlayers(arr);
        game.setStatus(GameStatusEnum.IN_PROGRESS);
        gameRepository.save(game);
        return game;
    }

    public void disconnectPlayer(String sessionID) {
        List<Game> optionalGame = gameRepository.findAll();
        System.out.println(optionalGame);

        for (Game game : optionalGame) {
            var arr = game.getConnectedPlayers();
            arr.stream().filter(x -> x.getSessionID() == sessionID).toArray();

            for (Player player : arr) {
                player.setConnected(false);
            }
            gameRepository.save(game);
        }
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll().stream().sorted(Comparator.comparing(Game::getCreatedAt).reversed()).toList();
    }

    public List<Game> getAllCurrentGames() {
        return gameRepository.findAll().stream().filter(e -> e.getStatus() != GameStatusEnum.FINISHED)
                .sorted(Comparator.comparing(Game::getCreatedAt).reversed()).toList();
    }

    public void endGame(String id) {
        var game = gameRepository.findById(id);

        if (game == null || game.get() == null) {
            System.out.println("game to end not found:");
            return;
        }
        Game gettedGame = game.get();
        gettedGame.setStatus(GameStatusEnum.FINISHED);
        gameRepository.save(gettedGame);
    }

    public Game connectToRandomGame(Player player) {
        Optional<Game> optionalGame = gameRepository.findFirstByStatusAndSecondPlayerIsNull(GameStatusEnum.NEW);
        optionalGame.orElseThrow(() -> new GameException("There is no available Game!"));
        Game game = optionalGame.get();
        game.setSecondPlayer(player);
        game.setStatus(GameStatusEnum.IN_PROGRESS);
        gameRepository.save(game);
        return game;
    }

    public Game sow(Sow sow) {
        Optional<Game> optionalGame = gameRepository.findById(sow.getGameId());

        optionalGame.orElseThrow(() -> new GameException("Game with provided id doesn't exist"));
        Game game = optionalGame.get();
        game.setLastStr(sow.getNewName());

        //    Game gameAfterSow=sowService.sow(game,sow.getPitIndex());
        gameRepository.save(game);

        return game;
    }
}
