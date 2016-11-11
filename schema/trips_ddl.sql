CREATE TABLE trips(
egn                 varchar(10) REFERENCES people(egn),
arrival_date        date        NOT NULL,
departure_date      date        NOT NULL,
city                varchar(20) NOT NULL
 )