# Elo schema

# --- !Ups

CREATE SEQUENCE elo_id_seq;
CREATE TABLE elo (
	id integer NOT NULL DEFAULT nextval('elo_id_seq'),
	player integer not null,
	battle integer,
	elo float not null,
);

# --- !Downs

DROP TABLE elo;
DROP SEQUENCE elo_id_seq;
