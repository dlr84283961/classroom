<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>12306前台</title>
    <jsp:include page="/common/frontend_common.jsp"/>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div>
    <div class="navbar-container ace-save-state" id="navbar-container">

        <div class="pull-left">
            <a href="#" class="navbar-brand">
                教室座位查询
            </a>
        </div>
        <div class="pull-right">
            <a href="#" class="login navbar-brand" style="display:none;">
                Mock登陆
            </a>
            <a href="#" class="profile navbar-brand" style="display:none;">
            </a>
            <a href="#" class="logout navbar-brand" style="display:none;">注销</a>
        </div>
        <div class="main-content-inner">
            <div class="col-sm-12">
                <div class="col-xs-12">
                    <div>
                        <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                            <div class="col-xs-12">
                                <div class="dataTables_length" id="dynamic-table_length">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    开放时间<select id="search-start" name="start" style="width: 100px;"> </select>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    结束时间<select id="search-end" name="end" style="width: 100px;"></select>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    日期<input id="search-date" type="text" name="end" class="form-control input-sm"
                                              placeholder="yyyy-MM-dd，必填" aria-controls="dynamic-table">
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <button class="btn btn-info fa research" type="button">
                                        查询
                                    </button>
                                </div>
                            </div>
                            <table id="dynamic-table"
                                   class="table table-striped table-bordered table-hover dataTable no-footer"
                                   role="grid"
                                   aria-describedby="dynamic-table_info" style="font-size:14px">
                                <thead>
                                <tr role="row">
                                    <th tabindex="1" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                        教室
                                    </th>
                                    <th tabindex="2" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                        剩余座位数
                                    </th>
                                    <th class="sorting_disabled" rowspan="1" colspan="1" aria-label="">
                                        操作
                                    </th>
                                </tr>
                                </thead>
                                <tbody id="numberList"></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="contactSeats" style="display:none;">
            <a href="#" class="navbar-brand">
                选择座位
            </a>
            <div class="col-sm-12">
                <div class="col-xs-12">
                    <div>
                        <div id="dynamic-table_wrapper2" class="dataTables_wrapper form-inline no-footer">
                            <table id="dynamic-table2"
                                   class="table table-striped table-bordered table-hover dataTable no-footer"
                                   role="grid"
                                   aria-describedby="dynamic-table_info" style="font-size:14px">
                                <thead>
                                <tr role="row">
                                    <th tabindex="1" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                        教室
                                    </th>
                                    <th tabindex="2" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                        日期
                                    </th>
                                    <th tabindex="3" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                        开始-结束
                                    </th>
                                    <th tabindex="4" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                        座位号
                                    </th>
                                </tr>
                                </thead>
                                <tbody id="seatList"></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="myOrders" style="display:none;">
            <a href="#" class="navbar-brand">
                我的占座
            </a>
            <div class="col-sm-12">
                <div class="col-xs-12">
                    <div>
                        <div id="dynamic-table_wrapper3" class="dataTables_wrapper form-inline no-footer">
                            <table id="dynamic-table3"
                                   class="table table-striped table-bordered table-hover dataTable no-footer"
                                   role="grid"
                                   aria-describedby="dynamic-table_info" style="font-size:14px">
                                <thead>
                                <tr role="row">
                                    <th tabindex="1" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                        日期
                                    </th>
                                    <th tabindex="3" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                        开始时间
                                    </th>
                                    <th tabindex="4" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                        结束时间
                                    </th>
                                    <th tabindex="5" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                        座位信息
                                    </th>
                                    <th tabindex="6" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                        状态
                                    </th>
                                    <th class="sorting_disabled" rowspan="1" colspan="1" aria-label="">
                                        操作
                                    </th>
                                </tr>
                                </thead>
                                <tbody id="orderList"></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.navbar-container -->
</div>

