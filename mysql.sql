

show variables like '%log_queries_not_using_indexes%';
show variables like '%long_query_time%';
show variables like '%slow_query_log%';
-- log_queries_not_using_indexes 没有使用索引的查询 long_query_time 耗时查询
set global log_queries_not_using_indexes=on;
set  long_query_time=10;

-- 查看字符集设置
show variables like 'collation_%';

show variables like 'character_set_%';

set character_set_database=utf8;
set character_set_server=utf8;