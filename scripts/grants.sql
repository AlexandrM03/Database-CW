-- programmer --
create role programmer_role;
grant
    create session,
    create table,
    create view,
    create procedure,
    create profile,
    create user,
    drop user,
    create role,
    drop profile,
    create any index,
    create any sequence
    to programmer_role with admin option;

create profile programmer_profile limit
    password_life_time 180
    sessions_per_user 10
    failed_login_attempts 7
    password_lock_time 1
    password_reuse_time 10
    password_grace_time default
    connect_time 180
    idle_time 30;

alter user programmer profile programmer_profile;
grant programmer_role to programmer;

-- show grants for programmer;
grant all privileges to programmer;
