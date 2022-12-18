create profile users_profile limit
    password_life_time 180
    sessions_per_user 20
    failed_login_attempts 5
    password_lock_time 60
    password_reuse_time 10
    connect_time 180
    idle_time 60;

-- customer --
create role customer_role;
grant connect to customer_role;
grant execute on programmer.user_package to customer_role;

create user customer
    identified by qwerty11
    default tablespace course_ts
    profile users_profile
    quota unlimited on course_ts;

grant customer_role to customer;

-- manager --
create role manager_role;
grant connect to manager_role;
grant execute on programmer.admin_package to manager_role;

create user manager
    identified by qwerty11
    default tablespace course_ts
    profile users_profile
    quota unlimited on course_ts;

grant manager_role to manager;

-- driver --
create role driver_role;
grant connect to driver_role;
grant execute on programmer.driver_package to driver_role;

create user driver
    identified by qwerty11
    default tablespace course_ts
    profile users_profile
    quota unlimited on course_ts;

grant driver_role to driver;