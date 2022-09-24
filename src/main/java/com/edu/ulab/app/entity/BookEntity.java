package com.edu.ulab.app.entity;

import lombok.*;

@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookEntity implements BaseEntity {

    private Long id;
    private Long userId;
    private String title;
    private String author;
    private long pageCount;
}
