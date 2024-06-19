<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>座位查询及管理</title>
    <jsp:include page="/common/backend_common.jsp"/>
    <jsp:include page="/common/page.jsp"/>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div class="main-content-inner">
    <div class="col-sm-12">
        <div class="col-xs-12">
            <div class="table-header">
                座位信息列表
            </div>
            <div>
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="dataTables_length" id="dynamic-table_length">
                                展示 <input id="pageSize" type="search" name="pageSize" value="100"
                                          class="form-control input-sm" aria-controls="dynamic-table">条记录
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input id="search-classroomNumber" type="search" name="classroomNumber"
                                       class="form-control input-sm" placeholder="教室名，必填" aria-controls="dynamic-table">
                                <input id="search-time" type="search" name="time" class="form-control input-sm"
                                       placeholder="日期(yyyyMMdd)，必填" aria-controls="dynamic-table">
                                <select id="search-status" name="status">
                                    <option value="">全部</option>
                                    <option value="0" selected>初始</option>
                                    <option value="1">已放座</option>
                                    <option value="2">已占座</option>
                                </select>

                                <input id="search-carriage" type="search" name="carriageNumber"
                                       class="form-control input-sm" placeholder="行" aria-controls="dynamic-table">
                                <input id="search-row" type="search" name="rowNum" class="form-control input-sm"
                                       placeholder="列" aria-controls="dynamic-table">
                                <input id="search-seat" type="search" name="seatNum" class="form-control input-sm"
                                       placeholder="座位号" aria-controls="dynamic-table">

                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <button class="btn btn-info fa research"  type="button">
                                    查询
                                </button>
                                <button class="btn btn-info fa select-all" type="button">
                                    全选
                                </button>
                                <button class="btn btn-info fa select-none" type="button">
                                    全不选
                                </button>
                                <button class="btn btn-info fa publish" type="button">
                                    放座
                                </button>
                                <button class="btn btn-info initialize" type="button">
                                    恢复初始
                                </button>
                            </div>
                        </div>
                        <table id="dynamic-table"
                               class="table table-striped table-bordered table-hover dataTable no-footer" role="grid"
                               aria-describedby="dynamic-table_info" style="font-size:14px">
                            <thead>
                            <tr role="row">
                                <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                </th>
                                <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                    教室
                                </th>
                                <th tabindex="1" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                    行
                                </th>
                                <th tabindex="2" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                    列
                                </th>
                                <th tabindex="3" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                    座位号
                                </th>
                                <th tabindex="4" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                    开放时间
                                </th>
                                <th tabindex="5" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                    结束时间
                                </th>
                                <th tabindex="6" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                    状态
                                </th>
                                <th tabindex="7" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                    占座用户
                                </th>
                                <th class="sorting_disabled" rowspan="1" colspan="1" aria-label="">操作</th>
                            </tr>
                            </thead>
                            <tbody id="seatList"></tbody>
                        </table>
                        <div class="row" id="seatPage">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script id="seatListTemplate" type="x-tmpl-mustache">
{{#seatList}}
<tr role="row" data-id="{{id}}"><!--even -->
    <td><input type="checkbox" class="seat-check" name="id" value="{{id}}"></input></td>
    <td>{{classroomNumber}}</td>
    <td>{{carriageNumber}}</td>
    <td>{{rowNumber}}</td>
    <td>{{seatNumber}}</td>
    <td>{{startTime}}</td>
    <td>{{endTime}}</td>
    <td>{{showStatus}}</td>
    <td>{{userId}}</td>
    <td>
        <div class="hidden-sm hidden-xs action-buttons">
            <a class="green number-edit" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-pencil bigger-100"></i>
            </a>
            <a class="red number-detail-update" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-flag bigger-100"></i>
            </a>
        </div>
    </td>
</tr>
{{/seatList}}

</script>

<script type="application/javascript">
    $(function () {
        var seatListTemplate = $('#seatListTemplate').html();
        Mustache.parse(seatListTemplate);

        $(".select-all").click(function (e) {
            e.preventDefault();
            $(".seat-check").prop("checked", true);
        });

        $(".select-none").click(function (e) {
            e.preventDefault();
            $(".seat-check").prop("checked", false);
        });

        $(".research").click(function (e) {
            e.preventDefault();
            searchSeatList();
        });

        $(".publish").click(function (e) {
            e.preventDefault();
            var idArray = [];
            $("#seatList").find(":checked").each(function(i, obj) {
                idArray.push($(obj).val());
            });
            $.ajax({
                url: "/admin/classroom/seat/publish.json",
                data: {
                    classroomNumber: $("#search-classroomNumber").val(),
                    classroomSeatIds: idArray.join(",")
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        showMessage("放票", "操作成功", true);
                    } else {
                        showMessage("放票", result.msg, false);
                    }
                    searchSeatList();
                }
            });
        });

        $(".initialize").click(function (e) {
            e.preventDefault();
            var idArray = [];
            $("#seatList").find(":checked").each(function(i, obj) {
                idArray.push($(obj).val());
            });
            $.ajax({
                url: "/admin/classroom/seat/initialize.json",
                data: {
                    classroomNumber: $("#search-classroomNumber").val(),
                    classroomSeatIds: idArray.join(",")
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        showMessage("初始化", "操作成功", true);
                    } else {
                        showMessage("初始化", result.msg, false);
                    }
                    searchSeatList();
                }
            });
        });

        function searchSeatList() {
            var classroomNumber = $("#search-classroomNumber").val();
            if (classroomNumber == '') {
                alert("教室必填");
                return;
            }
            var time = $("#search-time").val();
            if (time == '') {
                alert("日期必填");
                return;
            }
            var pageSize = $("#pageSize").val();
            var pageNo = $("#seatPage .pageNo").val() || 1;
            var carriageNum = $("#search-carriage").val();
            var rowNum = $("#search-row").val();
            var seatNum = $("#search-seat").val();
            var status = $("#search-status").val();
            var url = "/admin/classroom/seat/search.json" + "?classroomNumber=" + classroomNumber + "&time=" + time
                + "&status=" + status + "&carriageNum=" + carriageNum + "&rowNum=" + rowNum + "&seatNum=" + seatNum;
            $.ajax({
                url: url,
                data: {
                    pageNo: pageNo,
                    pageSize: pageSize
                },
                type: 'POST',
                success: function (result) {
                    renderSeatListAndPage(result, url)
                }
            });
        }

        function renderSeatListAndPage(result, url) {
            if (result.ret) {
                if (result.data.total > 0) {
                    var rendered = Mustache.render(seatListTemplate, {
                        seatList: result.data.data,
                        "showStatus": function () {
                            return showStatus(this.status);
                        }
                    });
                    $("#seatList").html(rendered);

                    var pageSize = $("#pageSize").val();
                    var pageNo = $("#seatPage .pageNo").val() || 1;
                    renderPage(url, result.data.total, pageNo, pageSize, result.data.total > 0 ? result.data.data.length : 0, "seatPage", renderSeatListAndPage);
                } else {
                    $("#seatList").html('');
                }
            } else {
                showMessage("获取座位信息", result.msg, false);
            }
        };


        function showStatus(status) {
            var showStatus = "未知";
            switch (status) {
                case 0:
                    showStatus = "初始";
                    break;
                case 1:
                    showStatus = "已放座";
                    break;
                case 2:
                    showStatus = "已占座";
                    break;

            }
            return showStatus;
        }
    })
</script>
