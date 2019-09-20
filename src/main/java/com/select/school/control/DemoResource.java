package com.select.school.control;

import com.select.school.model.dto.DemoReqt;
import com.select.school.model.dto.DemoResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.select.school.service.BizLogic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RequestMapping("/api")
@Api(value = "DemoResource")
@RestController
public class DemoResource {

private static final Logger log = LoggerFactory.getLogger(DemoResource.class);
    
    /**
     * 测试restFul的接口
     */
    @Autowired
    @Qualifier("DemoBLogic")
    private final BizLogic<DemoReqt, DemoResp> demoBLogic = null;
    
    /**
     * Get  /demo/{id} : 测试功能
     */
    @GetMapping("/demo/{id}")
    @ApiOperation(value="测试功能", notes="测试功能")
    public ResponseEntity<DemoResp> testDemo(@PathVariable String id) throws Exception {
        log.debug("测试restful功能Id : {}", id);
        DemoReqt demoReqt = new DemoReqt();
        demoReqt.setId(id);
        DemoResp result = demoBLogic.execute(demoReqt);
        return new ResponseEntity<DemoResp>(result, HttpStatus.OK);
    }
    
    /**
     * Post  /demo :批量新增的接口
     */
//    @PostMapping("/demo")
//    @ApiOperation(value="批量新增的接口", notes="批量新增的接口")
//    public ResponseEntity<DemoResp> testDemo(@ApiParam @RequestBody List<DemoReqt> demoReqtList) throws Exception {
//        log.debug("测试demo批量新增的功能的参数 : {}", demoReqtList);
//        
//        DemoResp result = demoBLogic.execute(demoReqtList.get(0));
//        
//        return new ResponseEntity<DemoResp>(result, HttpStatus.OK);
//    }
}
