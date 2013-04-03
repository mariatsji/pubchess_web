# Battle schema

# --- !Ups

CREATE SEQUENCE battle_id_seq;
CREATE TABLE battle (
	id integer NOT NULL DEFAULT nextval('battle_id_seq'),
	white integer not null,
	black integer not null,
	result integer default -1,
	whitebeers integer default -1,
	blackbeers integer default -1,
	tournament integer not null,
);

# --- !Downs

DROP TABLE battle;
DROP SEQUENCE battle_id_seq;
