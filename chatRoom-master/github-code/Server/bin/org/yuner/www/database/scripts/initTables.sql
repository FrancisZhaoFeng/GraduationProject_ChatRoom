use my_IM_GGMM;
create table userTable
(
	userId 		int				auto_increment	primary key,
	password	varchar(100),
	userName 	varchar(100)	unique,
	birthYear   smallint,
	birthMonth  smallint,
	birthDay    smallint,
	gender 		smallint,				-- 0 for female, 1 for male 
	avatarId 	int,
	isOnline	int,					-- 0 for offline, 1 for online
	signupTime	varchar(100),			-- XXXX-XX-XX-XX-XX (year-month-day-hour-minute)
	hometown	varchar(100),			-- country-provice/state-city--district/county/etc. (so four layers)
	location	varchar(100)			-- same as hometown
);

create table friendListTable
(
	masterId 			int,                   -- the id of the user this friendlist belongs to
	friendId 			int,
	friendName 			varchar(100),
	friendBirthYear     smallint,
	friendBirthMonth	smallint,
	friendBirthDay		smallint,
	friendGender 		smallint,
	friendAvatarId 		int,
	friendIsOnline		int,
	friendSignupTime	varchar(100),
	friendHometown		varchar(100),
	friendLocation		varchar(100),
	foreign key(masterId) references userTable(userId) on delete cascade
);

create table unSendMsgs
(
	senderId 			int,
	receiverId 			int,
	msg					text,
	_datetime			datetime,
	type				int
)
