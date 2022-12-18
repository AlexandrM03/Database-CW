create or replace package driver_package as
    function encrypt_password(password in varchar2) return users.password_hash%type;
    function login_driver(username_in in varchar2, password in varchar2) return sys_refcursor;
    procedure fulfill_driver_data(username_in in drivers.username%type, first_name_in in drivers.first_name%type,
                                  last_name_in in drivers.last_name%type, telephone_in in drivers.telephone%type);
    function get_accepted_orders return sys_refcursor;
    function get_order_items(order_id_in in orders.id%type) return sys_refcursor;
    function get_driver_data(driver_username_in in drivers.username%type) return sys_refcursor;
    procedure start_order(order_id_in in orders.id%type, driver_username_in in drivers.username%type);
    procedure finish_order(order_id_in in orders.id%type);
    function get_driver_orders(driver_username_in in drivers.username%type) return sys_refcursor;
    procedure update_driver_password(driver_username_in in drivers.username%type, new_password in varchar2);
end driver_package;

create or replace package body driver_package as
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

    -- 2. Function to login driver and return cursor with username, password, and 'driver'
    function login_driver(username_in in varchar2, password in varchar2) return sys_refcursor is
        v_password_hash drivers.password_hash%type;
        count_drivers   number;
        drivers_cursor  sys_refcursor;
    begin
        v_password_hash := encrypt_password(password);
        select count(*)
        into count_drivers
        from drivers
        where username = username_in
          and password_hash = v_password_hash;
        if count_drivers = 1 then
            open drivers_cursor for
                select username, password_hash, 'driver'
                from drivers
                where username = username_in
                  and password_hash = v_password_hash;
            return drivers_cursor;
        else
            raise_application_error(-20002, 'Invalid username or password');
        end if;
    end login_driver;

    -- 3. Procedure to fulfill driver data
    procedure fulfill_driver_data(username_in in drivers.username%type, first_name_in in drivers.first_name%type,
                                  last_name_in in drivers.last_name%type, telephone_in in drivers.telephone%type) is
        drivers_count number;
    begin
        select count(*) into drivers_count from drivers where username = username_in;
        if drivers_count = 1 then
            update drivers
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
    end fulfill_driver_data;

    -- 4. Function to get accepted orders
    function get_accepted_orders return sys_refcursor is
        v_cursor sys_refcursor;
    begin
        open v_cursor for
            select o.id          as id,
                   o.start_point as start_point,
                   o.end_point   as end_point,
                   o.price       as price,
                   o.start_date  as start_date,
                   os.status     as order_status,
                   u.username    as username
            from orders o
                     join order_statuses os on o.order_status_id = os.id
                     join users u on o.user_id = u.id
            where os.status = 'accepted';
        return v_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_accepted_orders;

    -- 5. Function to get order items
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

    -- 6. Function to get driver data
    function get_driver_data(driver_username_in in drivers.username%type) return sys_refcursor is
        driver_cursor sys_refcursor;
    begin
        open driver_cursor for
            select username,
                   first_name,
                   last_name,
                   telephone,
                   rating
            from drivers
            where username = driver_username_in;
        return driver_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_driver_data;

    -- 7. Procedure to start order
    procedure start_order(order_id_in in orders.id%type, driver_username_in in drivers.username%type) is
        v_driver_id drivers.id%type;
        v_order_status_id number;
    begin
        select id into v_driver_id from drivers where username = driver_username_in;
        select id into v_order_status_id from order_statuses where status = 'in_progress';
        update orders
        set order_status_id = v_order_status_id, driver_id = v_driver_id
        where id = order_id_in;
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end start_order;

    -- 8. Procedure to finish order
    procedure finish_order(order_id_in in orders.id%type) is
        v_order_status_id number;
    begin
        select id
        into v_order_status_id
        from order_statuses
        where status = 'completed';
        update orders
        set order_status_id = v_order_status_id, end_date = sysdate
        where id = order_id_in;
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end finish_order;

    -- 9. Function to get driver orders
    function get_driver_orders(driver_username_in in drivers.username%type) return sys_refcursor is
        driver_id_in drivers.id%type;
        driver_orders_cursor sys_refcursor;
    begin
        select id
        into driver_id_in
        from drivers
        where username = driver_username_in;
        open driver_orders_cursor for
            select o.id          as id,
                   o.start_point as start_point,
                   o.end_point   as end_point,
                   o.price       as price,
                   o.start_date  as start_date,
                   o.end_date    as end_date,
                   os.status     as order_status
            from orders o
                     join order_statuses os on o.order_status_id = os.id
            where o.driver_id = driver_id_in;
        return driver_orders_cursor;
    exception
        when others then
            raise_application_error(sqlcode, sqlerrm);
    end get_driver_orders;

    -- 10. Procedure to update driver password
    procedure update_driver_password(driver_username_in in drivers.username%type, new_password in varchar2) is
        v_password_hash varchar2(100);
    begin
        v_password_hash := encrypt_password(new_password);
        update drivers
        set password_hash = v_password_hash
        where username = driver_username_in;
        commit;
    exception
        when others then
            rollback;
            raise_application_error(sqlcode, sqlerrm);
    end update_driver_password;
