create table citizen
(
  id        bigint not null constraint citizen_pkey primary key,
  address   varchar(200),
  dob       date,
  dulnumber varchar(12) constraint uk_aa5e3hj9n0iy79nqeax5q3uqd unique,
  full_name varchar(100)
);
