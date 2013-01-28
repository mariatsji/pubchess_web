# Pelo schema

# --- !Ups

CREATE SEQUENCE pelo_id_seq;
CREATE TABLE pelo (
	id integer NOT NULL DEFAULT nextval('pelo_id_seq'),
	player integer not null,
	battle integer,
	pelo float not null,
);

# --- !Downs

DROP TABLE pelo;
DROP SEQUENCE pelo_id_seq;
