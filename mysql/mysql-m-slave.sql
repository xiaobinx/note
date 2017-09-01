/*
1.主数据库
	1.1 备份数据库，到从数据库还原
		将主服务器的test库导出sql文件，并将sql文件拷贝到从服务器
		mysqldump -u root -p test >/home/test.sql
		scp test.sql root@192.16.41.7:/home/test.sql

	1.2 在master服务器上，创建专用备份账户：
		GRANT REPLICATION SLAVE,RELOAD,SUPER ON *.* TO 'backuser'@'%' IDENTIFIED BY 'q123456';

	1.3 修改配置文件
		1.3.1 备份配置文件
			cp /etc/mysql/mysql.conf.d/mysqld.cnf /etc/mysql/mysql.conf.d/mysqld.cnf.backup
		1.3.2 修改配置文件在[mysqld]下加入下面的内容，然后重启服务
			# 启用二进制日志
			# mysql id主从唯一
			log-bin=mysql-bin
			server-id=0
			
			# 可选
			binlog-do-db=testdb		#允许复制的数据库名称，一行代表一个数据库
			binlog-ignore-db=mysql	#不复制的数据库
			
	1.4 用show master status记录file,position字段
	+------------------+----------+--------------+------------------+-------------------+
	| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
	+------------------+----------+--------------+------------------+-------------------+
	| mysql-bin.000001 |      154 |              |                  |                   |
	+------------------+----------+--------------+------------------+-------------------+

查看主服务器状态：
show slave master;

2.slave数据库
	2.1 创建数据库，还原主数据库的备份
	2.2 修改配置文件
		2.2.1 备份配置文件
			cp /etc/mysql/mysql.conf.d/mysqld.cnf /etc/mysql/mysql.conf.d/mysqld.cnf.backup
		2.2.2 修改配置文件在[mysqld]下加入下面的内容，然后重启服务
			# mysql id主从唯一
			# 启用二进制日志[可选]
			log-bin=mysql-bin
			server-id=0
	2.3 关闭slave同步进程
		stop slave;
	2.4 指定主数据库
		change master to master_host='mysql1.xiaobin',master_user='backuser',master_password='q123456',master_log_file='mysql-bin.000001',master_log_pos=154;
	2.5 启动slave同步进程
		start slave;
	2.6 查看从数据库信息 
		show slave status \G;
		
		以下两个信息必须为Yes,否则查日志排，重复排错 2.3~2.6
		Slave_IO_Running: Yes
		Slave_SQL_Running: Yes
		
PS: 如果是通过虚拟级复制的从数据库，有可能因为主从数据库的uuid相同IO通道无法打开，这个时候只要找到从数据库的auto.cnf(应该在/var/lib/mysql/auto.cnf)删掉或者重命名即可。
*/