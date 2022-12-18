create or replace view v_completed_orders as
    select o.id, o.price, u.username as customer, d.username as driver
    from orders o
    inner join users u on o.user_id = u.id
    left join drivers d on o.driver_id = d.id
    where o.order_status_id = (select id from order_statuses where status = 'completed');

select * from v_completed_orders;

create or replace view v_total_profit as
    select sum(price) as total_profit
    from v_completed_orders;

select * from v_total_profit;

create or replace view v_total_profit_by_driver as
    select driver, sum(price) as total_profit
    from v_completed_orders
    group by driver;

select * from v_total_profit_by_driver;

create or replace view v_total_profit_by_customer as
    select customer, sum(price) as total_profit
    from v_completed_orders
    group by customer;

select * from v_total_profit_by_customer;

create or replace view v_total_weight_and_volume as
    select sum(item_weight) as total_weight, sum(item_volume) as total_volume
    from order_items;

select * from v_total_weight_and_volume;

create or replace view v_total_orders_by_customer as
    select username, count(*) as total_orders
    from orders o
    inner join users u on o.user_id = u.id
    group by username;

select * from v_total_orders_by_customer;

create or replace view v_total_orders_by_driver as
    select username, count(*) as total_orders
    from orders o
    inner join drivers d on o.driver_id = d.id
    group by username;

select * from v_total_orders_by_driver;