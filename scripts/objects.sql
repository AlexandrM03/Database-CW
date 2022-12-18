-- triggers --
create or replace trigger notify_delete_order
    for delete
    on orders
    compound trigger
    message varchar2(1000);
    v_user_id number;
before each row is
begin
    message := 'Order ' || :old.start_point || '-->' || :old.end_point || ' rejected';
    v_user_id := :old.user_id;
end before each row;
    after statement is
    begin
        if message is not null then
            insert into user_notifications (operation_date, message, user_id) values (sysdate, message, v_user_id);
        end if;
    end after statement;
    end notify_delete_order;


create or replace trigger notify_insert_order
    for insert
    on orders
    compound trigger
    message varchar2(1000);
before each row is
begin
    message := 'Order ' || :new.start_point || '-->' || :new.end_point || ' created';
end before each row;
    after statement is
    begin
        if message is not null then
            insert into admin_notifications(operation_date, message) values (sysdate, message);
        end if;
    end after statement;
    end notify_insert_order;

-- triggers
select name
from cities
where sdo_relate(point, (select boundary from boundaries where name = 'Latvia'), 'mask=anyinteract') = 'TRUE';

create index cities_spatial_index
    on cities (point)
    indextype is mdsys.spatial_index;

create index boundaries_spacial_index
    on boundaries (boundary)
    indextype is mdsys.spatial_index;

create index roads_spatial_index
    on roads (line)
    indextype is mdsys.spatial_index;

declare
    v_login    varchar2(50);
    v_password varchar2(50);
begin
    for i in 1..100000
        loop
            v_login := 'test' || i;
            v_password := 'qwerty123';
            user_package.register_user(v_login, v_password);
        end loop;
end;