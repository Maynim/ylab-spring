package com.edu.ulab.app.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@ToString(exclude = "bookList")
@EqualsAndHashCode(exclude = "bookList")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Person implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String title;
    private int age;

    @Builder.Default
    @OneToMany(mappedBy = "person")
    private List<Book> bookList = new ArrayList<>();
}
