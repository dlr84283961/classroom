package cn.ninghan.front.controller;

import cn.ninghan.common.seatDao.ClassroomSeatMapper;
import cn.ninghan.front.common.R;
import cn.ninghan.front.service.ClassroomSeatService;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/front")
@Slf4j
public class FrontController {
    @Value("${classroom.seatTime-period}")
    private int timePeriod;
    @Resource
    private ClassroomSeatService classroomSeatService;
    @PostMapping("/searchLeftCount.json")
    @ResponseBody
    public R searchLeftCountOfSeat(@RequestParam(name = "startTime")String startTime,
                                   @RequestParam(name = "endTime")String endTime,
                                   @RequestParam(name = "date")String date){
        return R.success(classroomSeatService.getSeatsLeft(startTime,endTime,date,timePeriod));
    }

    @PostMapping("/getNumberSeats.json")
    @ResponseBody
    public R getClassroomNumberSeat(@RequestParam(name = "startTime")String startTime,
                                    @RequestParam(name = "endTime")String endTime,
                                    @RequestParam(name = "date")String date,
                                    @RequestParam(name = "number")String classroomNumber){
        return R.success(classroomSeatService.getSeatsByClassroomNumber(startTime,endTime,date,classroomNumber,timePeriod));
    }

    @PostMapping("/grabSeat.json")
    @ResponseBody
    public R grabClassroomSeat(@RequestParam(name = "classroomNumber")String classroomNumber,
                               @RequestParam(name = "date")String date,
                               @RequestParam(name = "timePeriod")String seatTimePeriod,
                               @RequestParam(name = "seatNumber")Integer seatNumber){
        List<String> strList = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(seatTimePeriod);
        if(strList.size()!=2){
            return R.fail("时间段出错");
        }
        String startTime = strList.get(0);
        String endTime = strList.get(1);
        classroomSeatService.grabSeat(classroomNumber,date,startTime,endTime,seatNumber,timePeriod);
        return R.success("占座成功");
    }


}
