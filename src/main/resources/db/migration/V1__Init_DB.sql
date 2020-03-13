create sequence hibernate_sequence start 1 increment 1;

create table account_activities (
  account_id int8 not null,
  activity_id int8 not null,
  primary key (account_id, activity_id)
  );

create table account_role (
  account_id int8 not null,
  roles varchar(255)
  );

create table accounts (
  account_id int8 not null,
  login varchar(255) not null,
  password varchar(255) not null,
  primary key (account_id)
  );

create table activities (
  activity_id int8 not null,
  activity_capacity int4 not null check (activity_capacity>=1),
  date date not null,
  activity_finish time not null,
  available boolean,
  activity_name varchar(25) not null,
  activity_price numeric(19, 2) not null check (activity_price>=1),
  activity_start time not null,
  activity_type_id int8 not null,
  lounge_id int8 not null,
  mentor_id int8 not null,
  primary key (activity_id)
  );

create table activity_types (
  activity_type_id int8 not null,
  available boolean,
  activity_type_name varchar(30) not null,
  primary key (activity_type_id)
  );

create table activity_users (
  activity_id int8 not null,
  user_id int8 not null,
  primary key (activity_id, user_id)
  );

create table lounge (
  lounge_id int8 not null,
  lounge_address varchar(255) not null,
  lounge_capacity int4 not null check (lounge_capacity>=1),
  lounge_work_finish time not null,
  lounge_name varchar(255) not null,
  lounge_work_start time not null, primary key (lounge_id)
  );

create table lounge_activity_types (
  lounge_id int8 not null,
  activity_type_id int8 not null,
  primary key (lounge_id, activity_type_id)
  );

create table usrs (
  user_id int8 not null,
  birthday date,
  email varchar(255) not null,
  first_name varchar(25) not null,
  last_name varchar(25) not null,
  phone_number varchar(255) not null,
  account_id int8, primary key (user_id)
  );

alter table if exists accounts
  add constraint UK_cc2c9baeppipgy2rjeccwcqs0
  unique (login);

alter table if exists activity_types
  add constraint UK_4e4aiemox5yiy04biw17tkyh3
  unique (activity_type_name);

alter table if exists lounge
  add constraint UK_ky079e80fd0br6rildxl1o7ho
  unique (lounge_name);

alter table if exists usrs
  add constraint UK_7oumg85hkh656bcxffemvsd2n
  unique (email);

alter table if exists account_activities
  add constraint activity_account_fk
  foreign key (activity_id) references activities;

alter table if exists account_activities
  add constraint account_activity_fk
  foreign key (account_id) references accounts;

alter table if exists account_role
  add constraint role_account_fk
  foreign key (account_id) references accounts;

alter table if exists activities
  add constraint activity_activity_type_fk
  foreign key (activity_type_id) references activity_types;

alter table if exists activities
  add constraint activity_lounge_fk
  foreign key (lounge_id) references lounge;

alter table if exists activities
  add constraint activity_mentor_fk
  foreign key (mentor_id) references usrs;

alter table if exists activity_users
  add constraint activity_user_fk
  foreign key (user_id) references usrs;

alter table if exists activity_users
  add constraint user_activity_fk
  foreign key (activity_id) references activities;

alter table if exists lounge_activity_types
  add constraint activity_type_lounge_fk
  foreign key (activity_type_id) references activity_types;

alter table if exists lounge_activity_types
  add constraint lounge_activity_type_fk
  foreign key (lounge_id) references lounge;

alter table if exists usrs
  add constraint user_account_fk
  foreign key (account_id) references accounts;
