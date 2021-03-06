package com.cl.house.common.page;

import java.util.List;

import lombok.Data;


/**
 *  将分页查询数据封装臣对象返回
 *  @author 臣不二
 *  2018年12月17日 下午6:22:13
 *
 */
@Data
public class PageData <T> {
	
	private List<T> list;
	private Pagination pagination;//pageNum,pageSize,page list
	
	public PageData(Pagination pagination,List<T> list){
		this.pagination = pagination;
		this.list = list;
	}
	
	public static  <T> PageData<T> buildPage(List<T> list,Long count,Integer pageSize,Integer pageNum){
		Pagination _pagination = new Pagination(pageSize, pageNum, count);
		return new PageData<>(_pagination, list);
	}
}
