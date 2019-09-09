package com.hyp.demo.blogic.java;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyp.demo.blogic.restDto.DemoReqt;
import com.hyp.demo.blogic.restDto.DemoResp;
import com.hyp.demo.blogic.restDto.DemoRespS01;
import com.hyp.demo.blogic.sqlDto.DemoSQL01IM;
import com.hyp.demo.blogic.sqlDto.DemoSQL01OM;
import com.hyp.demo.dao.QueryDao;
import com.hyp.demo.service.BizLogic;
import com.hyp.demo.utils.BeanCopierEx;


/**
 * 测试Mybatis访问数据的效果
 * @author Admin_Vip
 *
 */
@Service("DemoBLogic")
@Transactional(rollbackFor = Exception.class)
public class DemoBLogic implements BizLogic<DemoReqt, DemoResp> {
    
    private static final Logger log = LoggerFactory.getLogger(DemoBLogic.class);
    
    @Autowired
    private QueryDao queryDao;
    
    @Override
    public DemoResp execute(DemoReqt arg0) throws Exception {
        log.debug("DemoBLogic start..{}", arg0);
        DemoResp result = new DemoResp();
        ArrayList<DemoRespS01> demoList = new ArrayList<DemoRespS01>();
        DemoSQL01IM demoSQL01IM = new DemoSQL01IM();
        BeanCopierEx.copy(arg0, demoSQL01IM);
        // 如果demoId 不是空,则进行id查询
        if (StringUtils.isNotBlank(demoSQL01IM.getId())) {
            DemoSQL01OM mybatisDemo = queryDao.executeForObject("DemoSQL01", demoSQL01IM, DemoSQL01OM.class);
            DemoRespS01 demo = new DemoRespS01();
            BeanCopierEx.copy(mybatisDemo, demo);
            demoList.add(demo);
        } else {
            List<DemoSQL01OM> mybatisDemoList = queryDao.executeForObjectList("DemoSQL01", DemoSQL01OM.class);
            demoList = BeanCopierEx.copy(mybatisDemoList, DemoRespS01.class);
        }
        result.setMybatisDemoList(demoList);
        return result;
    }

}
