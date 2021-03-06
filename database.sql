-- DROP SEQUENCES

DROP SEQUENCE IF EXISTS sq_tb_app CASCADE;
DROP SEQUENCE IF EXISTS sq_tb_discipline CASCADE;
DROP SEQUENCE IF EXISTS sq_tb_lesson CASCADE;
DROP SEQUENCE IF EXISTS sq_tb_educational_content CASCADE;
DROP SEQUENCE IF EXISTS sq_tb_user CASCADE;

-- DROP TABLES

DROP TABLE IF EXISTS tb_app CASCADE;
DROP TABLE IF EXISTS tb_app_feature CASCADE;
DROP TABLE IF EXISTS tb_app_user CASCADE;
DROP TABLE IF EXISTS tb_discipline CASCADE;
DROP TABLE IF EXISTS tb_feature CASCADE;
DROP TABLE IF EXISTS tb_lesson CASCADE;
DROP TABLE IF EXISTS tb_educational_content CASCADE;
DROP TABLE IF EXISTS tb_student CASCADE;
DROP TABLE IF EXISTS tb_teacher CASCADE;
DROP TABLE IF EXISTS tb_user CASCADE;

-- CREATE SEQUENCES

CREATE SEQUENCE sq_tb_app;
CREATE SEQUENCE sq_tb_discipline;
CREATE SEQUENCE sq_tb_lesson;
CREATE SEQUENCE sq_tb_educational_content;
CREATE SEQUENCE sq_tb_user;

-- CREATE TABLES (LOGIC ORDER)

CREATE TABLE tb_user (
	id BIGINT NOT NULL DEFAULT NEXTVAL('sq_tb_user'),
	date_last_login TIMESTAMP NOT NULL,
	date_registration TIMESTAMP NOT NULL,
	email VARCHAR(50) NOT NULL,
	first_name VARCHAR(50) NOT NULL,
	gender VARCHAR(1) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	password VARCHAR(30), -- Nullable because interoperability with Twitter and Facebook
	CONSTRAINT pk_tb_user PRIMARY KEY(id),
	CONSTRAINT uk_tb_user_username UNIQUE (email)
);

CREATE TABLE tb_student (
	id_user BIGINT NOT NULL,
	academic_identifier VARCHAR(50),
	CONSTRAINT pk_tb_student PRIMARY KEY (id_user),
	CONSTRAINT fk_tb_student_tb_user FOREIGN KEY (id_user) REFERENCES tb_user
);

CREATE TABLE tb_teacher (
	id_user BIGINT NOT NULL,
	academic_identifier VARCHAR(50),
	CONSTRAINT pk_tb_teacher PRIMARY KEY (id_user),
	CONSTRAINT fk_tb_teacher_tb_user FOREIGN KEY (id_user) REFERENCES tb_user
);

CREATE TABLE tb_app (
	id BIGINT NOT NULL DEFAULT NEXTVAL('sq_tb_app'),
	date_creation TIMESTAMP NOT NULL,
	name VARCHAR(50) NOT NULL,
	CONSTRAINT pk_tb_app PRIMARY KEY(id)
);

CREATE TABLE tb_app_user (
	id_app BIGINT NOT NULL,
	id_user BIGINT NOT NULL,
	date_request TIMESTAMP NOT NULL,
	active BOOLEAN NOT NULL,
	admin BOOLEAN NOT NULL,	    
	CONSTRAINT pk_tb_app_user PRIMARY KEY (id_app, id_user),
	CONSTRAINT fk_tb_app_user_tb_app FOREIGN KEY (id_app) REFERENCES tb_app,
	CONSTRAINT fk_tb_app_user_tb_user FOREIGN KEY (id_user) REFERENCES tb_user
);

CREATE TABLE tb_feature (
	id BIGINT NOT NULL, -- Primary key not generated by sequence because this table is domain
	abstract BOOLEAN NOT NULL,
	hidden BOOLEAN NOT NULL,
	mandatory BOOLEAN NOT NULL,
	name VARCHAR(50) NOT NULL,
	operator VARCHAR(1),
	id_parent BIGINT,
	CONSTRAINT pk_tb_feature PRIMARY KEY (id),
	CONSTRAINT fk_tb_feature_tb_feature FOREIGN KEY (id_parent) REFERENCES tb_feature
);

CREATE TABLE tb_app_feature (
	id_app BIGINT NOT NULL,
	id_feature BIGINT NOT NULL,
	active BOOLEAN NOT NULL,
	CONSTRAINT pk_tb_app_feature PRIMARY KEY (id_app, id_feature),
	CONSTRAINT fk_tb_app_feature_tb_app FOREIGN KEY (id_app) REFERENCES tb_app,
	CONSTRAINT fk_tb_app_feature_tb_feature FOREIGN KEY (id_feature) REFERENCES tb_feature
);

CREATE TABLE tb_discipline (
	id BIGINT NOT NULL DEFAULT NEXTVAL('sq_tb_discipline'),
	description VARCHAR(1000),
	name VARCHAR(50) NOT NULL,
	id_app BIGINT NOT NULL,
	id_creator BIGINT NOT NULL,
	CONSTRAINT pk_tb_discipline PRIMARY KEY (id),
	CONSTRAINT fk_tb_discipline_tb_app FOREIGN KEY (id_app) REFERENCES tb_app,
	CONSTRAINT fk_tb_discipline_tb_user FOREIGN KEY (id_creator) REFERENCES tb_user
);

CREATE TABLE tb_lesson (
	id BIGINT NOT NULL DEFAULT NEXTVAL('sq_tb_lesson'),
	name VARCHAR(50) NOT NULL,
	id_discipline BIGINT NOT NULL,
	CONSTRAINT pk_tb_lesson PRIMARY KEY (id),
	CONSTRAINT fk_tb_lesson_tb_discipline FOREIGN KEY (id_discipline) REFERENCES tb_discipline
);

CREATE TABLE tb_educational_content (
	id BIGINT NOT NULL DEFAULT NEXTVAL('sq_tb_educational_content'),
	content VARCHAR(500) NOT NULL,
	footnote VARCHAR(50),
	media_type VARCHAR(1) NOT NULL,
	page BIGINT NOT NULL,
	title VARCHAR(50) NOT NULL,
	id_lesson BIGINT NOT NULL,
	CONSTRAINT pk_tb_educational_content PRIMARY KEY (id),
	CONSTRAINT fk_tb_educational_content_tb_lesson FOREIGN KEY (id_lesson) REFERENCES tb_lesson
);
