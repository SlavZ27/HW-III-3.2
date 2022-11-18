-- liquibase formatted sql
-- changeset vzaytsev: 1
create index faculty_name_and_color on faculty (name, color)