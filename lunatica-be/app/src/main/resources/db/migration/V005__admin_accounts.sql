update accounts
set roles = array_append(roles, 'BASIC')
where email = 'gero444a@gmail.com';

insert into accounts
values (gen_random_uuid(), 'realsmych@gmail.com', '0ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
        185244864, now(), now(), '{BASIC,ADMIN}'),
       (gen_random_uuid(), 'bloodyprince92@gmail.com',
        '0ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
        115971407, now(), now(), '{BASIC,ADMIN}'),
       (gen_random_uuid(), 'gcreego@gmail.com',
        '0ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
        null, now(), now(), '{BASIC,ADMIN}');
