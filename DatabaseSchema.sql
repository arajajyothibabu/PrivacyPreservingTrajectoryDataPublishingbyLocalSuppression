CREATE TABLE rawdatatable (
  id NUMBER(10) NOT NULL PRIMARY KEY REFERENCES sensitivedata(id),
  path VARCHAR2(100),
  diagnosis VARCHAR2(30)
);

CREATE TABLE trajectorydata (
  id NUMBER(10) NOT NULL,
  location VARCHAR2(10) NOT NULL,
  time VARCHAR2(10) NOT NULL
);

CREATE TABLE sensitivedata (
  id NUMBER(10) NOT NULL PRIMARY KEY ,
  diagnosis VARCHAR2(30)
);

INSERT INTO trajectorydata VALUES('1', 'a', '1');
INSERT INTO trajectorydata VALUES('1', 'd', '2');
INSERT INTO trajectorydata VALUES('1', 'b', '3');
INSERT INTO trajectorydata VALUES('1', 'e', '4');
INSERT INTO trajectorydata VALUES('1', 'f', '6');
INSERT INTO trajectorydata VALUES('1', 'e', '8');
INSERT INTO trajectorydata VALUES('2', 'd', '2');
INSERT INTO trajectorydata VALUES('2', 'c', '5');
INSERT INTO trajectorydata VALUES('2', 'f', '6');
INSERT INTO trajectorydata VALUES('2', 'c', '7');
INSERT INTO trajectorydata VALUES('2', 'e', '8');
INSERT INTO trajectorydata VALUES('3', 'b', '3');
INSERT INTO trajectorydata VALUES('3', 'c', '7');
INSERT INTO trajectorydata VALUES('3', 'e', '8');
INSERT INTO trajectorydata VALUES('4', 'b', '3');
INSERT INTO trajectorydata VALUES('4', 'e', '4');
INSERT INTO trajectorydata VALUES('4', 'f', '6');
INSERT INTO trajectorydata VALUES('4', 'e', '8');
INSERT INTO trajectorydata VALUES('5', 'e', '1');
INSERT INTO trajectorydata VALUES('5', 'b', '2');
INSERT INTO trajectorydata VALUES('5', 'e', '5');
INSERT INTO trajectorydata VALUES('5', 'f', '6');
INSERT INTO trajectorydata VALUES('5', 'e', '7');
INSERT INTO trajectorydata VALUES('6', 'c', '5');
INSERT INTO trajectorydata VALUES('6', 'f', '6');
INSERT INTO trajectorydata VALUES('6', 'e', '9');
INSERT INTO trajectorydata VALUES('7', 'f', '6');
INSERT INTO trajectorydata VALUES('7', 'c', '7');
INSERT INTO trajectorydata VALUES('7', 'e', '8');
INSERT INTO trajectorydata VALUES('8', 'a', '1');
INSERT INTO trajectorydata VALUES('8', 'd', '2');
INSERT INTO trajectorydata VALUES('8', 'f', '6');
INSERT INTO trajectorydata VALUES('8', 'c', '7');
INSERT INTO trajectorydata VALUES('8', 'e', '9');

INSERT INTO sensitivedata VALUES('1', 'HIV');
INSERT INTO sensitivedata VALUES('2', 'FEVER');
INSERT INTO sensitivedata VALUES('3', 'Hepatitis');
INSERT INTO sensitivedata VALUES('4', 'Flu');
INSERT INTO sensitivedata VALUES('5', 'HIV');
INSERT INTO sensitivedata VALUES('6', 'Hepatitis');
INSERT INTO sensitivedata VALUES('7', 'FEVER');
INSERT INTO sensitivedata VALUES('8', 'Flu');

COMMIT;