<script id="numberListTemplate" type="x-tmpl-mustache">
{{#numberList}}
<tr role="row" class="number-name odd" data-id="{{number}}">
    <td>{{number}}</td>
    <td>{{leftCount}}</td>
    <td>
        <div class="hidden-sm hidden-xs action-buttons">
            <a class="green grab_seats" href="#" data-id="{{number}}">
                查看座位
            </a>
        </div>
    </td>
</tr>
{{/numberList}}
</script>

<script id="seatListTemplate" type="x-tmpl-mustache">
{{#seatList}}
<tr role="row" class="traveller-name odd" data-id="{{seatNumber}}">
    <td>{{classroomNumber}}</td>
    <td>{{date}}</td>
    <td>{{timePeriod}}</td>
    <td>{{seatNumber}}</td>
    <td>
        <div class="hidden-sm hidden-xs action-buttons">
            <a class="green grab_concrete_seats" href="#" data-id="{{seatNumber}}">
                占座
            </a>
        </div>
    </td>
</tr>
{{/seatList}}
</script>

<script id="orderListTemplate" type="x-tmpl-mustache">
{{#orderList}}
<tr role="row" class="order-detail odd" data-id="{{trainOrder.orderId}}">
    <td>{{trainOrder.ticket}}</td>
    <td>{{fromStationName}}</td>
    <td>{{toStationName}}</td>
    <td>{{seatInfo}}</td>
    <td>{{showStatus}}</td>
    <td>
         <div class="hidden-sm hidden-xs action-buttons">
         {{#showPay}}
            <a class="green pay_order" href="#" data-id="{{trainOrder.orderId}}">
                立即支付
            </a>
         {{/showPay}}
         {{#showCancel}}
            <a class="refund_order" href="#" data-id="{{trainOrder.orderId}}">
                退款
            </a>
         {{/showCancel}}
        </div>
    </td>
</tr>
{{/orderList}}
</script>

<script type="application/javascript">
    $(function () {
        var numberListTemplate = $('#numberListTemplate').html();
        Mustache.parse(numberListTemplate);

        var seatListTemplate = $('#seatListTemplate').html();
        Mustache.parse(seatListTemplate);

        var orderListTemplate = $('#orderListTemplate').html();
        Mustache.parse(orderListTemplate);

        var isLogin = false;
        var hasResult = false;

        $.ajax({
            url: '/stationList.json',
            type: 'GET',
            success: function (result) {
                if (result.ret) {
                    var optionStr = '<option value="0"> </option>';
                    $(result.data).each(function (i, station) {
                        optionStr += Mustache.render("<option value='{{selectTime}}'>{{selectTime}}</option>", {
                            selectTime: station.selectTime
                        });
                    });
                    $("#search-start").html(optionStr);
                    $("#search-end").html(optionStr);
                } else {
                    showMessage("获取时间信息", result.msg, false);
                }
            }
        });

        function checkLogin() {
            $.ajax({
                url: '/info.json',
                type: 'GET',
                success: function (result) {
                    if (result.ret && result.data != null) {
                        bindLogin(result.data.name);
                    } else if (result.code == 2 || result.data == null) { // 未登录
                        bindLogout();
                    } else {
                        isLogin = false;
                        showMessage("加载信息", result.msg, false);
                    }
                }
            });
        }

        checkLogin();

        $(".logout").click(function (e) {
            e.preventDefault();
            $.ajax({
                url: '/logout.json',
                type: 'GET',
                success: function (result) {
                    if (result.ret) {
                        bindLogout();
                    } else {
                        showMessage("注销", result.msg, false);
                    }
                }
            });
        });

        $(".login").click(function (e) {
            e.preventDefault();
            $.ajax({
                url: '/mockLogin.json',
                type: 'GET',
                success: function (result) {
                    if (result.ret) {
                        checkLogin();
                    } else {
                        showMessage("登陆", result.msg, false);
                    }
                }
            });
        });

        $(".profile").click(function (e) {
            e.preventDefault();
            console.log("profile click");
        });

        // login成功后的操作
        function bindLogin(name) {
            $(".profile").html("欢迎, " + name).show();
            $(".logout").show();
            $(".login").hide();
            $(".myOrders").show();
            isLogin = true;
            getOrders();
        }

        // logout之后及未登录的的操作
        function bindLogout() {
            $(".profile").hide();
            $(".logout").hide();
            $(".login").show();
            $(".myOrders").hide();
            isLogin = false;
            $(".contactSeats").hide();
        }

        $(".research").click(function (e) {
            e.preventDefault();
            var start = $("#search-start").val();
            var end = $("#search-end").val();
            var date = $("#search-date").val();
            if (start == '' || start.length != 5) {
                alert("请选择开始时间");
                return;
            }
            if (end == '' || end.length != 5) {
                alert("请选择结束时间");
                return;
            }
            if (date == '' || date.length != 10) {
                alert("请输入yyyy-MM-dd格式的日期");
                return;
            }
            $.ajax({
                url: '/front/searchLeftCount.json',
                data: {
                    startTime: start,
                    endTime: end,
                    date: date
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        if(result.data && result.data.length > 0) {
                            hasResult = true;
                            var rendered = Mustache.render(numberListTemplate, {
                                numberList: result.data
                            });
                            $("#numberList").html(rendered);
                            bindClick();
                        } else {
                            hasResult = false;
                            showMessage("查询座位", "未查询到相关的教室", false);
                            $(".contactSeats").hide();
                        }
                    } else {
                        showMessage("查询车票", result.msg, false);
                    }
                }
            });
        });


        function bindClick() {
            $(".grab_seats").click(function (e) {
                e.preventDefault();
                var number = $(this).attr("data-id");
                var start = $("#search-start").val();
                var end = $("#search-end").val();
                var date = $("#search-date").val();
                if (start == '' || start.length != 5) {
                    alert("请选择开始时间");
                    return;
                }
                if (end == '' || end.length != 5) {
                    alert("请选择结束时间");
                    return;
                }
                if (date == '' || date.length != 10) {
                    alert("请输入yyyy-MM-dd格式的日期");
                    return;
                }
                $.ajax({
                    url: '/front/getNumberSeats.json',
                    data: {
                        startTime: start,
                        endTime: end,
                        date: date,
                        number: number
                    },
                    type: 'POST',
                    success: function (result) {
                        if (result.ret) {
                            if (isLogin) {
                                $(".contactSeats").show();
                                var rendered = Mustache.render(seatListTemplate, {
                                    seatList: result.data
                                });
                                $("#seatList").html(rendered);
                                bindGrabSeatClick();
                                showMessage("查看座位成功", "", true);
                            }else {
                                $(".contactSeats").hide();
                                showMessage("查看座位失败", "", false);
                            }
                        } else {
                            $(".contactSeats").hide();
                            showMessage("查看座位", result.msg, false);
                        }
                    }
                })
            })
        }

        function bindGrabSeatClick() {
            $(".grab_concrete_seats").click(function (e) {
                e.preventDefault();
                var id = $(this).attr("data-id");
                var tr = $(this).parent().parent().parent();
                var classroomNumber = tr.children().eq(0).text();
                var date = tr.children().eq(1).text();
                var timePeriod = tr.children().eq(2).text();
                var seatNumber = tr.children().eq(3).text();
                $.ajax({
                    url: '/front/grabSeat.json',
                    data: {
                        classroomNumber: classroomNumber,
                        date: date,
                        timePeriod: timePeriod,
                        seatNumber: seatNumber
                    },
                    type: 'POST',
                    success: function (result) {
                        if (result.ret) {
                            showMessage("占座成功", "请及时签到", false);
                            getOrders();
                        } else {
                            showMessage("占座", result.msg, false);
                        }
                    }
                })
            })
        }

        function getOrders() {
            $.ajax({
                url: '/user/getOrderList.json',
                type: 'GET',
                success: function (result) {
                    if (result.ret) {
                        var rendered = Mustache.render(orderListTemplate, {
                            orderList: result.data,
                            "showStatus":function () {
                                return showOrderStatus(this.trainOrder.status);
                            }
                        });
                        $("#orderList").html(rendered);
                        bindOrderClick();
                    } else {
                        showMessage("获取订单", result.msg, false);
                    }
                }
            })
        }

        function bindOrderClick() {
            $(".pay_order").click(function (e) {
                e.preventDefault();
                var orderId = $(this).attr("data-id");
                $.ajax({
                    url: '/front/mockPay.json',
                    data: {
                        orderId: orderId
                    },
                    type: 'POST',
                    success: function (result) {
                        if (result.ret) {
                            getOrders();
                        } else {
                            showMessage("支付订单", result.msg, false);
                        }
                    }
                })
            });
            $(".refund_order").click(function(e) {
                e.preventDefault();
                var orderId = $(this).attr("data-id");
                $.ajax({
                    url: '/front/mockCancel.json',
                    data: {
                        orderId: orderId
                    },
                    type: 'POST',
                    success: function (result) {
                        if (result.ret) {
                            getOrders();
                        } else {
                            showMessage("取消订单", result.msg, false);
                        }
                    }
                })
            })
        }

        function showOrderStatus(status) {
            if (status == 10) {
                return "等待支付";
            } else if (status == 20) {
                return "已支付";
            } else if (status == 30) {
                return "超时未支付自动取消";
            } else if (status == 40) {
                return "支付后取消";
            } else {
                return "未知状态";
            }
        }
    })
</script>