package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Item {

    @Id
    public int id;

    public String model;

    public String owner;
}
