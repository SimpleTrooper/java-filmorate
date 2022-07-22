# java-filmorate

![ER диаграмма для Filmorate](/Filmorate_ER.png) 

Таблицы:  
**films** - таблица, хранящая фильмы  

  - film_id        - идентификатор фильма, первичный ключ  
  - name           - название  
  - description    - описание  
  - release_date   - дата выхода  
  - duration       - продолжительность в минутах  
  - mpa_rating_id  - внешний ключ, ссылка на таблицу mpa_rating, рейтинг фильма  
 
 **mpa_ratings** - таблица, хранящая рейтинги mpa для фильмов  
 
  - mpa_rating_id - идентификатор, первичный ключ  
  - name          - наименование рейтинга  
  
  **genres** - таблица, хранящая жанры фильмов  
  
   - genre_id - идентификатор, первичный ключ  
   - name     - название  
   
  **film_genre** - таблица для связи многие ко многим фильмов и жанров  
  
   - (film_id, genre_id) - составной первичный ключ
   - film_id             - внешний ключ, ссылка на таблицу фильмов  
   - genre_id            - внешний ключ, ссылка на таблицу жанров  
   
   **users** - таблица, хранящая пользователей  
   
   - user_id  - идентификатор, первичный ключ  
   - email    - электронная почта  
   - login    - логин  
   - name     - имя пользователя  
   - birthday - дата рождения  
    
    
   **likes** - таблица, хранящая лайки пользователей фильмам  
   
   - (film_id, user_id) - составной первичный ключ 
   - film_id            - внешний ключ, ссылка на таблицу фильмов  
   - user_id            - внешний ключ, ссылка на таблицу пользователей  
    
   **friendship** - таблица со статусами дружбы между пользователями  
   
   - (user_id, friend_id) - составной первичный ключ
   - user_id              - внешний ключ, пользователь, запросивший дружбу  
   - friend_id            - внешний ключ, пользователь, к которому запросили дружбу  
   - status               - статус дружбы (подтверждено/не подтверждено)  
   
   
   Примеры запросов:  
   1) Вывести все фильмы с рейтингом PG  
  
              SELECT *   
              FROM films AS f  
              INNER JOIN mpa_ratings AS mpa_r ON mpa_r.mpa_rating_id=f.mpa_rating_id   
              WHERE mpa_r.name='PG'    
          
   2) Вывести топ-10 фильмов по лайкам:
   
           SELECT *
           FROM films as f
           LEFT JOIN likes as ful ON f.film_id=ful.film_id
           GROUP BY f.film_id
           ORDER BY COUNT(f.film_id) DESC
           LIMIT 10;
           
   3) Вывести общих друзей пользователей с id=1 и id=2  
  
  
          SELECT f.friend_id AS mutual_friend  
          FROM friendship f  
          WHERE f.user_id=1 AND f.status=true  
	            AND f.friend_id IN (SELECT f.friend_id  
					          FROM friendship as f  
                                    WHERE f.user_id=2 AND f.status=true);
     
