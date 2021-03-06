package ch.heigvd.res.labs.roulette.net.protocol;

public class ByeCommandResponse {


  private String status;
  private int numberOfCommands;

  public ByeCommandResponse(String status, int numberOfCommands) {
    this.status = status;
    this.numberOfCommands = numberOfCommands;
  }


  public void setStatus(String status) {
    this.status = status;
  }

  public void setNumberOfCommands(int numberOfCommands) {
    this.numberOfCommands = numberOfCommands;
  }

  public String getStatus() {

    return status;
  }

  public int getNumberOfCommands() {
    return numberOfCommands;
  }
}
