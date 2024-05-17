create sequence address_seq start with 1 increment by 1;

create table address
(
    id     bigserial not null primary key,
    street varchar(50) unique
);

alter table if exists client
add address_id bigint not null unique;

alter table if exists client
add foreign key (address_id) references address (id);

create sequence phone_seq start with 1 increment by 1;

create table phone
(
    id     bigserial not null primary key,
    client_id bigint not null,
    number varchar(50) unique,
    foreign key (client_id) references client (id)
);
