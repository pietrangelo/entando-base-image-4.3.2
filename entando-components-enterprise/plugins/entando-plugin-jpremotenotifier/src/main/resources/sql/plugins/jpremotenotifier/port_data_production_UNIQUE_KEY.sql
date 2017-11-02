-- PostgreSQL
/*
CREATE SEQUENCE uniquekey
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 3000
  CACHE 1;
ALTER TABLE uniquekey OWNER TO agile;
*/

-- MySQL

/*

DROP TABLE IF EXISTS sequence;
CREATE TABLE sequence (
name VARCHAR(50) NOT NULL,
current_value INT NOT NULL,
increment INT NOT NULL DEFAULT 1,
PRIMARY KEY (name)
) ENGINE=InnoDB;

INSERT INTO sequence VALUES ('uniquekey',(SELECT keyvalue FROM uniquekeys),1);

DROP FUNCTION IF EXISTS currval;
DELIMITER $
CREATE FUNCTION currval (seq_name VARCHAR(50))
RETURNS INTEGER
CONTAINS SQL
BEGIN
  DECLARE value INTEGER;
  SET value = 0;
  SELECT current_value INTO value
  FROM sequence
  WHERE name = seq_name;
  RETURN value;
END$
DELIMITER ;

DROP FUNCTION IF EXISTS nextval;
DELIMITER $
CREATE FUNCTION nextval (seq_name VARCHAR(50))
RETURNS INTEGER
CONTAINS SQL
BEGIN
   UPDATE sequence
   SET          current_value = current_value + increment
   WHERE name = seq_name;
   RETURN currval(seq_name);
END$
DELIMITER ;

*/
