# redis 分布式锁实现 介绍

1. 集成了springboot的redis框架，手写springboot starter项目。
2. 项目下载下来，使用maven install构建此项目
3. pom.xml
```
<dependency>
    <groupId>com.maike.winter</groupId>
    <artifactId>redis-distribute-lock-spring-boot-starter</artifactId>
    <version>3.0.1-SNAPSHOT</version>
</dependency>
```
4. application.yml 配置

```
spring.redis:
  host: localhsot
  port: 6379
  database: 14
  
redis.distribute:
    expired-time: 10
    lock-pre: maike
    retry-count: 1
```
5. 使用方式
注入方式

```
 @Autowired
    private ILockManager iLockManager;


    @Test
    public void  test1(){
        iLockManager.callBack("key1", new LockRedisReturnCallBack<Object>() {
            @Override
            public Object execute() {
                System.out.println(11111);
                return 0;
            }
        });
    }
```
