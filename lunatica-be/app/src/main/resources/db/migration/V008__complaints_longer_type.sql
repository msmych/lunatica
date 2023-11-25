alter table complaints
    rename column type to type_old;

alter table complaints
    add column type varchar(255) null;

update complaints
set type = type_old
where true;

alter table complaints
    drop column type_old;
