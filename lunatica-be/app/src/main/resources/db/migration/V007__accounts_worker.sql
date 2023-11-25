update accounts
set roles = '{ADMIN,WORKER,CLIENT}'::text[]
where email not in ('gero444a@gmail.com', 'bloodyprince92@gmail.com');

update accounts
set roles = '{CLIENT}'::text[]
where email in ('gero444a@gmail.com', 'bloodyprince92@gmail.com');

alter table complaints
    rename column problem_country to problem_country_old;

alter table complaints
    add column problem_country varchar(9) null;

update complaints
    set problem_country = problem_country_old
    where true;

alter table complaints
    drop column problem_country_old;
