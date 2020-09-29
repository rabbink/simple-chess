package nl.rabbink.simplechess.services;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.game.Game;
import com.github.bhlangonijr.chesslib.game.GameResult;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import nl.rabbink.simplechess.exception.GameNotFoundException;
import nl.rabbink.simplechess.exception.IllegalMoveException;
import nl.rabbink.simplechess.exception.IncorrectSideException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

  private final List<Game> games = new ArrayList<>();

  public synchronized List<Game> getGames() {
    return Collections.unmodifiableList(games);
  }

  public synchronized UUID createGame() {
    UUID uuid = UUID.randomUUID();
    Game game = new Game(uuid.toString(), null);
    game.setBoard(new Board());

    games.add(game);

    return uuid;
  }

  public synchronized Game getGameById(UUID gameId) {
    Game result = games.stream()
        .filter(game -> game.getGameId().equals(gameId.toString()))
        .findAny()
        .orElse(null);

    if (result == null) {
      throw new GameNotFoundException("Invalid game id: " + gameId);
    }

    return result;
  }

  public synchronized Game move(UUID gameId, String from, String to) {
    final Game game = getGameById(gameId);
    final Board board = game.getBoard();

    final Square fromSquare = toSquare(from);
    final Square toSquare = toSquare(to);

    final Piece fromPiece = board.getPiece(fromSquare);

    if (fromPiece.equals(Piece.NONE)) {
      throw new IllegalMoveException("Piece to move cannot be: " + Piece.NONE);
    }

    if (!board.getSideToMove().equals(fromPiece.getPieceSide())) {
      throw new IncorrectSideException("Side to move is: " + board.getSideToMove());
    }

    final Move move = new Move(fromSquare, toSquare);

    if (board.isMoveLegal(move, true) && isMoveInGeneratedLegalMoves(move, board)) {
      board.doMove(new Move(fromSquare, toSquare));
    } else {
      throw new IllegalMoveException("Illegal move from: " + from + " to: " + to);
    }

    GameResult gameResult = game.getResult();
    if (board.isMated()) {
      gameResult = board.getSideToMove().equals(Side.BLACK) ? GameResult.WHITE_WON : GameResult.BLACK_WON;
    } else if (board.isDraw()) {
      gameResult = GameResult.DRAW;
    }
    game.setResult(gameResult);

    return game;
  }

  private boolean isMoveInGeneratedLegalMoves(Move move, Board board) {
    try {
      final MoveList moves = MoveGenerator.generateLegalMoves(board);
      return moves.stream()
          .anyMatch(move1 -> move.getFrom().equals(move1.getFrom()) && move.getTo().equals(move1.getTo()));
    } catch (MoveGeneratorException e) {
      throw new RuntimeException("Could not generate legal moves", e);
    }
  }

  private Square toSquare(String string) {
    try {
      return Square.fromValue(string.toUpperCase());
    } catch(IllegalArgumentException ex) {
      throw new IllegalMoveException("Square: " + string + " not found");
    }
  }

}
