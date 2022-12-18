create table json_documents
(
    id raw(16),
    data clob,
    constraint json_documents_pk primary key (id),
    constraint json_documents_json_chk check (data is json)
);

-- json directory --
create directory json_dir as 'C:\app\Administrator\admin\orcl\json_dir';

select *
from dba_directories;

-- 1. Boundaries --
insert into json_documents (id, data)
values (sys_guid(), bfilename('JSON_DIR', 'europe.geojson'));

select json_value(data, '$.features[0].geometry'
                  returning sdo_geometry
                  error on error)
from json_documents;

insert into boundaries (name, boundary)
select jt.name, jt.sdo_val
from json_documents j nested data.features[*]
    columns (name varchar2(50) path '$.properties.NAME',
             sdo_val sdo_geometry path '$.geometry') jt;
commit;

-- 2. Cities --
insert into json_documents (id, data)
values (sys_guid(), bfilename('JSON_DIR', 'lithLaPoCities.geojson'));

select json_value(data, '$.features[0].geometry'
                  returning sdo_geometry
                  error on error)
from json_documents;

insert into cities (name, point)
select jt.name, jt.sdo_val
from json_documents j nested data.features[*]
    columns (name varchar2(50) path '$.properties.ascii_name',
             sdo_val sdo_geometry path '$.geometry') jt
where jt.sdo_val.sdo_gtype = 2001;
commit;

-- 3. Roads --
insert into json_documents (id, data)
values (sys_guid(), bfilename('JSON_DIR', 'ltRoads.geojson'));
insert into json_documents (id, data)
values (sys_guid(), bfilename('JSON_DIR', 'laRoads.geojson'));
insert into json_documents (id, data)
values (sys_guid(), bfilename('JSON_DIR', 'plRoads.geojson'));

select json_value(data, '$.features[0].geometry'
                  returning sdo_geometry
                  error on error)
from json_documents;

insert into roads (line)
select jt.sdo_val
from json_documents j nested data.features[*]
    columns (sdo_val sdo_geometry path '$.geometry') jt;
commit;

-- drop unnecessary table --
drop table json_documents;