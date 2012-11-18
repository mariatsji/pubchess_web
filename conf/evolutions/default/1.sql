# Tasks schema
 
# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    label varchar(255)
);

CREATE SEQUENCE player_id_seq;
CREATE TABLE player (
    id integer NOT NULL DEFAULT nextval('player_id_seq'),
    name varchar(255),
    elo float
);
 
# --- !Downs
 
DROP TABLE task;
DROP SEQUENCE task_id_seq;
DROP TABLE player;
DROP SEQUENCE player_id_seq;
