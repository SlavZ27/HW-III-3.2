create table car_brands
(
    id        serial PRIMARY KEY,
    car_brand text
);

create table car_models
(
    id        serial PRIMARY KEY,
    car_model text
);

create table car_model_brand
(
    id           serial PRIMARY KEY,
    car_model_id serial references car_models (id),
    car_brand_id serial references car_brands (id)
);

create table cars
(
    id                 serial PRIMARY KEY,
    car_model_brand_id serial REFERENCES car_model_brand (id),
    cost               money
);


create table person_names
(
    id   serial PRIMARY KEY,
    name text
);

create table persons
(
    id                   serial PRIMARY KEY,
    person_name_id       serial REFERENCES person_names (id),
    age                  smallint,
    driver_licence_exist boolean
);

create table car_owning
(
    id        serial PRIMARY KEY,
    person_id serial REFERENCES persons (id),
    car_id    serial references cars (id)
)