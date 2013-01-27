# Player schema
 
# --- !Ups

CREATE SEQUENCE player_id_seq;
CREATE TABLE player (
    id integer NOT NULL DEFAULT nextval('player_id_seq'),
    name varchar(255),
    elo float
);
 
# --- !Downs

DROP TABLE player;
DROP SEQUENCE player_id_seq;
