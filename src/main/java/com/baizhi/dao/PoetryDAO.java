package com.baizhi.dao;

import com.baizhi.entity.Poetry;

import java.util.List;

public interface PoetryDAO {

    //查询所有唐诗
    public List<Poetry> findAll();
}
