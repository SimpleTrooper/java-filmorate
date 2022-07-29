CREATE TABLE IF NOT EXISTS films (
  film_id bigint PRIMARY KEY AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  description varchar(200),
  release_date datetime,
  duration int,
  mpa_rating_id bigint
);

CREATE TABLE IF NOT EXISTS genres (
  genre_id bigint PRIMARY KEY AUTO_INCREMENT,
  name varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id bigint,
  genre_id bigint,
  PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS mpa_ratings (
  mpa_rating_id bigint PRIMARY KEY AUTO_INCREMENT,
  name varchar(10) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS likes (
  film_id bigint,
  user_id bigint,
  PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS users (
  user_id bigint PRIMARY KEY AUTO_INCREMENT,
  email varchar(100) UNIQUE NOT NULL,
  login varchar(40) UNIQUE NOT NULL,
  name varchar(100),
  birthday datetime
);

CREATE TABLE IF NOT EXISTS friendship (
  user_id bigint,
  friend_id bigint,
  status boolean,
  PRIMARY KEY (user_id, friend_id)
);

COMMENT ON COLUMN films.release_date IS 'In past and later than 28.12.1895';

COMMENT ON COLUMN users.birthday IS 'In past';

ALTER TABLE films ADD FOREIGN KEY (mpa_rating_id) REFERENCES mpa_ratings (mpa_rating_id);

ALTER TABLE film_genre ADD FOREIGN KEY (film_id) REFERENCES films (film_id);

ALTER TABLE film_genre ADD FOREIGN KEY (genre_id) REFERENCES genres (genre_id);

ALTER TABLE likes ADD FOREIGN KEY (film_id) REFERENCES films (film_id);

ALTER TABLE likes ADD FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE friendship ADD FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE friendship ADD FOREIGN KEY (friend_id) REFERENCES users (user_id);
