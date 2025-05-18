package com.seaexplorer.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "visited_positions")
public class VisitedPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private int y;

    @Enumerated(EnumType.STRING)
    private Direction direction;

}
