/**
 * projectName: selectSchool
 * fileName: SqlParameter.java
 * packageName: com.select.school.utils.dxm.sqlUtils
 * date: 2019-09-25
 * copyright(c) 2017-2020 德慧公司
 */
package com.select.school.utils.dxm.sqlUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @version: V1.0
 * @author: DongXiaoMing
 * @className: SqlParameter
 * @packageName: com.select.school.utils.dxm.sqlUtils
 * @description:
 * @data: 2019-09-25
 **/
public class SqlParameter implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> paramMap = new HashMap();

    private SqlParameter() {
    }

    public static SqlParameter getParameter() {
        return new SqlParameter();
    }

    public SqlParameter addLimit(int pageNo, int pageSize) {
        this.paramMap.put("offset", (pageNo - 1) * pageSize);
        this.paramMap.put("rows", pageSize);
        return this;
    }

    public SqlParameter addOrder(String field, Order o) {
        if (field != null && o != null && !"null".equals(field + "")) {
            this.paramMap.put("order_by_column", field);
            this.paramMap.put("order_by", o.getCode());
            return this;
        } else {
            return this;
        }
    }

    public SqlParameter addQuery(String key, Object value) {
        this.paramMap.put(key, value);
        return this;
    }

    public SqlParameter addUpdate(String key, Object value) {
        this.paramMap.put("update" + (key.charAt(0) + "").toUpperCase() + key.substring(1, key.length()), value);
        return this;
    }

    public SqlParameter clear() {
        this.paramMap.clear();
        return this;
    }

    public Map<String, Object> getMap() {
        return this.paramMap;
    }
}
