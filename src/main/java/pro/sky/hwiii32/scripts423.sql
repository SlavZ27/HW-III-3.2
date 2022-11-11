select student.name, student.age, faculty.name
from student
         inner join faculty on faculty.id = student.faculty_id;

select student.* from student right join avatar a on student.id = a.student_id