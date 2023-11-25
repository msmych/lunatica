alter table accounts
    add column roles text[] not null default '{}';

insert into accounts
values (gen_random_uuid(), 'gero444a@gmail.com', '0ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
        5312492652, now(), now(), '{ADMIN}');
