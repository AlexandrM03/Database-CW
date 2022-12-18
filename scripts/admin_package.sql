create or replace package admin_package as
    function evaluate_order_price(order_id_in in orders.id%type) return number;
    procedure update_order_price(order_id_in in orders.id%type);
    procedure accept_order(order_id_in in orders.id%type);
    function encrypt_password(password in varchar2) return drivers.password_hash%type;
    procedure hire_driver(username_in in drivers.username%type, driver_password in varchar2);
    function get_all_users return sys_refcursor;
    function get_all_drivers return sys_refcursor;
    function get_all_orders return sys_refcursor;
    function get_all_orders_by_driver(driver_username_in in drivers.username%type) return sys_refcursor;
    function get_all_orders_by_user(username_in in users.username%type) return sys_refcursor;
    function get_order_items(order_id_in in orders.id%type) return sys_refcursor;
    procedure reject_order(order_id_in in orders.id%type);
    function get_admin_notifications return sys_refcursor;
    procedure remove_admin_notification(notification_id_in in admin_notifications.id%type);
    procedure confirm_driver_rating(driver_username_in in drivers.username%type);
    procedure remove_driver_rating(rating_id_in in drivers_ratings.id%type);
    function get_driver_rating(driver_username_in in drivers.username%type) return sys_refcursor;
    function get_all_users_pagination(page_number_in in number, page_size_in in number) return sys_refcursor;
    procedure export_orders(start_date_in in date, end_date_in in date);
end admin_package;

