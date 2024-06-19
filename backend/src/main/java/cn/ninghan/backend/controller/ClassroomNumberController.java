package cn.ninghan.backend.controller;

import cn.ninghan.backend.common.R;
import cn.ninghan.backend.dto.ClassroomNumberDto;
import cn.ninghan.backend.service.ClassroomNumberService;
import cn.ninghan.backend.utils.BeanValidator;
import cn.ninghan.backend.param.ClassroomNumberParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/admin/classroom/number")
@Slf4j
public class ClassroomNumberController {
    @Resource
    private ClassroomNumberService classroomNumberService;

    @RequestMapping("/list.page")
    public ModelAndView list(){
        return new ModelAndView("trainNumber");
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public R getAllData(){
        List<ClassroomNumberDto> classroomNumberDtos = classroomNumberService.getAll();
        return R.success(classroomNumberDtos);
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public R save(ClassroomNumberParam param){
        BeanValidator.check(param);
        classroomNumberService.save(param);
        return R.success("保存成功");
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public R update(ClassroomNumberParam param){
        BeanValidator.check(param);
        classroomNumberService.updateClass(param);
        return R.success("更新成功");
    }
}
