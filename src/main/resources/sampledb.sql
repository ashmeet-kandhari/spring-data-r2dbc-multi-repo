
CREATE SCHEMA IF NOT EXISTS testSchema
   AUTHORIZATION postgres;


CREATE TABLE IF NOT EXISTS testSchema.person
(
	person_id uuid NOT NULL,
	email_id character varying(255) NOT NULL,
	first_name character varying(255) NOT NULL,
	last_name character varying(255) NOT NULL,
	created_timestamp timestamp without time zone NOT NULL,
	CONSTRAINT person_person_id PRIMARY KEY (person_id),
  CONSTRAINT person_email_id UNIQUE (email_id)
);

CREATE TABLE IF NOT EXISTS testSchema.address
(
	id UUID NOT NULL,
	person_id uuid NOT NULL,
  state character varying(255) NOT NULL,
  city character varying(255) NOT NULL,
  created_timestamp timestamp without time zone  NOT NULL,
  CONSTRAINT address_id PRIMARY KEY (id),
  CONSTRAINT address_person_id_fkey FOREIGN KEY (person_id)
      REFERENCES testSchema.person (person_id) MATCH SIMPLE
      ON UPDATE NO ACTION
      ON DELETE NO ACTION
);

CREATE INDEX IF NOT EXISTS address_person_id
    ON testSchema.address USING btree
    (person_id ASC NULLS LAST)
    TABLESPACE pg_default;

