package nl.rabbink.simplechess.rest.dto;

public class BoardDTO {

  private String fen;

  private String sideToMove;

  public String getFen() {
    return fen;
  }

  public void setFen(String fen) {
    this.fen = fen;
  }

  public String getSideToMove() {
    return sideToMove;
  }

  public void setSideToMove(String sideToMove) {
    this.sideToMove = sideToMove;
  }
}
