-- 查看字符集设置
show variables like 'collation_%';

show variables like 'character_set_%';

set character_set_database=utf8;
set character_set_server=utf8;