package com.edu.ulab.app.entity;

import lombok.*;

@Builder
@Data
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
