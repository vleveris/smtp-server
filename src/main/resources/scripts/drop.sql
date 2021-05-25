alter table if exists account drop constraint if exists FKpy61tgfe9h2a1r5pn6i6fy5j5
alter table if exists account_mail drop constraint if exists FK603ra0r8e9yamqqn22dac7mrj
alter table if exists account_mail drop constraint if exists FKevwsj100vctk8ibmr2v8ic99v
drop table if exists account cascade
drop table if exists account_mail cascade
drop table if exists domain cascade
drop table if exists mail cascade
drop sequence if exists hibernate_sequence
