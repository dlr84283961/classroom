package cn.ninghan.common.test;

import cn.ninghan.common.dao.ClassroomNumberMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class TestController {
    @Resource
    private ClassroomNumberMapper classroomNumberMapper;
    @RequestMapping
    @ResponseBody
    public void test(){
        classroomNumberMapper.getAll();
    }
}
