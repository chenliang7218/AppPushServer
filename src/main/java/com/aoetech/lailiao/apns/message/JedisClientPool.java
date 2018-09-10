package com.aoetech.lailiao.apns.message;

import com.aoetech.lailiao.apns.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;


/**
 * Created by HASEE on 2017/7/11.
 */
public class JedisClientPool {
    private static Logger logger = LoggerFactory.getLogger(JedisClientPool.class);
    //Redis服务器IP
    private static String ADDR;

    //Redis的端口号
    private static int PORT;


    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT;

    private static int TIMEOUT;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;
    private static int EXPIRE_TIME = 2 * 24 * 3600;

    /**
     * 初始化Redis连接池
     */
    static {
        try {
            Properties prop = new Properties();
            File file = new File(System.getProperty("user.dir") + File.separator + Main.CONFIG_FILE_NAME);
            FileInputStream stream = new FileInputStream(file);
            prop.load(stream);
            ADDR = prop.getProperty("redis.hosts");
            PORT = Integer.parseInt(prop.getProperty("redis.port"));
            MAX_ACTIVE = Integer.parseInt(prop.getProperty("redis.max.total"));
            MAX_IDLE = Integer.parseInt(prop.getProperty("redis.max.idle"));
            MAX_WAIT = Integer.parseInt(prop.getProperty("redis.max.wait"));
            TIMEOUT = Integer.parseInt(prop.getProperty("redis.time.out"));
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT,TIMEOUT);
            stream.close();
        } catch (Exception e) {
            logger.error("init :" + e.toString());
        }
    }


    /**
     * 向缓存中设置字符串内容
     * @param key key
     * @param value value
     * @return
     * @throws Exception
     */
    public static boolean  set(String key,String value) throws Exception{
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key,EXPIRE_TIME);
            return true;
        } catch (Exception e) {
            logger.error("jedis set error:" + e.toString());
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }


    /**
     * 删除缓存中得对象，根据key
     * @param key
     * @return
     */
    public static boolean del(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
            return true;
        } catch (Exception e) {
            logger.error("jedis del error:" + e.toString());
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 根据key 获取内容
     * @param key
     * @return
     */
    public static Object get(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Object value = jedis.get(key);
            logger.info("key: " + key + " ,last time :" + jedis.ttl(key));
            return value;
        } catch (Exception e) {
            logger.error("jedis get error:" + e.toString());
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }


    /**
     * 根据key 获取内容
     * @param key
     * @return
     */
    public static String pop(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.lpop(key);
            return value;
        } catch (Exception e) {
            logger.error("jedis get error:" + e.toString());
            return "";
        }finally{
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 根据key 获取内容
     * @param key
     * @return
     */
    public static List<String> blpop(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> value = jedis.blpop(0,key);
            return value;
        } catch (Exception e) {
            logger.error("jedis get error:" + e.toString());
            return null;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }

    public static void repush(String key,String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.rpush(key,value);
        } catch (Exception e) {
            logger.error("jedis get error:" + e.toString());
        }finally{
            jedisPool.returnResource(jedis);
        }
    }



    public static boolean isExist(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            logger.info("key: " + key + " ,last time :" + jedis.ttl(key));
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("jedis exist error:" + e.toString());
            return false;
        }finally{
            jedisPool.returnResource(jedis);
        }
    }

}