create or replace package body admin_package as
    -- 1. Function to evaluate the price of an order
    function evaluate_order_price(order_id_in in orders.id%type) return number is
        order_price   number := 0;
        distance      number(10, 2);
        v_start_point orders.start_point%type;
        v_end_point   orders.end_point%type;
        v_item_weight order_items.item_weight%type;
        v_item_volume order_items.item_volume%type;
        cursor c_order_items is select item_weight, item_volume
                                from order_items
                                where order_id = order_id_in;
    begin
        select start_point, end_point into v_start_point, v_end_point from orders where id = order_id_in;
        select sdo_geom.sdo_distance(
                       (select point from cities where name like v_start_point),
                       (select point from cities where name like v_end_point),
                       0.005, 'unit=km')
        into distance
        from dual;
        order_price := distance * 0.85;
        open c_order_items;
        loop
            fetch c_order_items into v_item_weight, v_item_volume;
            exit when c_order_items%notfound;
            order_price := order_price + (v_item_weight * 0.2) + (v_item_volume * 2.6);
        end loop;
        close c_order_items;
        return order_price;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end evaluate_order_price;

    -- 2. Procedure to update the price of an order
    procedure update_order_price(order_id_in in orders.id%type) is
        order_price number;
    begin
        order_price := evaluate_order_price(order_id_in);
        update orders
        set price = order_price
        where id = order_id_in;
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end update_order_price;

    -- 3. Procedure to set order status to 'accepted'
    procedure accept_order(order_id_in in orders.id%type) is
        v_order_status_id order_statuses.status%type;
    begin
        select id into v_order_status_id from order_statuses where status = 'accepted';
        update orders
        set order_status_id = v_order_status_id
        where id = order_id_in;
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end accept_order;

    -- 4. Function that takes a password and returns a hash
    function encrypt_password(password in varchar2) return drivers.password_hash%type is
        hash drivers.password_hash%type;
    begin
        hash := utl_raw.cast_to_raw(utl_encode.base64_encode(utl_raw.cast_to_raw(password)));
        return hash;
    exception
        when others then
            raise_application_error(-20001, 'Error encrypting password');
    end encrypt_password;

    -- 5. Procedure to hire a new driver
    procedure hire_driver(username_in in drivers.username%type, driver_password in varchar2) is
        v_password_hash drivers.password_hash%type;
    begin
        v_password_hash := encrypt_password(driver_password);
        insert into drivers (username, password_hash)
        values (username_in, v_password_hash);
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end hire_driver;

    -- 6. Function to get all users
    function get_all_users return sys_refcursor is
        v_cursor   sys_refcursor;
        v_admin_id user_roles.id%type;
    begin
        select id into v_admin_id from user_roles where name = 'admin';
        open v_cursor for
            select username, first_name, last_name, telephone
            from users
            where user_role_id != v_admin_id;
        return v_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_all_users;

    -- 7. Function to get all drivers
    function get_all_drivers return sys_refcursor is
        v_cursor sys_refcursor;
    begin
        open v_cursor for
            select username, first_name, last_name, telephone, rating
            from drivers;
        return v_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_all_drivers;

    -- 8. Function to get all orders
    function get_all_orders return sys_refcursor is
        v_cursor sys_refcursor;
    begin
        open v_cursor for
            select o.id          as id,
                   o.start_point as start_point,
                   o.end_point   as end_point,
                   o.price       as price,
                   o.start_date  as start_date,
                   o.end_date    as end_date,
                   os.status     as order_status,
                   u.username    as username,
                   d.username    as driver_username
            from orders o
                     join order_statuses os on o.order_status_id = os.id
                     join users u on o.user_id = u.id
                     left join drivers d on o.driver_id = d.id
            order by o.start_date desc;
        return v_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_all_orders;

    -- 9. Function to get all orders by driver
    function get_all_orders_by_driver(driver_username_in in drivers.username%type) return sys_refcursor is
        driver_id_in drivers.id%type;
        v_cursor     sys_refcursor;
    begin
        select id into driver_id_in from drivers where username = driver_username_in;
        open v_cursor for
            select o.id          as id,
                   o.start_point as start_point,
                   o.end_point   as end_point,
                   o.price       as price,
                   o.start_date  as start_date,
                   o.end_date    as end_date,
                   os.status     as order_status,
                   u.username    as username,
                   d.username    as driver_username
            from orders o
                     join order_statuses os on o.order_status_id = os.id
                     join users u on o.user_id = u.id
                     left join drivers d on o.driver_id = d.id
            where o.driver_id = driver_id_in
            order by o.start_date desc;
        return v_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_all_orders_by_driver;

    -- 10. Function to get all orders by user
    function get_all_orders_by_user(username_in in users.username%type) return sys_refcursor is
        user_id_in users.id%type;
        v_cursor   sys_refcursor;
    begin
        select id into user_id_in from users where username like username_in;
        open v_cursor for
            select o.id          as id,
                   o.start_point as start_point,
                   o.end_point   as end_point,
                   o.price       as price,
                   o.start_date  as start_date,
                   o.end_date    as end_date,
                   os.status     as order_status,
                   u.username    as username,
                   d.username    as driver_username
            from orders o
                     join order_statuses os on o.order_status_id = os.id
                     join users u on o.user_id = u.id
                     left join drivers d on o.driver_id = d.id
            where o.user_id = user_id_in
            order by o.start_date desc;
        return v_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_all_orders_by_user;

    -- 11. Function to get all order items
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

    -- 12. Procedure to reject order
    procedure reject_order(order_id_in in orders.id%type) is
    begin
        delete from order_items where order_id = order_id_in;
        delete from orders where id = order_id_in;
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end reject_order;

    -- 13. Function to get all notifications for admin
    function get_admin_notifications return sys_refcursor is
        notifications_cursor sys_refcursor;
    begin
        open notifications_cursor for select id, operation_date, message from admin_notifications;
        return notifications_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_admin_notifications;

    -- 14. Function to remove admin notification
    procedure remove_admin_notification(notification_id_in in admin_notifications.id%type) is
    begin
        delete from admin_notifications where id = notification_id_in;
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end remove_admin_notification;

    -- 15. Procedure to confirm and evaluate driver rating
    procedure confirm_driver_rating(driver_username_in in drivers.username%type) is
        driver_id_in  drivers.id%type;
        driver_rating drivers.rating%type;
    begin
        select id into driver_id_in from drivers where username = driver_username_in;
        select avg(rating) into driver_rating from drivers_ratings where driver_id = driver_id_in;
        update drivers set rating = driver_rating where id = driver_id_in;
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end confirm_driver_rating;

    -- 16. Procedure to remove driver rating
    procedure remove_driver_rating(rating_id_in in drivers_ratings.id%type) is
    begin
        delete from drivers_ratings where id = rating_id_in;
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end remove_driver_rating;

    -- 17. Function to get all driver ratings
    function get_driver_rating(driver_username_in in drivers.username%type) return sys_refcursor is
        driver_id_in  drivers.id%type;
        rating_cursor sys_refcursor;
    begin
        select id into driver_id_in from drivers where username = driver_username_in;
        open rating_cursor for select id, rating, message
                               from drivers_ratings
                               where driver_id = driver_id_in;
        return rating_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_driver_rating;

    -- 18. Function to get all users pagination
    function get_all_users_pagination(page_number_in in number, page_size_in in number) return sys_refcursor is
        users_cursor sys_refcursor;
        v_admin_id user_roles.id%type;
    begin
        select id into v_admin_id from user_roles where name = 'admin';
        open users_cursor for
            select username, first_name, last_name, telephone
            from (select users.*, rownum rnum
                  from (select * from users where user_role_id != v_admin_id order by id) users
                  where rownum <= page_number_in * page_size_in)
            where rnum > (page_number_in - 1) * page_size_in;
        return users_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_all_users_pagination;

    -- 19. Function to export json orders where end_date between start_date and end_date
    procedure export_orders(start_date_in in date, end_date_in in date) is
        json_file   clob;
        orders_file utl_file.file_type;
        wrong_date_range exception;
    begin
        if start_date_in > end_date_in then
            raise wrong_date_range;
        end if;
        select json_object(
                       key 'orders' value json_arrayagg(
                        json_object(
                                key 'id' value o.id,
                                key 'start_point' value o.start_point,
                                key 'end_point' value o.end_point,
                                key 'price' value o.price,
                                key 'start_date' value o.start_date,
                                key 'end_date' value o.end_date,
                                key 'order_status' value os.status,
                                key 'username' value u.username,
                                key 'driver_username' value d.username
                            )))
        into json_file
        from orders o
                 join order_statuses os on o.order_status_id = os.id
                 join users u on o.user_id = u.id
                 left join drivers d on o.driver_id = d.id
        where o.end_date between start_date_in and end_date_in;
        orders_file := utl_file.fopen('JSON_DIR', 'orders.json', 'w');
        utl_file.put_line(orders_file, json_file, true);
        utl_file.fclose(orders_file);
    exception
        when wrong_date_range then
            raise_application_error(-20001, 'Start date must be less than end date');
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end export_orders;
end admin_package;

