-- 创建用户
CREATE USER username IDENTIFIED BY 'password';

-- 删除用户
DROP USER username@localhost;

-- 授权
-- 所有数据库 所有权限
GRANT ALL PRIVILEGES ON *.* TO 'username'@'localhost' IDENTIFIED BY 'password';

-- 语法: grant 权限[ALL PRIVILIGES(所有权限)|SELECT|UPDATE|DELETE] on 数据库.表明 to 用户名@登录主机名[%为所有主机] identified by 密码

-- 删除授权
REVOKE ALL PRIVILEGES ON *.* FROM 'username'@'localhost';

-- 刷新权限表
FLUSH PRIVILIGES;

-- 查看所有授权
SHOW GRANTS;
-- 查看指定用户权限
SHOW GRANTS FOR ROOT;