create sequence hibernate_sequence start 1 increment 1
create table account (id int8 not null, password varchar(255) not null, role int4 not null, username varchar(100) not null, domain_id int8, primary key (id))
create table account_mail (mail_id int8 not null, account_id int8 not null, primary key (mail_id, account_id))
create table domain (id int8 not null, name varchar(255) not null, primary key (id))
create table mail (id int8 not null, date timestamp, message text, reverse_path varchar(255), primary key (id))
alter table if exists domain add constraint UK_ga2sqp4lboblqv6oks9oryd9q unique (name)
alter table if exists account add constraint FKpy61tgfe9h2a1r5pn6i6fy5j5 foreign key (domain_id) references domain
alter table if exists account_mail add constraint FK603ra0r8e9yamqqn22dac7mrj foreign key (account_id) references account
alter table if exists account_mail add constraint FKevwsj100vctk8ibmr2v8ic99v foreign key (mail_id) references mail
