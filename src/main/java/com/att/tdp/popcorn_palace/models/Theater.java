package com.att.tdp.popcorn_palace.models;

import jakarta.persistence.*;

@Entity
@Table(name = "theater")
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Theater(String name) {
        this.name = name;
    }

    public Theater() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
