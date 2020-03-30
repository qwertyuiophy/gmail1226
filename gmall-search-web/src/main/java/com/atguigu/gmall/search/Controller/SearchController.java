package com.atguigu.gmall.search.Controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class SearchController {

    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap){// 三级分类id、关键字、

        // 调用搜索服务，返回搜索结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfos =  searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList",pmsSearchSkuInfos);


        Set<String> valueSet = new HashSet<>();

        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {

            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {

                String valueId = pmsSkuAttrValue.getValueId();
                valueSet.add(valueId);
            }
        }

        List<PmsBaseAttrInfo> pmsBaseAttrInfos= attrService.getAttrValueListByValueId(valueSet);
        modelMap.put("attrList",pmsBaseAttrInfos);


        //对平台属性集合进一步处理，删除当前条件中valueId所在的属性组
        String[] delValueIds = pmsSearchParam.getValueId();
        if(delValueIds != null){
            //面包屑
            List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();

            for (String delValueId : delValueIds) {
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
                //面包屑
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParam(pmsSearchParam, delValueId));

                while (iterator.hasNext()){
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String valueId = pmsBaseAttrValue.getId();
                            if(delValueId.equals(valueId)){
                                //面包屑的属性名
                                pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                                iterator.remove();
                            }
                        }
                    }

                pmsSearchCrumbs.add(pmsSearchCrumb);
                }

            //面包屑
            modelMap.put("attrValueSelectedList",pmsSearchCrumbs);
        }



        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam",urlParam);

        String keyword = pmsSearchParam.getKeyword();
        if(StringUtils.isNotBlank(keyword)){
            modelMap.put("keyword",keyword);
        }



        return "list";
    }



    /*private String getUrlParamForCrumbs(PmsSearchParam pmsSearchParam,String delValueId) {

        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] skuAttrValueList = pmsSearchParam.getValueId();
        String urlParam = "";

        if(StringUtils.isNotBlank(keyword)){

            if(StringUtils.isNotBlank(keyword)){
                urlParam += "&";
            }
            urlParam += "keyword=" + keyword;
        }

        if(StringUtils.isNotBlank(catalog3Id)){

            if(StringUtils.isNotBlank(urlParam)){
                urlParam += "&";
            }
            urlParam += "catalog3Id=" + catalog3Id;
        }

        if(skuAttrValueList != null){

            for (String pmsSkuAttrValue : skuAttrValueList) {

                if(!delValueId.equals(pmsSkuAttrValue)){
                    urlParam += "&valueId=" + pmsSkuAttrValue;
                }
            }
        }

        return urlParam;
    }*/



    private String getUrlParam(PmsSearchParam pmsSearchParam,String ...delValueId) {

        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] skuAttrValueList = pmsSearchParam.getValueId();
        String urlParam = "";

        if(StringUtils.isNotBlank(keyword)){

            if(StringUtils.isNotBlank(keyword)){
                urlParam += "&";
            }
            urlParam += "keyword=" + keyword;
        }

        if(StringUtils.isNotBlank(catalog3Id)){

            if(StringUtils.isNotBlank(urlParam)){
                urlParam += "&";
            }
            urlParam += "catalog3Id=" + catalog3Id;
        }

        if(skuAttrValueList != null){

            for (String pmsSkuAttrValue : skuAttrValueList) {

                if(delValueId.length == 0 || pmsSkuAttrValue.equals(delValueId[0])){
                    urlParam += "&valueId=" + pmsSkuAttrValue;
                }
                //urlParam += "&valueId=" + pmsSkuAttrValue;
            }
        }

        return urlParam;
    }


    @RequestMapping("index")
    @LoginRequired(loginSuccess = false)
    public String index(){
        return "index";
    }
}
