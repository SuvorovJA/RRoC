create table citizen
(
  id        bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  address   varchar(200),
  dob       date,
  dulnumber varchar(12) constraint uk_aa5e3hj9n0iy79nqeax5q3uqd unique,
  full_name varchar(100)
);
ALTER SEQUENCE citizen_id_seq RESTART WITH 101;