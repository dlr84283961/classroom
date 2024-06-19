package cn.ninghan.common.dao;

import cn.ninghan.common.domain.ClassroomNumber;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClassroomNumberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomNumber record);

    int insertSelective(ClassroomNumber record);

    ClassroomNumber selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassroomNumber record);

    int updateByPrimaryKey(ClassroomNumber record);

    List<ClassroomNumber> getAll();

    ClassroomNumber findByName(@Param("name") String name);
}
