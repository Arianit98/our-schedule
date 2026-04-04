package com.arianit.rest.schedule.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends PanacheEntity {
    public String name;
    public String surname;
    public String username;
    public String email;
    public String subjectId;
    public LocalDate birthday;
    public String image;
    @OneToMany
    public List<Appointment> appointments;
}
