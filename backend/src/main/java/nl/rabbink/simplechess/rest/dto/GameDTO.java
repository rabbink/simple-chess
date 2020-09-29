package nl.rabbink.simplechess.rest.dto;

import javax.validation.constraints.NotBlank;

public class GameDTO {

  @NotBlank
  private String gameId;

  private BoardDTO board;

  private String result;

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public BoardDTO getBoard() {
    return board;
  }

  public void setBoard(BoardDTO board) {
    this.board = board;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }
}
