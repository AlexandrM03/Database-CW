create or replace package user_package as
    function encrypt_password(password in varchar2) return users.password_hash%type;
    procedure register_user(username_in in varchar2, password in varchar2);
    function login_user(username_in in varchar2, password in varchar2) return sys_refcursor;
    procedure fulfill_user_data(username_in in users.username%type, first_name_in in users.first_name%type,
                                last_name_in in users.last_name%type, telephone_in in users.telephone%type);
    function get_user_data(username_in in users.username%type) return sys_refcursor;
    procedure add_item_to_order(order_id_in in orders.id%type, item_name_in in order_items.item_name%type,
                                item_weight_in in order_items.item_weight%type,
                                item_volume_in in order_items.item_volume%type);
    procedure create_order(username_in in users.username%type, start_point_in in orders.start_point%type,
                           end_point_in in orders.end_point%type);
    function get_user_orders(username_in in users.username%type) return sys_refcursor;
    function get_countries return sys_refcursor;
    function get_cities(country_name_in in boundaries.name%type) return sys_refcursor;
    procedure rate_driver(order_id_in in orders.id%type, rating_in in drivers_ratings.rating%type,
                          message_in in drivers_ratings.message%type);
    function get_order_items(order_id_in in orders.id%type) return sys_refcursor;
    function get_user_notifications(username_in in users.username%type) return sys_refcursor;
    procedure remove_notification(notification_id_in in user_notifications.id%type);
    function get_order_status(order_id_in in orders.id%type) return sys_refcursor;
end user_package;

