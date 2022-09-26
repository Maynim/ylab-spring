insert into person (id, full_name, title, age)
values (1001, 'default user', 'reader', 55);

insert into book (id, title, author, page_count, person_id)
values (2002, 'default book', 'author', 5500, 1001);

insert into book (id, title, author, page_count, person_id)
values (3003, 'more default book', 'on more author', 6655, 1001);

