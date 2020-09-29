package nl.rabbink.simplechess.rest.resources;

import com.github.bhlangonijr.chesslib.game.Game;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nl.rabbink.simplechess.rest.dto.BoardDTO;
import nl.rabbink.simplechess.rest.dto.GameDTO;
import nl.rabbink.simplechess.rest.dto.MoveDTO;
import nl.rabbink.simplechess.services.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
public class GameController {

  private final GameService gameService;

  private final ModelMapper modelMapper;

  public GameController(GameService gameService, ModelMapper modelMapper) {
    this.gameService = gameService;
    this.modelMapper = modelMapper;
  }

  @Operation(summary = "Get all games")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the games")})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<GameDTO>> getGames() {
    List<Game> games = gameService.getGames();

    List<GameDTO> dtos = mapList(games, GameDTO.class);

    return ResponseEntity.ok(dtos);
  }

  @Operation(summary = "Create a game")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Game created")})
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> createGame() {
    final UUID uuid = gameService.createGame();

    Map<String, Object> result = new HashMap<>();
    result.put("gameId", uuid);

    return ResponseEntity.ok(result);
  }

  @Operation(summary = "Get a game by its id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the game"),
      @ApiResponse(responseCode = "404", description = "Game not found",
          content = @Content) })
  @GetMapping(value = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GameDTO> getGameById(@PathVariable UUID gameId) {
    final Game game = gameService.getGameById(gameId);

    GameDTO dto = modelMapper.map(game, GameDTO.class);

    return ResponseEntity.ok(dto);
  }

  @Operation(summary = "Get a gameboard")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the board"),
      @ApiResponse(responseCode = "404", description = "Game not found",
          content = @Content) })
  @GetMapping(value = "/{gameId}/board", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BoardDTO> getBoardByGameId(@PathVariable UUID gameId) {
    final Game game = gameService.getGameById(gameId);

    BoardDTO dto = modelMapper.map(game.getBoard(), BoardDTO.class);

    return ResponseEntity.ok(dto);
  }

  @Operation(summary = "Move a piece")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Piece moved"),
      @ApiResponse(responseCode = "400", description = "Invalid Input",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Game not found",
          content = @Content) })
  @PostMapping(value = "/{gameId}/move", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
      MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<GameDTO> doMove(@PathVariable UUID gameId, @Valid @RequestBody MoveDTO moveDTO) {
    final Game game = gameService.move(gameId, moveDTO.getFrom(), moveDTO.getTo());

    GameDTO dto = modelMapper.map(game, GameDTO.class);

    return ResponseEntity.ok(dto);
  }

  private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
    return source
        .stream()
        .map(element -> modelMapper.map(element, targetClass))
        .collect(Collectors.toList());
  }

}
