select student.name, student.age, faculty.name
from student
         left join faculty on faculty.id = student.faculty_id;

select student.* from student inner join avatar a on student.id = a.student_id