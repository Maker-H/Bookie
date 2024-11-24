drop table if exists AppUser;
drop table if exists Book;
drop table if exists Review;

create table if not exists AppUser (
    uuid identity,
    userId varchar(100) not null,
    userPwd varchar(100) not null,
    createdAt timestamp not null,
    modifiedAt timestamp
);

create table if not exists Book (
    uuid identity,
    bookName varchar(50),
    bookAuthor varchar(50),
    bookDescription varchar(50),
    bookImage blob,
    createdAt timestamp not null,
    modifiedAt timestamp
);

create table if not exists Review (
    uuid identity,
    appUser bigint not null,
    book bigint not null,
    review varchar(50) not null,
    createdAt timestamp not null,
    modifiedAt timestamp
);

alter table Review
    add foreign key (appUser) references AppUser(uuid);
alter table Review
    add foreign key (book) references Book(uuid);
