package com.tunion.chain;

import com.tunion.chain.bitcoinj.service.BitcoinService;
import com.tunion.cores.BaseTest;
import com.tunion.cores.result.Results;
import com.tunion.cores.tools.cache.JedisUtils;
import com.tunion.cores.utils.JacksonUtil;
import com.tunion.dubbo.IService.chainrouter.IDubboChainRouter;
import org.bitcoinj.core.Coin;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Think on 2018/2/28.
 */
public class CoinSendReceiveTest extends BaseTest{
    private static Logger logger = LoggerFactory.getLogger(CoinSendReceiveTest.class);

    @Autowired(required = false)
    private IDubboChainRouter iDubboChainRouter;

    @Autowired
    private BitcoinService bitcoinService;

    @Test
    public void createAddress()
    {

        try {
            Results results = iDubboChainRouter.createAddress("lzftest3",1);

            logger.info("response data:{}", JacksonUtil.getJackson(results));

        }catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void getBalance()
    {

        try {
            Results results = iDubboChainRouter.getBalance("lzftest3",1);

            logger.info("response data:{}", JacksonUtil.getJackson(results));

        }catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void withdrawalCash()
    {
        try {
            Results results = iDubboChainRouter.withdrawalCash("lzfsend",1,"mnfgdws5XDPUZSgUgHfv3bjd6wK9tHKpLn","0.01","0.002","","");

            logger.info("response data:{}", JacksonUtil.getJackson(results));

        }catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void unitTest()
    {
        Coin amount = Coin.parseCoin("0.1");

        logger.info(amount.toPlainString());

        Map param=new HashMap<String,String>();
        param.put("accoutName","lizf");
        param.put("accoutAddress","1234567890");
        param.put("accoutType","BTC");
        param.put("txid","12345678");
        param.put("amount","90");

//        JedisUtils.pushMessage(JacksonUtil.getJackson(param));

        logger.info(""+JedisUtils.queueSize());

        String msg=JedisUtils.popMessage();
        logger.info(msg);

        Map msgParam = (Map)JacksonUtil.getJacksonObj(msg,Map.class);

        logger.info(""+msgParam.get("accoutAddress"));
    }

    @Test
    public void batchWithdrawal()
    {
        try {

            Map mapTrans = new HashMap();

            mapTrans.put("mmukg2FqRrNU4ZKERv2FcZHicAs8v2hcZF",0.01);
            mapTrans.put("n1oLje7r3kAvjrAyABXf4X5YuVNxrhWVpZ",0.01);

            Results results = bitcoinService.batchWithdrawal(mapTrans,"0.001","batch Transaction");

            logger.info("response data:{}", JacksonUtil.getJackson(results));

        }catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void newAddress()
    {
        Results results = bitcoinService.createAddress("helloworld");

        logger.info("response data:{}", JacksonUtil.getJackson(results));
    }
}
