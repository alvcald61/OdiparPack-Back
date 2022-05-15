package com.pucp.odiparpackback.model;

import com.pucp.odiparpackback.model.Person;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "administrator_id")
public class Administrator extends Person {
}