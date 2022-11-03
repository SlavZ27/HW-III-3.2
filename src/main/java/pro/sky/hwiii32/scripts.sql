select * from student where age>10 and age<20;
select student.name from student;
select * from student where name like '%o%';
select * from student where id>student.age;
select * from student order by age;
select * from Student as st ,Faculty as fa where st.faculty_id=fa.id and fa.id=15