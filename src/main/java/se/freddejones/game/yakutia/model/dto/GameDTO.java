package se.freddejones.game.yakutia.model.dto;

public class GameDTO {

    private Long id;
    private String name;
    private String status;
    private String date;
    private boolean canStartGame;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCanStartGame() {
        return canStartGame;
    }

    public void setCanStartGame(boolean canStartGame) {
        this.canStartGame = canStartGame;
    }
}
