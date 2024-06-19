package cn.ninghan.front.controller;

import cn.ninghan.common.domain.ClassroomUser;
import cn.ninghan.front.common.R;
import cn.ninghan.front.service.CanSelectTimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Value("${classroom.seatTime-period}")
    private int timePeriod;

    @Resource
    private CanSelectTimeService canSelectTimeService;

    @RequestMapping("/")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    @RequestMapping("/mockLogin.json")
    @ResponseBody
    public R login(HttpServletRequest request){
        ClassroomUser classroomUser = ClassroomUser.builder().id(1l).name("test").build();
        classroomUser.setPassword(null);
        request.getSession().setAttribute("user",classroomUser);
        return R.success("登录成功");

    }

    @RequestMapping("/logout.json")
    public R logout(HttpServletRequest request){
        request.getSession().invalidate();
        return R.success("登出成功");
    }

    @RequestMapping("/stationList.json")
    @ResponseBody
    public R stationList(){
        return R.success(canSelectTimeService.getCanSelectTimes(timePeriod));
    }

    @RequestMapping("/info.json")
    @ResponseBody
    public R info(HttpServletRequest request){
        return R.success(request.getSession().getAttribute("user"));
    }
}
