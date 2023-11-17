create table if not exists accounts
(
    id         uuid         not null primary key,
    email      varchar(200) null,
    pass_hash  varchar(200) null,
    tg_chat_id bigint       null,
    created_at timestamp    not null,
    updated_at timestamp    not null
);
