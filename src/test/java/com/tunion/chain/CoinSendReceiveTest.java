package com.tunion.chain;

import com.tunion.cores.BaseTest;
import com.tunion.cores.result.Results;
import com.tunion.cores.utils.JacksonUtil;
import com.tunion.dubbo.IService.chainrouter.IDubboChainRouter;
import org.bitcoinj.core.Coin;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Think on 2018/2/28.
 */
public class CoinSendReceiveTest extends BaseTest{
    private static Logger logger = LoggerFactory.getLogger(CoinSendReceiveTest.class);

    @Autowired(required = false)
    private IDubboChainRouter iDubboChainRouter;

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
    }
}
