package com.example;

import com.blazebit.persistence.CTE;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@CTE
public class ItemCTE {
    @Id
    public int id;

    public String model;

    public String owner;
}
