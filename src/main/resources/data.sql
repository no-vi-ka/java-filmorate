MERGE INTO mpa_rating (id, name)
VALUES (1,'G'),
       (2,'PG'),
       (3,'PG-13'),
       (4,'R'),
       (5,'NC-17');

MERGE INTO genres (id, name)
VALUES (1,'Комедия'),
       (2,'Драма'),
       (3,'Мультфильм'),
       (4,'Триллер'),
       (5,'Документальный'),
       (6,'Боевик');


------MERGE INTO mpa_rating (id, name)
------VALUES (1,'G'),
------       (2,'PG'),
------       (3,'PG-13'),
------       (4,'R'),
------       (5,'NC-17');
------
------MERGE INTO genres (id, name)
------VALUES (1,'Комедия'),
------       (2,'Драма'),
------       (3,'Мультфильм'),
------       (4,'Триллер'),
------       (5,'Документальный'),
------       (6,'Боевик');
----MERGE INTO genres (ID, NAME) VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'), (6, 'Боевик');
----
----MERGE INTO mpa_rating (ID, NAME) VALUES (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');
--insert into GENRES (ID, NAME) VALUES (1, 'Комедия');
--insert into GENRES (ID, NAME) VALUES (2, 'Драма');
--insert into GENRES (ID, NAME) VALUES (3, 'Мультфильм');
--insert into GENRES (ID, NAME) VALUES (4, 'Триллер');
--insert into GENRES (ID, NAME) VALUES (5, 'Документальный');
--insert into GENRES (ID, NAME) VALUES (6, 'Боевик');
--
--INSERT INTO mpa_rating (ID, NAME) VALUES (1, 'G');
--INSERT INTO mpa_rating (ID, NAME) VALUES (2, 'PG');
--INSERT INTO mpa_rating (ID, NAME) VALUES (3, 'PG-13');
--INSERT INTO mpa_rating (ID, NAME) VALUES (4, 'R');
--INSERT INTO mpa_rating (ID, NAME) VALUES (5, 'NC-17');
