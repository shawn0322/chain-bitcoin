package com.tunion.dubbo.chainrouter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tunion.cores.result.Results;
import com.tunion.cores.tools.cache.JedisUtils;
import com.tunion.cores.utils.CommConstants;
import com.tunion.cores.utils.JacksonUtil;
import com.tunion.cores.utils.StringUtil;
import com.tunion.dubbo.IService.chainweb.IDubboChainWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Think on 2018/2/1.
 */
@Component
public class CoinReceivedNotifyService {
    private static Logger logger = LoggerFactory.getLogger(CoinReceivedNotifyService.class);

//    @Autowired(required = false)
    @Reference
    private IDubboChainWeb dubboChainWeb;

    public Results notifyCoinRecevied(String accoutAddress, int accoutType, String txid, String amount) {
        Results results = null;
        try {
            //查询一下地址所对应的账号信息
            String accoutName = JedisUtils.getObjectByRawkey(CommConstants.MANUFACTOR_TYPE.index(accoutType).name()+accoutAddress);

            logger.info("accoutAddress:{},accoutName:{},amount:{}",accoutAddress,accoutName,amount);

            if(StringUtil.isNullStr(accoutName))
            {
                return results;
            }

            results = dubboChainWeb.notifyCoinRecevied(accoutName,accoutAddress,accoutType,txid,amount);

            logger.info("results:"+ JacksonUtil.getJackson(results));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return results;
    }
}
