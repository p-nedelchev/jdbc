CREATE TABLE trips
(egn varchar(10) REFERENCES people(egn),
 arrival_date date,
 departure_date date,
 city varchar(20)
 )