package com.hyp.demo.blogic.restDto;

import java.io.Serializable;

public class DemoReqt implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3425847102168704004L;

    private String id;
    
    private String name;
    
    private Integer age;

    /**  
    * 获取id  
    * @return id id  
    */
    public String getId() {
        return id;
    }
    

    /**  
    * 设置id  
    * @param id id  
    */
    public void setId(String id) {
        this.id = id;
    }
    

    /**  
    * 获取name  
    * @return name name  
    */
    public String getName() {
        return name;
    }
    

    /**  
    * 设置name  
    * @param name name  
    */
    public void setName(String name) {
        this.name = name;
    }
    

    /**  
    * 获取age  
    * @return age age  
    */
    public Integer getAge() {
        return age;
    }
    

    /**  
    * 设置age  
    * @param age age  
    */
    public void setAge(Integer age) {
        this.age = age;
    }
}