create or replace package body user_package as
    -- 1. Function that takes a password and returns a hash
    function encrypt_password(password in varchar2) return users.password_hash%type is
        hash users.password_hash%type;
    begin
        hash := utl_raw.cast_to_raw(utl_encode.base64_encode(utl_raw.cast_to_raw(password)));
        return hash;
    exception
        when others then
            raise_application_error(-20001, 'Error encrypting password');
    end encrypt_password;

    -- 2. Procedure to register a new user
    procedure register_user(username_in in varchar2, password in varchar2) is
        password_hash users.password_hash%type;
        user_role_id  user_roles.id%type;
    begin
        password_hash := user_package.encrypt_password(password);
        select id into user_role_id from user_roles where name = 'user';
        insert into users (username, password_hash, user_role_id)
        values (username_in, password_hash, user_role_id);
        commit;
    exception
        when others then
            rollback;
            raise_application_error(-20001, 'Username already exists');
    end register_user;

    -- 3. Function to login user and return a cursor with username, password_hash and role name
    function login_user(username_in in varchar2, password in varchar2) return sys_refcursor is
        v_password_hash users.password_hash%type;
        users_count     number;
        user_cursor     sys_refcursor;
    begin
        v_password_hash := user_package.encrypt_password(password);
        select count(*) into users_count from users where username = username_in and password_hash = v_password_hash;
        if users_count = 1 then
            open user_cursor for
                select u.username as username, ur.name as role_name
                from users u
                         join user_roles ur on ur.id = u.user_role_id
                where u.username = username_in
                  and u.password_hash = v_password_hash;
            return user_cursor;
        else
            raise_application_error(-20001, 'Invalid username or password');
        end if;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end login_user;

    -- 4. Procedure to fulfill user data
    procedure fulfill_user_data(username_in in users.username%type, first_name_in in users.first_name%type,
                                last_name_in in users.last_name%type, telephone_in in users.telephone%type) is
        users_count number;
    begin
        select count(*) into users_count from users where username = username_in;
        if users_count = 1 then
            update users
            set first_name = first_name_in,
                last_name  = last_name_in,
                telephone  = telephone_in
            where username = username_in;
            commit;
        else
            raise_application_error(-20001, 'Invalid username');
        end if;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end fulfill_user_data;

    -- 5. Function to get user data
    function get_user_data(username_in in users.username%type) return sys_refcursor is
        users_count number;
        user_cursor sys_refcursor;
    begin
        select count(*) into users_count from users where username = username_in;
        if users_count = 1 then
            open user_cursor for
                select u.username   as username,
                       u.first_name as first_name,
                       u.last_name  as last_name,
                       u.telephone  as telephone
                from users u
                where u.username = username_in;
            return user_cursor;
        else
            raise_application_error(-20001, 'Invalid username');
        end if;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_user_data;

    -- 6. Procedure to add item to order
    procedure add_item_to_order(order_id_in in orders.id%type, item_name_in in order_items.item_name%type,
                                item_weight_in in order_items.item_weight%type,
                                item_volume_in in order_items.item_volume%type) is
    begin
        insert into order_items (order_id, item_name, item_weight, item_volume)
        values (order_id_in, item_name_in, item_weight_in, item_volume_in);
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end add_item_to_order;

    -- 7. Procedure to create a new order
    procedure create_order(username_in in users.username%type, start_point_in in orders.start_point%type,
                           end_point_in in orders.end_point%type) is
        user_id         users.id%type;
        order_status_id order_statuses.id%type;
    begin
        select id into user_id from users where username = username_in;
        select id into order_status_id from order_statuses where status like 'pending';
        insert into orders (user_id, start_point, end_point, order_status_id, start_date)
        values (user_id, start_point_in, end_point_in, order_status_id, sysdate);
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end create_order;

    -- 8. Function to get all orders for a user
    function get_user_orders(username_in in users.username%type) return sys_refcursor is
        v_user_id     users.id%type;
        orders_cursor sys_refcursor;
    begin
        select id into v_user_id from users where username = username_in;
        open orders_cursor for
            select o.id          as order_id,
                   o.start_point as start_point,
                   o.end_point   as end_point,
                   o.price       as price,
                   o.start_date  as start_date,
                   o.end_date    as end_date,
                   d.last_name   as driver_last_name,
                   d.first_name  as driver_first_name,
                   d.rating      as driver_rating,
                   os.status     as order_status
            from orders o
                     join order_statuses os on os.id = o.order_status_id
                     left join drivers d on d.id = o.driver_id
            where o.user_id = v_user_id
            order by o.start_date desc;
        return orders_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_user_orders;

    -- 9. Function to get name of countries
    function get_countries return sys_refcursor is
        countries_cursor sys_refcursor;
    begin
        open countries_cursor for
            select name
            from boundaries;
        return countries_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_countries;

    -- 10. Function to get all cities of a country
    function get_cities(country_name_in in boundaries.name%type) return sys_refcursor is
        cities_cursor sys_refcursor;
    begin
        open cities_cursor for
            select name
            from cities
            where sdo_relate(point, (select boundary from boundaries where name = country_name_in),
                             'mask=anyinteract') = 'TRUE'
            order by name;
        return cities_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_cities;

    -- 11. Procedure to rate a driver
    procedure rate_driver(order_id_in in orders.id%type, rating_in in drivers_ratings.rating%type,
                          message_in in drivers_ratings.message%type) is
        driver_id drivers.id%type;
    begin
        select driver_id into driver_id from orders where id = order_id_in;
        insert into drivers_ratings (driver_id, rating, message)
        values (driver_id, rating_in, message_in);
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end rate_driver;

    -- 12. Function to get order items
    function get_order_items(order_id_in in orders.id%type) return sys_refcursor is
        items_cursor sys_refcursor;
    begin
        open items_cursor for
            select item_name,
                   item_weight,
                   item_volume
            from order_items
            where order_id = order_id_in;
        return items_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_order_items;

    -- 13. Function to get all notifications for user
    function get_user_notifications(username_in in users.username%type) return sys_refcursor is
        user_id              users.id%type;
        notifications_cursor sys_refcursor;
    begin
        select id into user_id from users where username = username_in;
        open notifications_cursor for select id, operation_date, message
                                      from user_notifications
                                      where user_id = user_id;
        return notifications_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_user_notifications;

    -- 14. Procedure to remove a notification
    procedure remove_notification(notification_id_in in user_notifications.id%type) is
    begin
        delete from user_notifications where id = notification_id_in;
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end remove_notification;

    -- 15. Function to get order status
    function get_order_status(order_id_in in orders.id%type) return sys_refcursor is
        order_status_cursor sys_refcursor;
    begin
        open order_status_cursor for
            select os.status as status
            from orders o
                     join order_statuses os on os.id = o.order_status_id
            where o.id = order_id_in;
        return order_status_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_order_status;
end user_package;

-- test get order status --
declare
    v_cursor sys_refcursor;
    v_status order_statuses.status%type;
begin
    v_cursor := user_package.get_order_status(101);
    loop
        fetch v_cursor into
            v_status;
        exit when v_cursor%notfound;
        dbms_output.put_line(v_status);
    end loop;
