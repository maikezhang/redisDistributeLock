package com.miake.winter.redis.manage;

import com.miake.winter.redis.config.LockProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author maike
 * @version 1.0.0
 * @date 2020-01-06 09:24
 */
@Component
public class LuaDistributedLock implements ILock, InitializingBean {


    @Autowired
    private RedisTemplate<String,String> strRedisTemplate;
    private DefaultRedisScript<Long> lockScript;
    private DefaultRedisScript<Long> unlockScript;
    private ThreadLocal<String> threadKeyId = new ThreadLocal<String>() {
        protected String initialValue() {
            return UUID.randomUUID().toString().replace("-", "");
        }
    };


    private void initialize(){
        this.lockScript = new DefaultRedisScript();
        this.lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock/lock.lua")));
        this.lockScript.setResultType(Long.class);
        this.unlockScript = new DefaultRedisScript();
        this.unlockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock/unlock.lua")));
        this.unlockScript.setResultType(Long.class);

    }

    @Override
    public String lock(String lockKey, LockProperties properties) {
        if(Objects.isNull(lockKey)){
//            throw new CustomerException("lock key can't be null");
        }
        String key=getLockKey(lockKey,properties);
        List<String> keyList = new ArrayList();
        keyList.add(key);
        keyList.add(this.threadKeyId.get());
        int lockTryCount = 0;

        for(; properties.getRetryCount() <= 0 || lockTryCount <= properties.getRetryCount(); ++lockTryCount) {
            if (lockTryCount > 0) {
                System.out.println("重试获取锁"+key+"+操作:"+lockTryCount+"次");
            }

            if ((Long)this.strRedisTemplate.execute(this.lockScript, keyList, new Object[]{String.valueOf(properties.getExpiredTime() * 1000)}) > 0L) {
                System.out.println("加锁成功，lockKey:"+key+",准备执行业务操作");
                return key;
            }

            try {
                Thread.sleep(10L, (int)(Math.random() * 500.0D));
            } catch (InterruptedException var7) {
                throw new RuntimeException(var7);
            }
        }

        throw new RuntimeException("access to distributed lock more than retries：" + properties.getRetryCount());
    }

    @Override
    public String unLock(String lockKey, LockProperties properties) {
        String key = this.getLockKey(lockKey, properties);
        List<String> keyList = new ArrayList();
        keyList.add(key);
        keyList.add(this.threadKeyId.get());
        this.strRedisTemplate.execute(this.unlockScript, keyList, new Object[0]);
        System.out.println("成功释放锁，lockKey:"+lockKey);
        return lockKey;
    }

    private String getLockKey(String lock, LockProperties lockProperties) {
        if (StringUtils.isEmpty(lockProperties.getLockPre())) {
            return lock;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(lockProperties.getLockPre()).append(lock);
            return sb.toString();
        }
    }

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an
     *                   essential property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.initialize();
    }
}
