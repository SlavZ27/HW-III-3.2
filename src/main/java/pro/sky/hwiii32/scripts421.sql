ALTER table student
    add constraint age_constraint check ( age > 15 );

alter table student
    alter column name set not null;
alter table student
    add constraint name_unique unique (name);

alter table faculty
    add constraint unique_name_color unique (name, color);

ALTER table student
    alter column age set default 20;


