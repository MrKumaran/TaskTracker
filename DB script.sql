create table authentication
(
    user_id  varchar(36) not null
        primary key,
    mail     varchar(60) not null,
    password varchar(64) not null,
    salt     varchar(10) not null,
    constraint authentication_un
        unique (mail)
)
    comment 'Authentication details';

create table profile
(
    user_id    varchar(36)  not null
        primary key,
    user_name  varchar(50)  not null,
    avatar_url varchar(300) null,
    constraint profile_authentication_user_id_fk
        foreign key (user_id) references authentication (user_id)
            on delete cascade
)
    comment 'User Profile table ';

create table task
(
    user_id     varchar(36)  not null,
    task_id     varchar(36)  not null
        primary key,
    task_title  varchar(100) not null,
    due         datetime     not null,
    isDone      tinyint(1)   not null,
    completedAt datetime     null,
    constraint task_profile_user_id_fk
        foreign key (user_id) references profile (user_id)
            on delete cascade
)
    comment 'Task table';

