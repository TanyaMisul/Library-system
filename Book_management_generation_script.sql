create schema if not exists `book_management_system`;
use `book_management_system`;


create table if not exists `profiles`(

	`id` int not null auto_increment,
    `fullName` varchar(100) not null,
    `age` int not null,
    `sex` int not null,
     constraint `PK_profiles` primary key (`id` ASC)
);

create table if not exists `users`(

	`id` int not null auto_increment,
    `login` varchar(50) not null,
    `password` varchar(50) not null,
    `status` int not null,
    `profileId` int not null,
     constraint `PK_users` primary key (`id` ASC),
     constraint `FK_users_profiles` foreign key (`profileId`) references `profiles`(`id`)
);

create table if not exists `admins`(

	`id` int not null auto_increment,
    `login` varchar(50) not null,
    `password` varchar(50) not null,
     constraint `PK_admins` primary key (`id` ASC)
);

create table if not exists `masters`(

	`id` int not null auto_increment,
    `login` varchar(50) not null,
    `password` varchar(50) not null,
     constraint `PK_masters` primary key (`id` ASC)
);

create table if not exists `books` ( 
	`id` int not null auto_increment,
	`name` varchar(50) not null,
	`author` varchar(50) not null,
	`genre` varchar(50) not null,
	`amount` int not null,
    constraint `PK_books` primary key (`id` ASC)
);

create table if not exists `user_books` ( 
	`userId` int not null,
    `bookId` int not null,
    `date`   date not null,
    constraint `FK_user_books_users` foreign key (`userId`) references `users`(`id`),
    constraint `FK_user_books_books` foreign key (`bookId`) references `books`(`id`)
);

insert into `admins` (login, password) values ('admin', 'admin');