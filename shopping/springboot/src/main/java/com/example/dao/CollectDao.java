package com.example.dao;

import com.example.entity.Collect;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CollectDao extends Mapper<Collect> {

	List<Collect> findBySearch(@Param("search") Collect search);

	@Select("select * from collect where userId = #{id}")
	List<Collect> findByUserId(@Param("id") Integer id);

	@Select("select * from collect where goodsId = #{goodsId} and userId = #{userId} and role = #{role}")
	Collect findByRelIdAndUserIdAndRole(@Param("goodsId") Integer goodsId, @Param("userId") Integer userId, @Param("role") Integer role);

	@Delete("delete from collect where goodsId = #{goodsId} and userId = #{userId} and role = #{role}")
	void deleteByRelIdAndUserIdAndRole(@Param("goodsId") Integer goodsId, @Param("userId") Integer userId, @Param("role") Integer role);

}
