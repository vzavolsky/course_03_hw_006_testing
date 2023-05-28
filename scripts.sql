SELECT *
FROM students;

SELECT *
FROM students
WHERE age >= 10 AND age <= 14;

SELECT s.name
FROM students as s;

SELECT *
FROM students
WHERE name LIKE '%o%';

SELECT *
FROM students
WHERE age < id;

SELECT *
FROM students
ORDER BY age ASC;