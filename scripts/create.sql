/**
 * Created by catop on 3/11/15.
 */

DROP DATABASE IF EXISTS superheroes;
CREATE DATABASE superheroes;

USE superheroes;

/* entity tables  */

CREATE TABLE `character` (
  id INT NOT NULL,
  name VARCHAR(128) NOT NULL,
  description VARCHAR(2048),
  modified DATETIME,
  thumbnail_path VARCHAR(256),
  thumbnail_extension VARCHAR(4),
  resource_uri VARCHAR(256),
  updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  popularity INT NOT NULL DEFAULT 0,
  PRIMARY KEY(id),
  INDEX(popularity)
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE comic (
  id INT NOT NULL,
  digital_id BIGINT,
  title VARCHAR(128) NOT NULL,
  issue_number INT,
  description VARCHAR(2048),
  variant_description VARCHAR(512),
  modified DATETIME,
  isbn VARCHAR(64),
  upc VARCHAR(64),
  diamond_code VARCHAR(64),
  ean VARCHAR(64),
  issn VARCHAR(64),
  format VARCHAR(32),
  page_count INT,
  resource_uri VARCHAR(256),
  on_sale_date DATETIME,
  foc_date DATETIME,
  print_price DOUBLE,
  thumbnail_path VARCHAR(256),
  thumbnail_extension VARCHAR(8),
  updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  popularity INT NOT NULL DEFAULT 0,
  PRIMARY KEY(id),
  INDEX(popularity)
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE text_object (
  id INT NOT NULL AUTO_INCREMENT,
  comic_id_fk INT NOT NULL,
  type VARCHAR(32),
  language VARCHAR(8),
  text VARCHAR(64),
  PRIMARY KEY(id),
  FOREIGN KEY (comic_id_fk) REFERENCES comic(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE creator (
  id INT NOT NULL,
  first_name VARCHAR(64),
  middle_name VARCHAR(64),
  last_name VARCHAR(64),
  full_name VARCHAR(128),
  suffix VARCHAR(8),
  modified DATETIME,
  thumbnail_path VARCHAR(256),
  thumbnail_extension VARCHAR(8),
  resource_uri VARCHAR(256),
  updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  popularity INT NOT NULL DEFAULT 0,
  PRIMARY KEY(id),
  INDEX(popularity)
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE event (
  id INT NOT NULL,
  title VARCHAR(128) NOT NULL,
  description VARCHAR(2048),
  resource_uri VARCHAR(256),
  modified DATETIME,
  start DATETIME,
  end DATETIME,
  thumbnail_path VARCHAR(256),
  thumbnail_extension VARCHAR(8),
  next_id INT,
  previous_id INT,
  updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  popularity INT NOT NULL DEFAULT 0,
  PRIMARY KEY(id),
  INDEX(popularity)
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE series (
  id INT NOT NULL,
  title VARCHAR(128) NOT NULL,
  description VARCHAR(2048),
  resource_uri VARCHAR(256),
  start_year DATE,
  end_year DATE,
  rating VARCHAR(64),
  type VARCHAR(32),
  modified DATETIME,
  thumbnail_path VARCHAR(256),
  thumbnail_extension VARCHAR(8),
  next_id INT,
  previous_id INT,
  updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  popularity INT NOT NULL DEFAULT 0,
  PRIMARY KEY(id),
  INDEX(popularity)
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE story (
  id INT NOT NULL,
  title VARCHAR(128) NOT NULL,
  description VARCHAR(2048),
  resource_uri VARCHAR(256),
  type VARCHAR(32),
  thumbnail VARCHAR(256),
  original_issue_id INT,
  modified DATETIME,
  updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  popularity INT NOT NULL DEFAULT 0,
  PRIMARY KEY(id),
  INDEX(popularity)
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE url (
  id INT NOT NULL AUTO_INCREMENT,
  type VARCHAR(32),
  url VARCHAR(256),
  PRIMARY KEY(id)
) ENGINE=InnoDB CHARACTER SET utf8;

/* Populate this table with your public and private marvel keys */

CREATE TABLE `key` (
  type VARCHAR(16) NOT NULL,
  value VARCHAR(64) NOT NULL,
  PRIMARY KEY (type)
) ENGINE=InnoDB CHARACTER SET utf8;

/* junction tables */

CREATE TABLE character_comic_junction (
  character_id INT NOT NULL,
  comic_id INT NOT NULL,
  FOREIGN KEY (character_id) REFERENCES `character`(id) ON DELETE NO ACTION,
  FOREIGN KEY (comic_id) REFERENCES comic(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE character_series_junction (
  character_id INT NOT NULL,
  series_id INT NOT NULL,
  FOREIGN KEY (character_id) REFERENCES `character`(id) ON DELETE NO ACTION,
  FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE character_story_junction (
  character_id INT NOT NULL,
  story_id INT NOT NULL,
  FOREIGN KEY(character_id) REFERENCES `character`(id) ON DELETE NO ACTION,
  FOREIGN KEY(story_id) REFERENCES story(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE character_event_junction (
  character_id INT NOT NULL,
  event_id INT NOT NULL,
  FOREIGN KEY(character_id) REFERENCES `character`(id) ON DELETE NO ACTION,
  FOREIGN KEY(event_id) REFERENCES event(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE character_url_junction (
  character_id INT NOT NULL,
  url_id INT NOT NULL,
  FOREIGN KEY(character_id) REFERENCES `character`(id) ON DELETE NO ACTION,
  FOREIGN KEY(url_id) REFERENCES url(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE comic_series_junction (
  comic_id INT NOT NULL,
  series_id INT NOT NULL,
  FOREIGN KEY (comic_id) REFERENCES comic(id) ON DELETE NO ACTION,
  FOREIGN KEY(series_id) REFERENCES series(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE comic_creator_junction (
  comic_id INT NOT NULL,
  creator_id INT NOT NULL,
  FOREIGN KEY (comic_id) REFERENCES comic(id) ON DELETE NO ACTION,
  FOREIGN KEY (creator_id) REFERENCES creator(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE comic_story_junction (
  comic_id INT NOT NULL,
  story_id INT NOT NULL,
  FOREIGN KEY (comic_id) REFERENCES comic(id) ON DELETE NO ACTION,
  FOREIGN KEY (story_id) REFERENCES story(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE comic_event_junction (
  comic_id INT NOT NULL,
  event_id INT NOT NULL,
  FOREIGN KEY (comic_id) REFERENCES comic(id) ON DELETE NO ACTION,
  FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE creator_series_junction (
  creator_id INT NOT NULL,
  series_id INT NOT NULL,
  FOREIGN KEY (creator_id) REFERENCES creator(id) ON DELETE NO ACTION,
  FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE creator_story_junction (
  creator_id INT NOT NULL,
  story_id INT NOT NULL,
  FOREIGN KEY (creator_id) REFERENCES creator(id) ON DELETE NO ACTION,
  FOREIGN KEY (story_id) REFERENCES story(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE creator_url_junction (
  creator_id INT NOT NULL,
  url_id INT NOT NULL,
  FOREIGN KEY (creator_id) REFERENCES creator(id) ON DELETE NO ACTION,
  FOREIGN KEY (url_id) REFERENCES url(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE event_url_junction (
  event_id INT NOT NULL,
  url_id INT NOT NULL,
  FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE NO ACTION,
  FOREIGN KEY (url_id) REFERENCES url(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE event_creator_junction (
  event_id INT NOT NULL,
  creator_id INT NOT NULL,
  FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE NO ACTION,
  FOREIGN KEY (creator_id) REFERENCES creator(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE event_story_junction (
  event_id INT NOT NULL,
  story_id INT NOT NULL,
  FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE NO ACTION,
  FOREIGN KEY (story_id) REFERENCES story(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE event_series_junction (
  event_id INT NOT NULL,
  series_id INT NOT NULL,
  FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE NO ACTION,
  FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE series_url_junction (
  series_id INT NOT NULL,
  url_id INT NOT NULL,
  FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE NO ACTION,
  FOREIGN KEY (url_id) REFERENCES url(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;

CREATE TABLE series_story_junction (
  series_id INT NOT NULL,
  story_id INT NOT NULL,
  FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE NO ACTION,
  FOREIGN KEY (story_id) REFERENCES story(id) ON DELETE NO ACTION
) ENGINE=InnoDB CHARACTER SET utf8;
