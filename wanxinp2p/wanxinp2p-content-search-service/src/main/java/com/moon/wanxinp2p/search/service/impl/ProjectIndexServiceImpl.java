package com.moon.wanxinp2p.search.service.impl;

import com.moon.wanxinp2p.api.search.model.ProjectQueryParamsDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.domain.PageVO;
import com.moon.wanxinp2p.search.service.ProjectIndexService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标的检索业务层接口实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-11 15:39
 * @description
 */
@Service
@Slf4j
public class ProjectIndexServiceImpl implements ProjectIndexService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Value("${wanxinp2p.es.index}")
    private String projectIndex;

    /**
     * 检索标的
     *
     * @param projectQueryParamsDTO
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param order
     * @return
     */
    @Override
    public PageVO<ProjectDTO> queryProjectIndex(ProjectQueryParamsDTO projectQueryParamsDTO,
                                                Integer pageNo, Integer pageSize, String sortBy, String order) {
        // 创建搜索请求对象(SearchRequest)，索引库名称从apollo配置中心获取
        SearchRequest searchRequest = new SearchRequest(this.projectIndex);
        // 搜索源构建对象(SearchSourceBuilder)
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        /* 设置搜索条件 */
        // 创建条件封装对象
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 非空判断并封装条件
        if (StringUtils.isNotBlank(projectQueryParamsDTO.getName())) {
            queryBuilder.must(QueryBuilders.termQuery("name", projectQueryParamsDTO.getName()));
        }
        if (projectQueryParamsDTO.getStartPeriod() != null) {
            queryBuilder.must(QueryBuilders.rangeQuery("period").gte(projectQueryParamsDTO.getStartPeriod()));
        }
        if (projectQueryParamsDTO.getEndPeriod() != null) {
            queryBuilder.must(QueryBuilders.rangeQuery("period").lte(projectQueryParamsDTO.getEndPeriod()));
        }

        // 向搜索源构建对象设置查询条件
        searchSourceBuilder.query(queryBuilder);
        // 向搜索源构建对象设置排序参数
        if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
            if ("asc".equalsIgnoreCase(order)) {
                searchSourceBuilder.sort(sortBy, SortOrder.ASC);
            }
            if ("desc".equalsIgnoreCase(order)) {
                searchSourceBuilder.sort(sortBy, SortOrder.DESC);
            }
        } else {
            // 默认按创建时间倒序
            searchSourceBuilder.sort("createdate", SortOrder.DESC);
        }

        // 向搜索源构建对象设置分页参数
        searchSourceBuilder.from((pageNo - 1) * pageSize);
        searchSourceBuilder.size(pageSize);

        // 搜索请求对象(SearchRequest)中设置上面构造好的搜索源(SearchSourceBuilder)
        searchRequest.source(searchSourceBuilder);

        try {
            /* 执行搜索，向ES发起http请求 */
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 获取搜索结果
            SearchHits hits = response.getHits();
            // 匹配到的总记录数
            long totalHits = hits.getTotalHits().value;
            log.info("搜索匹配到的总记录数：{}", totalHits);

            // 得到匹配度高的文档
            SearchHit[] searchHits = hits.getHits();
            // 循环搜索结果，转成dto
            List<ProjectDTO> list = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                ProjectDTO projectDTO = new ProjectDTO();
                // 获取源文档内容Map
                Map<String, Object> sourceMap = hit.getSourceAsMap();
                projectDTO.setAmount(new BigDecimal((Double) sourceMap.get("amount")));
                projectDTO.setProjectStatus((String) sourceMap.get("projectstatus"));
                projectDTO.setPeriod(Integer.parseInt(sourceMap.get("period").toString()));
                projectDTO.setName((String) sourceMap.get("name"));
                projectDTO.setDescription((String) sourceMap.get("description"));
                projectDTO.setId(Long.parseLong(sourceMap.get("id").toString()));
                projectDTO.setAnnualRate(new BigDecimal(sourceMap.get("annualrate").toString()));

                list.add(projectDTO);
            }

            // 封装为 PageVO 对象并返回
            PageVO<ProjectDTO> pageVO = new PageVO<>();
            pageVO.setTotal(totalHits);
            pageVO.setContent(list);
            pageVO.setPageSize(pageSize);
            pageVO.setPageNo(pageNo);
            return pageVO;
        } catch (IOException e) {
            log.error("标的检索出现异常：{}", e.getMessage(), e);
            return null;
        }
    }

}
