package com.select.school.service.wxApplet.impl;

import com.select.school.mapper.OptionMapper;
import com.select.school.model.entity.Option;
import com.select.school.service.wxApplet.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionImpl implements OptionService {

    @Autowired
    private OptionMapper optionMapper;

    @Override
    public List<Option> selectOne(){
        return optionMapper.selectOne();
    }
}
