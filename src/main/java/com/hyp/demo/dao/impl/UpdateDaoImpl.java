package com.hyp.demo.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hyp.demo.dao.UpdateDao;


public class UpdateDaoImpl extends SqlSessionDaoSupport implements UpdateDao {
    static Log log = LogFactory.getLog(UpdateDaoImpl.class);

    public int execute(String sqlID, Object bindParams) {
        if (log.isDebugEnabled()) {
            log.debug("execute Start.");
        }

        SqlSession sqlSession = this.getSqlSession();
        int row = sqlSession.update(sqlID, bindParams);
        if (log.isDebugEnabled()) {
            log.debug("execute End. success count:" + row);
        }

        return row;
    }
}
