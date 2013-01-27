# Tournament schema
 
# --- !Ups

CREATE SEQUENCE tournament_id_seq;
CREATE TABLE tournament (
	id integer NOT NULL DEFAULT nextval('tournament_id_seq'),
	desc varchar(255),
	played timestamp
);

# --- !Downs

DROP TABLE tournament;
DROP SEQUENCE tournament_id_seq;