-- test update order price
begin
    admin_package.update_order_price(2);
end;

-- test accept order
begin
    admin_package.accept_order(81);
end;

-- test hire driver
begin
    admin_package.hire_driver('driver1', 'password');
end;

-- test get all users
declare
    user_username  users.username%type;
    user_firstname users.first_name%type;
    user_lastname  users.last_name%type;
    user_telephone users.telephone%type;
    v_cursor       sys_refcursor;
begin
    v_cursor := admin_package.get_all_users;
    loop
        fetch v_cursor into user_username, user_firstname, user_lastname, user_telephone;
        exit when v_cursor%notfound;
        dbms_output.put_line(user_username || ' ' || user_firstname || ' ' || user_lastname || ' ' ||
                             user_telephone);
    end loop;
    close v_cursor;
end;

-- test get all drivers
declare
    driver_id        drivers.id%type;
    driver_username  drivers.username%type;
    driver_firstname drivers.first_name%type;
    driver_lastname  drivers.last_name%type;
    driver_telephone drivers.telephone%type;
    driver_rating    drivers.rating%type;
    v_cursor         sys_refcursor;
begin
    v_cursor := admin_package.get_all_drivers;
    loop
        fetch v_cursor into driver_id, driver_username, driver_firstname, driver_lastname, driver_telephone, driver_rating;
        exit when v_cursor%notfound;
        dbms_output.put_line(driver_id || ' ' || driver_username || ' ' || driver_firstname || ' ' || driver_lastname ||
                             ' ' ||
                             driver_telephone || ' ' || driver_rating);
    end loop;
    close v_cursor;
end;

-- test get all orders
declare
    order_id     orders.id%type;
    order_start  orders.start_point%type;
    order_end    orders.end_point%type;
    order_price  orders.price%type;
    order_startd orders.start_date%type;
    order_endd   orders.end_date%type;
    order_status order_statuses.status%type;
    order_user   users.username%type;
    order_driver drivers.username%type;
    v_cursor     sys_refcursor;
begin
    v_cursor := admin_package.get_all_orders;
    loop
        fetch v_cursor into order_id, order_start, order_end, order_price, order_startd, order_endd, order_status, order_user, order_driver;
        exit when v_cursor%notfound;
        dbms_output.put_line(order_id || ' ' || order_start || ' ' || order_end || ' ' || order_price || ' ' ||
                             order_startd || ' ' || order_endd || ' ' || order_status || ' ' || order_user || ' ' ||
                             order_driver);
    end loop;
    close v_cursor;
