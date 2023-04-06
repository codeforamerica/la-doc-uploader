-- This will make the original file_id a column a uuid column

alter table user_files add column new_id uuid DEFAULT ${uuid_function}();
alter table user_files drop column file_id;
alter table user_files rename column new_id to file_id;
alter table user_files alter column file_id set not null;
alter table user_files add primary key (file_id);