package nl.rabbink.simplechess.rest.dto;

import javax.validation.constraints.NotBlank;

public class MoveDTO {

  @NotBlank(message = "Please provide a from position")
  private String from;

  @NotBlank(message = "Please provide a to position")
  private String to;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }
}
