create table if not exists complaints
(
    id              uuid        not null primary key,
    state           varchar(20) not null,
    problem_country varchar(2)  null,
    problem_date    date        null,
    type            varchar(20) null,
    contact_details jsonb       not null,
    created_at      timestamp   not null,
    updated_at      timestamp   not null
);
