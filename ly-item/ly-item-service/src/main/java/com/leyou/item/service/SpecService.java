package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.entity.SpecParam;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroupDTO> queryGroupByCategory(Long id) {

        //查询规格组
        SpecGroup s = new SpecGroup();

        s.setCid(id);

        List<SpecGroup> list = specGroupMapper.select(s);

        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        //对象转换
        return BeanHelper.copyWithCollection(list,SpecGroupDTO.class);
    }



    public List<SpecParamDTO> querySpecParams(Long gid) {

        SpecParam s = new SpecParam();

        s.setGroupId(gid);

        List<SpecParam> list = specParamMapper.select(s);

        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);

        }

        return BeanHelper.copyWithCollection(list,SpecParamDTO.class);


    }
}
