package se.freddejones.game.yakutia.model.dto;

public class InvitedPlayer {

    private Long id;
    private String name;        // TODO remove? used for what?

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
