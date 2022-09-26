package com.edu.ulab.app.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@ToString(exclude = "person")
@EqualsAndHashCode(exclude = "person")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Book implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    private String title;
    private String author;
    private long pageCount;
}
