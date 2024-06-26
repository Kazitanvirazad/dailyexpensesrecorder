-- Author: Kazi Tanvir Azad
-- Some of the data types may be specific to PostgreSQL

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS EXPENSE_RECORDER;

CREATE TYPE EXPENSE_RECORDER.monthname as enum
('JANUARY','FEBRUARY','MARCH','APRIL','MAY','JUNE','JULY','AUGUST','SEPTEMBER','OCTOBER','NOVEMBER','DECEMBER','NOT_SPECIFIED');
CREATE CAST (varchar(15) AS EXPENSE_RECORDER.monthname) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS EXPENSE_RECORDER.avatar(
    avatarid SERIAL,
    avatarencoded TEXT,
    isdefaultavatar BOOL,
    CONSTRAINT PK_avatar PRIMARY KEY (avatarid)
);

CREATE TABLE IF NOT EXISTS EXPENSE_RECORDER.user (
    userid BIGSERIAL,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50),
    email VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    hashedpassword VARCHAR(150) NOT NULL,
    isloggedout BOOL,
    datecreated TIMESTAMP DEFAULT (NOW() AT TIME ZONE 'UTC') NOT NULL,
    avatarid SERIAL,
    user_bio varchar(100),
    entrycount INTEGER DEFAULT 0 NOT NULL,
    CONSTRAINT PK_user PRIMARY KEY (userid),
    CONSTRAINT FK_avatar_user FOREIGN KEY (avatarid) REFERENCES EXPENSE_RECORDER.avatar(avatarid)
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
    entryid UUID DEFAULT uuid_generate_v4(),
    userid BIGSERIAL,
    creationtime TIMESTAMP DEFAULT (NOW() AT TIME ZONE 'UTC') NOT NULL,
    monthname EXPENSE_RECORDER.monthname DEFAULT 'NOT_SPECIFIED' NOT NULL,
    entryname VARCHAR(100) DEFAULT 'NOT_SPECIFIED' NOT NULL,
    entrymonth DATE NOT NULL,
    amount FLOAT8 DEFAULT 0.00 NOT NULL,
    description VARCHAR(100),
    lastmodified TIMESTAMP DEFAULT (NOW() AT TIME ZONE 'UTC') NOT NULL,
    itemcount INTEGER DEFAULT 0 NOT NULL,
    CONSTRAINT PK_entry PRIMARY KEY (entryid),
    CONSTRAINT FK_user_entry FOREIGN KEY (userid) REFERENCES EXPENSE_RECORDER.user(userid)
);

CREATE TABLE IF NOT EXISTS EXPENSE_RECORDER.entry_year (
    entryyearid UUID DEFAULT uuid_generate_v4(),
    userid BIGSERIAL,
    year VARCHAR(4) NOT NULL,
    year_itemcount INTEGER DEFAULT 0 NOT NULL,
    year_entrycount INTEGER DEFAULT 0 NOT NULL,
    CONSTRAINT PK_entry_year PRIMARY KEY (entryyearid),
    CONSTRAINT FK_user_item FOREIGN KEY (userid) REFERENCES EXPENSE_RECORDER.user(userid)
);

CREATE TABLE IF NOT EXISTS EXPENSE_RECORDER.entry_month (
    entrymonthid UUID DEFAULT uuid_generate_v4(),
    userid BIGSERIAL,
    month VARCHAR(10) NOT NULL,
    year VARCHAR(4) NOT NULL,
    month_itemcount INTEGER DEFAULT 0 NOT NULL,
    month_entrycount INTEGER DEFAULT 0 NOT NULL,
    CONSTRAINT PK_entry_month PRIMARY KEY (entrymonthid),
    CONSTRAINT FK_user_item FOREIGN KEY (userid) REFERENCES EXPENSE_RECORDER.user(userid)
);

CREATE TABLE IF NOT EXISTS EXPENSE_RECORDER.item (
    itemid UUID DEFAULT uuid_generate_v4(),
    itemname VARCHAR(50) NOT NULL,
    price FLOAT8 DEFAULT 0.00 NOT NULL,
    count INTEGER DEFAULT 1 NOT NULL,
    totalamount FLOAT8 DEFAULT 0.00 NOT NULL,
    entryid UUID,
    description VARCHAR(100),
    userid BIGSERIAL,
    categoryid UUID,
    CONSTRAINT PK_item PRIMARY KEY (itemid),
    CONSTRAINT FK_entry_item FOREIGN KEY (entryid) REFERENCES EXPENSE_RECORDER.entry(entryid),
    CONSTRAINT FK_user_item FOREIGN KEY (userid) REFERENCES EXPENSE_RECORDER.user(userid),
    CONSTRAINT FK_category_item FOREIGN KEY (categoryid) REFERENCES EXPENSE_RECORDER.category(categoryid)
);
