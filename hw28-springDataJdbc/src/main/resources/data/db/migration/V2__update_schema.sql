create table address
(
    id     bigserial not null primary key,
    street varchar(50) unique,
    client_id bigint not null references client (id)
);

create table phone
(
    id     bigserial not null primary key,
    client_id bigint not null,
    number varchar(50) unique,
    foreign key (client_id) references client (id)
);
