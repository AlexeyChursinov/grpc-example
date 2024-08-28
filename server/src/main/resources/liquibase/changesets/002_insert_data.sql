INSERT INTO regions (id, region_name, region_code)
VALUES (1, 'Санкт-Петербург', 78),
       (2, 'Москва', 77),
       (3, 'Новосибирская область', 54),
       (4, 'Свердловская область', 66);

INSERT INTO users (id, first_name, last_name, email, region_id)
VALUES (1, 'Горшенев', 'Михаил', 'kish@example.com', 1),
       (2, 'Маргулис', 'Евгений', 'margulis@example.com', 2),
       (3, 'Пушной', 'Александр', 'pushnoy@example.com', 3),
       (4, 'Самойлов', 'Глеб', 'gleb@example.com', 4);