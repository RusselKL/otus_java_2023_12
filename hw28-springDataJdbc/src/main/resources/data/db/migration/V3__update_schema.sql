alter table if exists client
add password varchar(50) unique;

create table role
(
    id     bigserial not null primary key,
    client_id bigint not null,
    role varchar(50),
    foreign key (client_id) references client (id)
);