end;

-- tests --
begin
    user_package.register_user('test', 'test');
end;

-- test login --
declare
    user_cursor   sys_refcursor;
    username      users.username%type;
    password_hash users.password_hash%type;
    role_name     user_roles.name%type;
begin
    user_cursor := user_package.login_user('test', 'test');
    loop
        fetch user_cursor into username, password_hash, role_name;
        exit when user_cursor%notfound;
        dbms_output.put_line(username || ' ' || password_hash || ' ' || role_name);
    end loop;
end;

-- test fulfill user data --
begin
    user_package.fulfill_user_data('test', 'test1', 'test2', 'test3');
end;

-- test get user data --
declare
    user_cursor sys_refcursor;
    username    users.username%type;
    first_name  users.first_name%type;
    last_name   users.last_name%type;
    telephone   users.telephone%type;
begin
    user_cursor := user_package.get_user_data('testSpring2');
    loop
        fetch user_cursor into username, first_name, last_name, telephone;
        exit when user_cursor%notfound;
        dbms_output.put_line(username || ' ' || first_name || ' ' || last_name || ' ' || telephone);
    end loop;
end;

-- test create order --
begin
    user_package.create_order('testSpring', 'Warsaw', 'Vilnius');
    --user_package.add_item_to_order(62, 'test', 100, 10);
end;

select start_point, end_point
from orders
where id = 18;

declare
    v_start_point orders.start_point%type;
    v_end_point   orders.end_point%type;
begin
    select start_point, end_point into v_start_point, v_end_point from orders where id = 18;
    dbms_output.put_line(v_start_point);
    dbms_output.put_line(v_end_point);
end;

-- test get user orders --
declare
    orders_cursor sys_refcursor;
    order_id      orders.id%type;
    start_point   orders.start_point%type;
    end_point     orders.end_point%type;
    price         orders.price%type;
    start_date    orders.start_date%type;
    end_date      orders.end_date%type;
    last_name     drivers.last_name%type;
    first_name    drivers.first_name%type;
    rating        drivers.rating%type;
    status        order_statuses.status%type;
begin
    orders_cursor := user_package.get_user_orders('testSpring2');
    loop
        fetch orders_cursor into order_id, start_point, end_point, price, start_date, end_date, last_name, first_name, rating, status;
        exit when orders_cursor%notfound;
        dbms_output.put_line(order_id || ' ' || start_point || ' ' || end_point || ' ' || price || ' ' || start_date ||
                             ' ' || end_date || ' ' || last_name || ' ' || first_name || ' ' || rating || ' ' || status);
    end loop;
end;

-- test get countries --
declare
    countries_cursor sys_refcursor;
    country_name     boundaries.name%type;
begin
    countries_cursor := user_package.get_countries;
    loop
        fetch countries_cursor into country_name;
        exit when countries_cursor%notfound;
        dbms_output.put_line(country_name);
    end loop;
end;

-- test get cities --
declare
    cities_cursor sys_refcursor;
    city_name     cities.name%type;
begin
    cities_cursor := user_package.get_cities('Latvia');
    loop
        fetch cities_cursor into city_name;
        exit when cities_cursor%notfound;
        dbms_output.put_line(city_name);
    end loop;
end;

-- test get order items --
declare
    items_cursor sys_refcursor;
    item_name    order_items.item_name%type;
    item_weight  order_items.item_weight%type;
    item_volume  order_items.item_volume%type;
begin
    items_cursor := user_package.get_order_items(2);
    loop
        fetch items_cursor into item_name, item_weight, item_volume;
        exit when items_cursor%notfound;
        dbms_output.put_line(item_name || ' ' || item_weight || ' ' || item_volume);
    end loop;
end;

-- test get user notifications --
declare
    notifications_cursor sys_refcursor;
    notification_id      user_notifications.id%type;
    operation_date       user_notifications.operation_date%type;
    message              user_notifications.message%type;
begin
    notifications_cursor := user_package.get_user_notifications('testSpring');
    loop
        fetch notifications_cursor into notification_id, operation_date, message;
        exit when notifications_cursor%notfound;
        dbms_output.put_line(notification_id || ' ' || operation_date || ' ' || message);
    end loop;
end;

-- test remove notification --
begin
    user_package.remove_notification(1);
end;

-- test rate driver
begin
    user_package.rate_driver(81, 1, 'Test rej');
end;