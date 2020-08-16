create table if not exists Ingredient (
  id varchar(4) not null ,
  name varchar(25) not null ,
  type varchar(20) not null
);

create table if not exists Taco (
    id identity,
    name varchar(50) not null ,
    createdAt timestamp not null
);

create table if not exists Taco_Ingredients (
    taco bigint not null ,
    ingredient varchar(4) not null
);

alter table Taco_Ingredients
    add foreign key (taco) references Taco(id);

alter table Taco_Ingredients
    add foreign key (ingredient) references Ingredient(id);

create table if not exists Taco_Order (
    id identity ,
    name varchar(50) not null,
    street varchar(50) not null,
    city varchar(50) not null,
    province varchar(50) not null,
    zip varchar(50) not null,
    ccNumber varchar(16) not null,
    ccExpiration varchar(10) not null,
    ccCVV varchar(3) not null,
    placedAt timestamp not null
);

create table if not exists Taco_Order_Tacos (
    tacoOrder bigint not null,
    taco bigint not null
);


alter table Taco_Order_Tacos
    add foreign key (tacoOrder) references Taco_Order(id);

alter table Taco_Order_Tacos
    add foreign key (taco) references Taco(id);

-- create table if not exists users(
--   username varchar(50) not null ,
--   password varchar(500) not null ,
--   enabled boolean not null
-- );
--
-- create table if not exists authorities(
--     username varchar(50) not null ,
--     authority varchar(50) not null
-- );
--
-- alter table authorities
--     add foreign key (username) references users(username)
