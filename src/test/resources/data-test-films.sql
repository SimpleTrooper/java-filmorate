INSERT INTO  films SELECT * FROM (
SELECT  1, 'Terminator', 'Some science fiction', '1984-10-10', 120, 4 UNION
SELECT  2, 'Test film', 'Some test', '1904-10-10', 198, 1
) x WHERE NOT EXISTS (SELECT * FROM films);

