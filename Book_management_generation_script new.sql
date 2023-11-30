create schema if not exists `book_management_system`;
use `book_management_system`;

create table if not exists `accounts`(

	`id` int not null auto_increment,
    `login` varchar(50) not null,
    `password` varchar(50) not null,
    `role` int not null,
    `isActive` int not null,
     constraint `PK_accounts` primary key (`id` ASC)
);
create table if not exists `userProfiles`(

	`id` int not null auto_increment,
	`accountId` int not null,
    `fullName` varchar(100) not null,
    `age` int not null,
    `sex` int not null,
     constraint `PK_profiles` primary key (`id` ASC),
     constraint `FK_users_accounts` foreign key (`accountId`) references `accounts`(`id`)
);

create table if not exists `employeeProfiles`(

	`id` int not null auto_increment,
    `accountId` int not null,
    `fullName` varchar(100) not null,
	constraint `PK_profiles` primary key (`id` ASC),
	constraint `FK_employee_accounts` foreign key (`accountId`) references `accounts`(`id`)
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
    `startDate` date not null,
    `countOfDays` int not null,
    constraint `FK_user_books_users` foreign key (`userId`) references `userProfiles`(`id`),
    constraint `FK_user_books_books` foreign key (`bookId`) references `books`(`id`)
);

create table if not exists `reports` ( 
	`id` int not null auto_increment,
	`accountId` int not null,
    `date` date not null,
    `path` varchar(256) not null,
    constraint `PK_reports` primary key (`id` ASC),
	constraint `FK_reports_accounts` foreign key (`accountId`) references `accounts`(`id`)
);