end;

-- test get all orders by driver
declare
    order_id     orders.id%type;
    order_start  orders.start_point%type;
    order_end    orders.end_point%type;
    order_price  orders.price%type;
    order_status order_statuses.status%type;
    order_user   users.username%type;
    v_cursor     sys_refcursor;
begin
    v_cursor := admin_package.get_all_orders_by_driver(1);
    loop
        fetch v_cursor into order_id, order_start, order_end, order_price, order_status, order_user;
        exit when v_cursor%notfound;
        dbms_output.put_line(order_id || ' ' || order_start || ' ' || order_end || ' ' || order_price || ' ' ||
                             order_status || ' ' || order_user);
    end loop;
    close v_cursor;
end;

-- test get all orders by user
declare
    order_id     orders.id%type;
    order_start  orders.start_point%type;
    order_end    orders.end_point%type;
    order_price  orders.price%type;
    order_startd orders.start_date%type;
    order_endd   orders.end_date%type;
    order_status order_statuses.status%type;
    order_user   users.username%type;
    order_driver drivers.username%type;
    v_cursor     sys_refcursor;
begin
    v_cursor := admin_package.get_all_orders_by_user('testSpring2');
    loop
        fetch v_cursor into order_id, order_start, order_end, order_price, order_startd, order_endd, order_status, order_user, order_driver;
        exit when v_cursor%notfound;
        dbms_output.put_line(order_id || ' ' || order_start || ' ' || order_end || ' ' || order_price || ' ' ||
                             order_status || ' ' || order_user || ' ' || order_driver);
    end loop;
    close v_cursor;
end;

-- test get order items
declare
    item_name   order_items.item_name%type;
    item_weight order_items.item_weight%type;
    item_volume order_items.item_volume%type;
    v_cursor    sys_refcursor;
begin
    v_cursor := admin_package.get_order_items(62);
    loop
        fetch v_cursor into item_name, item_weight, item_volume;
        exit when v_cursor%notfound;
        dbms_output.put_line(item_name || ' ' || item_weight || ' ' || item_volume);
    end loop;
    close v_cursor;
end;

-- test reject order
begin
    admin_package.reject_order(62);
end;

-- test get admin notifications
declare
    notifications_cursor sys_refcursor;
    notification_id      admin_notifications.id%type;
    operation_date       admin_notifications.operation_date%type;
    message              admin_notifications.message%type;
begin
    notifications_cursor := admin_package.get_admin_notifications;
    loop
        fetch notifications_cursor into notification_id, operation_date, message;
        exit when notifications_cursor%notfound;
        dbms_output.put_line(notification_id || ' ' || operation_date || ' ' || message);
    end loop;
    close notifications_cursor;
end;

-- test remove notification
begin
    admin_package.remove_admin_notification(1);
end;

-- test get driver rating
declare
    v_id      drivers_ratings.id%type;
    v_rating  drivers_ratings.rating%type;
    v_message drivers_ratings.message%type;
    v_cursor  sys_refcursor;
begin
    v_cursor := admin_package.get_driver_rating('driver1');
    loop
        fetch v_cursor into v_id, v_rating, v_message;
        exit when v_cursor%notfound;
        dbms_output.put_line(v_id || ' ' || v_rating || ' ' || v_message);
    end loop;
    close v_cursor;
end;

-- test confirm driver rating
begin
    admin_package.confirm_driver_rating('driver1');
end;

-- test remove driver rating
begin
    admin_package.remove_driver_rating(3);
end;

-- test get users pagination
declare
    user_username  users.username%type;
    user_firstname users.first_name%type;
    user_lastname  users.last_name%type;
    user_telephone users.telephone%type;
    v_cursor       sys_refcursor;
begin
    v_cursor := admin_package.get_all_users_pagination(1, 15);
    loop
        fetch v_cursor into user_username, user_firstname, user_lastname, user_telephone;
        exit when v_cursor%notfound;
        dbms_output.put_line(user_username || ' ' || user_firstname || ' ' || user_lastname || ' ' ||
                             user_telephone);
    end loop;
    close v_cursor;
end;

-- test export orders
begin
    admin_package.export_orders(to_date('2022-12-07', 'yyyy-mm-dd'), to_date('2023-01-31', 'yyyy-mm-dd'));
end;