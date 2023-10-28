create table if not exists messages
(
    id           uuid      not null primary key,
    complaint_id uuid      null,
    content      text      not null,
    created_at   timestamp not null,
    updated_at   timestamp not null
)
