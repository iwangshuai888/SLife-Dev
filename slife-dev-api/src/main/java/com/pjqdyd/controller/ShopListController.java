package com.pjqdyd.controller;

import com.pjqdyd.pojo.Shop;
import com.pjqdyd.pojo.vo.ShopItemVO;
import com.pjqdyd.pojo.vo.ShopListVO;
import com.pjqdyd.result.ResponseResult;
import com.pjqdyd.service.ShopListService;
import com.pjqdyd.service.ShopService;
import com.pjqdyd.utils.DistanceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**   
 * @Description:  [店铺列表controller层]
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */
@Slf4j
@Api(value = "店铺列表Controller层", tags = "查询店铺列表")
@CrossOrigin
@RestController
@RequestMapping("/slife/shopList")
public class ShopListController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopListService shopListService;

    /**
     * 获取附近的商铺
     * @param latitude 用户所在位置的纬度
     * @param longitude 用户所在位置的经度
     * @param page 当前要查询的页数
     * @param size 当前要查询的数据条数
     * @return 包含
     */
    @ApiOperation(value = "查询附近的商铺", tags = "主页查询附近商铺")
    @GetMapping("/localShop")
    public ResponseResult getLocalShopList(@RequestParam("latitude") Double latitude,
                                           @RequestParam("longitude") Double longitude,
                                           @RequestParam("page") Integer page,
                                           @RequestParam(value = "size", defaultValue = "5") Integer size){

        Sort sort = new Sort(Sort.Direction.DESC, "rate"); //按评分高底排序
        Pageable pageable = PageRequest.of(page-1, size, sort); //分页查询第page-1页的5条数据

        //查询附近0.1经纬度度内, 也就是11.1公里范围内的店铺
        Page<Shop> shopPage = shopListService.findLocalShop(
                latitude-0.1,latitude+0.1,
                longitude-0.1,longitude+0.1,
                1,pageable);

        //设置数据给前端
        ShopListVO shopListVO = new ShopListVO();
        shopListVO.setPage(page);
        shopListVO.setTotal((int)shopPage.getTotalElements());
        shopListVO.setTotalPage(shopPage.getTotalPages());

        List<Shop> shopList = shopPage.getContent();

        List<ShopItemVO> shopItemVOS = new ArrayList<>(); //用来存放返回给前端的ShopItemVO
        for (Shop shop: shopList) {
            ShopItemVO shopItemVO = new ShopItemVO();
            BeanUtils.copyProperties(shop, shopItemVO);
            shopItemVO.setDistance(DistanceUtil.getDistance(longitude,latitude, //设置店铺距离
                    shop.getShopLongitude(),shop.getShopLatitude()));
            shopItemVOS.add(shopItemVO); //添加店铺数据
        }
        shopListVO.setLocalList(shopItemVOS);
        return ResponseResult.success(shopListVO);
    }

}
