create or replace package admin_package as
    function evaluate_order_price(order_id_in in orders.id%type) return number;
    procedure update_order_price(order_id_in in orders.id%type);
    procedure accept_order(order_id_in in orders.id%type);
    function encrypt_password(password in varchar2) return drivers.password_hash%type;
    procedure hire_driver(username_in in drivers.username%type, driver_password in varchar2);
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

end admin_package;

-- test update order price
begin
    admin_package.update_order_price(2);
end;

-- test accept order
begin
    admin_package.accept_order(2);
end;

-- test hire driver
begin
    admin_package.hire_driver('driver1', 'password');
end;