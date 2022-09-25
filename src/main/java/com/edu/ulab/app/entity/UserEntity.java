package com.edu.ulab.app.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@ToString(exclude = "bookList")
@EqualsAndHashCode(exclude = "bookList")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity implements BaseEntity {

    private Long id;
    private String fullName;
    private String title;
    private int age;

    @Builder.Default
    private List<BookEntity> bookList = new ArrayList<>();
}
