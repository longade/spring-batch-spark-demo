DROP TABLE people IF EXISTS;

CREATE TABLE people (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);

insert into people (person_id, first_name, last_name) values (1, 'Mozelle', 'Humpage');
insert into people (person_id, first_name, last_name) values (2, 'Elka', 'Broggio');
insert into people (person_id, first_name, last_name) values (3, 'Margret', 'Fiennes');
insert into people (person_id, first_name, last_name) values (4, 'Cullan', 'Capes');
insert into people (person_id, first_name, last_name) values (5, 'Arnoldo', 'Kirwood');
insert into people (person_id, first_name, last_name) values (6, 'Elvina', 'Slyne');
insert into people (person_id, first_name, last_name) values (7, 'Lorelle', 'Wiltshear');
insert into people (person_id, first_name, last_name) values (8, 'Murdock', 'Stollwerck');
insert into people (person_id, first_name, last_name) values (9, 'Daphne', 'Gerring');
insert into people (person_id, first_name, last_name) values (10, 'Ronica', 'Cosgreave');

-- DROP TABLE cars IF EXISTS;
--
-- CREATE TABLE cars (
--     car_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
--     brand VARCHAR(20),
--     model VARCHAR(20),
--     person_id BIGINT NOT NULL,
--     foreign key (person_id) references people(person_id)
-- );
--
-- insert into cars (car_id, brand, model, person_id) values (1, 'Buick', 'Park Avenue', 1);
-- insert into cars (car_id, brand, model, person_id) values (2, 'Volvo', 'V40', 1);
-- insert into cars (car_id, brand, model, person_id) values (3, 'Dodge', 'Ram 3500', 3);
-- insert into cars (car_id, brand, model, person_id) values (4, 'Ford', 'Ranger', 5);
-- insert into cars (car_id, brand, model, person_id) values (5, 'Ford', 'Escape', 4);
