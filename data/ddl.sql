-- Author: Kazi Tanvir Azad
-- Some of the data types may be specific to PostgreSQL

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS EXPENSE_RECORDER;

CREATE TYPE EXPENSE_RECORDER.monthname as enum
('JANUARY','FEBRUARY','MARCH','APRIL','MAY','JUNE','JULY','AUGUST','SEPTEMBER','OCTOBER','NOVEMBER','DECEMBER','NOT_SPECIFIED');
CREATE CAST (varchar(15) AS EXPENSE_RECORDER.monthname) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS EXPENSE_RECORDER.user (
    userid BIGSERIAL,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50),
    email VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20) UNIQUE,
    hashedpassword VARCHAR(150) NOT NULL,
    isloggedout BOOL,
    datecreated TIMESTAMP DEFAULT (NOW() AT TIME ZONE 'UTC'),
    CONSTRAINT PK_user PRIMARY KEY (userid)
);

CREATE TABLE IF NOT EXISTS EXPENSE_RECORDER.category (
    categoryid UUID DEFAULT uuid_generate_v4(),
    categoryname VARCHAR(50) NOT NULL,
    description  VARCHAR(100),
    userid BIGSERIAL,
    CONSTRAINT PK_category PRIMARY KEY (categoryid),
    CONSTRAINT FK_user_category FOREIGN KEY (userid) REFERENCES EXPENSE_RECORDER.user(userid)
);

CREATE TABLE IF NOT EXISTS EXPENSE_RECORDER.entry (
    entryno UUID DEFAULT uuid_generate_v4(),
    userid BIGSERIAL,
    entrytime TIMESTAMP DEFAULT (NOW() AT TIME ZONE 'UTC'),
    monthname EXPENSE_RECORDER.monthname DEFAULT 'NOT_SPECIFIED',
    month DATE NOT NULL,
    amount FLOAT8 DEFAULT 0.00,
    description VARCHAR(100),
    lastmodified TIMESTAMP DEFAULT (NOW() AT TIME ZONE 'UTC'),
    CONSTRAINT PK_entry PRIMARY KEY (entryno),
    CONSTRAINT FK_user_entry FOREIGN KEY (userid) REFERENCES EXPENSE_RECORDER.user(userid)
);

CREATE TABLE IF NOT EXISTS EXPENSE_RECORDER.item (
    itemno UUID DEFAULT uuid_generate_v4(),
    itemname VARCHAR(50) NOT NULL,
    price FLOAT8 DEFAULT 0.00,
    count INTEGER DEFAULT 1,
    totalamount FLOAT8 DEFAULT 0.00,
    entryno UUID,
    description VARCHAR(100),
    userid BIGSERIAL,
    categoryid UUID,
    CONSTRAINT PK_item PRIMARY KEY (itemno),
    CONSTRAINT FK_entry_item FOREIGN KEY (entryno) REFERENCES EXPENSE_RECORDER.entry(entryno),
    CONSTRAINT FK_user_item FOREIGN KEY (userid) REFERENCES EXPENSE_RECORDER.user(userid),
    CONSTRAINT FK_category_item FOREIGN KEY (categoryid) REFERENCES EXPENSE_RECORDER.category(categoryid)
);
