alter table if exists client
add password varchar(50) unique;

create sequence role_seq start with 1 increment by 1;

create table role
(
    id     bigserial not null primary key,
    client_id bigint not null,
    role varchar(50),
    foreign key (client_id) references client (id)
);
