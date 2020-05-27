package hu.inf.unideb.oldgold.results;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Játékállást reprezentáló osztály.
 */
@Entity
@Table(name = "game_result")
@Getter
@Setter
@ToString
public class GameResult {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name_of_player")
    private String nameOfPlayer;

    private int score;

}
