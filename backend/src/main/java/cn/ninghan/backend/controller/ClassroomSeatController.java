package cn.ninghan.backend.controller;

import cn.ninghan.backend.common.R;
import cn.ninghan.backend.dto.ClassroomSeatDto;
import cn.ninghan.backend.dto.PageResult;
import cn.ninghan.backend.param.PageParam;
import cn.ninghan.backend.param.SeatSearchParam;
import cn.ninghan.backend.service.ClassroomSeatService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/classroom/seat")
public class ClassroomSeatController {
    @Resource
    private ClassroomSeatService classroomSeatService;
    @Value("${classroom.seatTime-period}")
    private int timePeriod;

    @RequestMapping("/list.page")
    public ModelAndView seatList(){
        return new ModelAndView("trainSeat");
    }

    @RequestMapping("/generate.json")
    @ResponseBody
    public R generateSeat(@RequestParam("classroomNumberId")Integer classroomId,
                          @RequestParam("startTime")String startTime,@RequestParam("endTime")String endTime){
        classroomSeatService.generate(classroomId,startTime,endTime,timePeriod);
        return R.success("生成座位成功");
    }

    @RequestMapping("/search.json")
    @ResponseBody
    public R searchSeat(SeatSearchParam seatSearchParam, PageParam pageParam){
        int count = classroomSeatService.searchCount(seatSearchParam);
        if(count<=0){
            return R.success(PageResult.<ClassroomSeatDto>builder().total(0).data(Collections.emptyList()).build());
        }
        List<ClassroomSeatDto> classroomSeatDtos = classroomSeatService.searchWithPage(seatSearchParam,pageParam);
        return R.success(PageResult.<ClassroomSeatDto>builder().total(count).data(classroomSeatDtos).build());
    }

    @RequestMapping("/publish.json")
    @ResponseBody
    public R publishSeat(String classroomNumber,@RequestParam("classroomSeatIds") String classroomSeatIds){
        classroomSeatService.publishSeats(classroomNumber,classroomSeatIds);
        return R.success("发布成功");
    }

    @RequestMapping("/initialize.json")
    @ResponseBody
    public R initializeSeat(String classroomNumber,@RequestParam("classroomSeatIds") String classroomSeatIds){
        classroomSeatService.initializeSeats(classroomNumber,classroomSeatIds);
        return R.success("初始成功");
    }
}
