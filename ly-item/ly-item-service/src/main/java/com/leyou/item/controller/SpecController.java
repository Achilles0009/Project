package com.leyou.item.controller;

import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecController {

    /**
     * 根据商品分类查询规格组
     * @param id 商品分类id
     * @return 规格组集合
     */

    @Autowired
    private SpecService specService;

    @GetMapping("/groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> queryGroupByCategory(@RequestParam("id") Long id){
        return ResponseEntity.ok(specService.queryGroupByCategory(id));
    }

    /**
     * 根据规格组id查询规格参数
     * @param gid
     * @return 规格组集合
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParamDTO>> querySpecParams(@RequestParam("gid") Long gid) {
        return ResponseEntity.ok(specService.querySpecParams(gid));
    }
}
