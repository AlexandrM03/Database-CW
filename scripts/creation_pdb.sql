alter
session set "_ORACLE_SCRIPT"= true;

create
tablespace course_TS datafile 'cw.dbf'
    size 100 m autoextend on next 10 m
    blocksize 8192
    logging
    online
    segment space management auto;

create
pluggable database course_pdb
    admin user programmer identified by qwerty11
    storage (maxsize 4 G)
    default tablespace course_TS
        datafile 'cw.dbf' size 100 m autoextend on
    file_name_convert = ('pdbseed','course_pdb');

select name, open_mode
from v$pdbs;

alter
pluggable database course_pdb open;

alter
pluggable database course_pdb save state;