end driver_package;

-- test login --
declare
    v_username drivers.username%type;
    v_password drivers.password_hash%type;
    v_role     varchar2(10);
    v_cursor   sys_refcursor;
begin
    v_cursor := driver_package.login_driver('driver1', 'password');
    loop
        fetch v_cursor into v_username, v_password, v_role;
        exit when v_cursor%notfound;
        dbms_output.put_line(v_username || ' ' || v_password || ' ' || v_role);
    end loop;
end;

-- test fulfill driver data --
begin
    driver_package.fulfill_driver_data('driver1', 'John', 'Doe', '1234567890');
end;

-- test get accepted orders --
declare
    v_cursor sys_refcursor;
    v_id     orders.id%type;
    v_start  orders.start_point%type;
    v_end    orders.end_point%type;
    v_price  orders.price%type;
    v_start_date orders.start_date%type;
    v_status order_statuses.status%type;
    v_user   users.username%type;
begin
    v_cursor := driver_package.get_accepted_orders;
    loop
        fetch v_cursor into v_id, v_start, v_end, v_price, v_start_date, v_status, v_user;
        exit when v_cursor%notfound;
        dbms_output.put_line(v_id || ' ' || v_start || ' ' || v_end || ' ' || v_price || ' ' || v_status || ' ' ||
                             v_user);
    end loop;
end;

-- test get order items --
declare
    v_cursor sys_refcursor;
    v_name   order_items.item_name%type;
    v_weight order_items.item_weight%type;
    v_volume order_items.item_volume%type;
begin
    v_cursor := driver_package.get_order_items(21);
    loop
        fetch v_cursor into v_name, v_weight, v_volume;
        exit when v_cursor%notfound;
        dbms_output.put_line(v_name || ' ' || v_weight || ' ' || v_volume);
    end loop;
end;

-- test get driver data --
declare
    v_cursor sys_refcursor;
    v_username drivers.username%type;
    v_first    drivers.first_name%type;
    v_last     drivers.last_name%type;
    v_telephone drivers.telephone%type;
    v_rating   drivers.rating%type;
begin
    v_cursor := driver_package.get_driver_data('driver1');
    loop
        fetch v_cursor into v_username, v_first, v_last, v_telephone, v_rating;
        exit when v_cursor%notfound;
        dbms_output.put_line(v_username || ' ' || v_first || ' ' || v_last || ' ' || v_telephone || ' ' || v_rating);
    end loop;
end;

-- test start order --
begin
    driver_package.start_order(81, 'driver1');
end;

-- test finish order --
begin
    driver_package.finish_order(81);
end;

-- test get driver orders --
declare
    v_cursor sys_refcursor;
    v_id     orders.id%type;
    v_start  orders.start_point%type;
    v_end    orders.end_point%type;
    v_price  orders.price%type;
    v_start_date orders.start_date%type;
    v_end_date orders.end_date%type;
    v_status order_statuses.status%type;
begin
    v_cursor := driver_package.get_driver_orders('driver1');
    loop
        fetch v_cursor into v_id, v_start, v_end, v_price, v_start_date, v_end_date, v_status;
        exit when v_cursor%notfound;
        dbms_output.put_line(v_id || ' ' || v_start || ' ' || v_end || ' ' || v_price || ' ' || v_start_date || ' ' ||
                             v_end_date || ' ' || v_status);
    end loop;
end;

-- test update driver password --
begin
    driver_package.update_driver_password('driver1', 'qwerty11');
end;