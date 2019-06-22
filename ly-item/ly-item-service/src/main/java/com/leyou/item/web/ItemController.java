package com.leyou.item.web;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.service.ItemService;
import dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("item")
    public ResponseEntity<Item> saveItem(Item item){
        if (item.getPrice() == null){
//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            throw new LyException(ExceptionEnum.PRICE_CANNOT_BE_NULL);
        }

        Item result = itemService.saveItem(item);
        //return ResponseEntity.status(HttpStatus.CREATED).body(result);
        return ResponseEntity.ok(result);
    }
}
