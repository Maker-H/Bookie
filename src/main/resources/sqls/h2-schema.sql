-- 외래 키 제약 조건 삭제
--ALTER TABLE REVIEW DROP CONSTRAINT FK_REVIEW_APPUSER;
--ALTER TABLE REVIEW DROP CONSTRAINT FK_REVIEW_BOOK;

-- 테이블 삭제
--DROP TABLE IF EXISTS REVIEW;
--DROP TABLE IF EXISTS BOOK;
--DROP TABLE IF EXISTS APPUSER;


-- APPUSER 테이블 생성
CREATE TABLE APPUSER (
	TSID VARCHAR(100) NOT NULL,
	ID VARCHAR(50),
	PASSWORD VARCHAR(255),
	NICKNAME VARCHAR(50),
	EMAIL VARCHAR(100),
	ROLE VARCHAR(20),
	CREATED_ON TIMESTAMP,
	MODIFIED_ON TIMESTAMP,
	CONSTRAINT PK_APPUSER PRIMARY KEY (TSID)
);

-- BOOK 테이블 생성
CREATE TABLE BOOK (
	TSID VARCHAR(50) NOT NULL,
	TITLE VARCHAR(255),
	AUTHOR VARCHAR(50),
	PUBLISHED_DATE VARCHAR(50),
	ISBN VARCHAR(50),
	DESCRIPTION CLOB,
	THUMBNAIL_URL VARCHAR(500),
	CREATED_ON TIMESTAMP,
	MODIFIED_ON TIMESTAMP,
	CONSTRAINT PK_BOOK PRIMARY KEY (TSID)
);

-- REVIEW 테이블 생성
CREATE TABLE REVIEW (
	TSID VARCHAR(100) NOT NULL,
	APPUSER_TSID VARCHAR(100) NOT NULL,
	BOOK_TSID VARCHAR(100) NOT NULL,
	CONTENT CLOB,
	RATE DECIMAL(2,1),
	CREATED_ON TIMESTAMP,
	MODIFIED_ON TIMESTAMP,
	CONSTRAINT PK_REVIEW PRIMARY KEY (TSID)
);

-- 외래 키 추가: REVIEW.APPUSER_TSID -> APPUSER.TSID
ALTER TABLE REVIEW
ADD CONSTRAINT FK_REVIEW_APPUSER
FOREIGN KEY (APPUSER_TSID) REFERENCES APPUSER (TSID);

-- 외래 키 추가: REVIEW.BOOK_TSID -> BOOK.TSID
ALTER TABLE REVIEW
ADD CONSTRAINT FK_REVIEW_BOOK
FOREIGN KEY (BOOK_TSID) REFERENCES BOOK (TSID);

