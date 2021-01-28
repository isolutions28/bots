package ua.isolutions.bot.commons.command.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "commands")
@Data
public class CommandEntity {

    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String label;
    @Column(unique = true)
    private String command;
    private int position;
    private boolean enabled;
    private boolean topCommand;
}
