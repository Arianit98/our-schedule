package com.arianit.rest.schedule.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "calendars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Calendar extends PanacheEntity {
    private String title;
    private String description;
    @ManyToOne
    private User user;
    @OneToMany
    private List<Appointment> appointments = new ArrayList<>();

    @Override
    public String toString() {
        return "Calendar{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", appointments=" + appointments +
                '}';
    }
}
