<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>教室管理</title>
    <jsp:include page="/common/backend_common.jsp"/>
    <jsp:include page="/common/page.jsp"/>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div class="page-header">
    <h1>
        教室管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            维护教室
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-12">
        <div class="col-xs-12">
            <div class="table-header">
                教室列表&nbsp;&nbsp;
                <a class="green" href="#">
                    <i class="ace-icon fa fa-plus-circle orange bigger-130 number-add"></i>
                </a>
            </div>
            <div>
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <table id="dynamic-table" class="table table-striped table-bordered table-hover dataTable no-footer" role="grid"
                           aria-describedby="dynamic-table_info" style="font-size:14px">
                        <thead>
                        <tr role="row">
                            <th tabindex="1" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                教室名称
                            </th>
                            <th tabindex="1" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                开启时间
                            </th>
                            <th tabindex="1" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                结束时间
                            </th>
                            <th tabindex="1" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                座位类型
                            </th>
                            <th tabindex="1" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                教室类型
                            </th>
                            <th tabindex="1" aria-controls="dynamic-table" rowspan="5" colspan="1">
                                座位数
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
<div id="dialog-number-form" style="display: none;">
    <form id="numberForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td><label for="name">名称</label></td>
                <td>
                    <input type="text" name="name" id="name" value="" class="text ui-widget-content ui-corner-all">
                    <input type="hidden" name="id" id="classroomNumberId"/>
                </td>
            </tr>
            <tr>
                <td><label for="startTime">开启时间</label></td>
                <td>
                    <input type="text" name="startTime" id="startTime" value="" class="text ui-widget-content ui-corner-all">
                </td>
            </tr>
            <tr>
                <td><label for="endTime">结束时间</label></td>
                <td>
                    <input type="text" name="endTime" id="endTime" value="" class="text ui-widget-content ui-corner-all">
                </td>
            </tr>
            <tr>
                <td><label for="seatType">教室座位类型</label></td>
                <td>
                    <select id="seatType" name="seatType" data-placeholder="选择教室座位类型" style="width: 150px;">
                        <option value="C2">C2</option>
                        <option value="C5">C5</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="roomType">教室类型</label></td>
                <td>
                    <select id="roomType" name="roomType" data-placeholder="选择教室类型" style="width: 150px;">
                        <option value="1">多媒体教室</option>
                        <option value="2">普通教室</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dialog-generate-form" style="display: none;">
    <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
        <tr>
            <td><label for="name">开放时间</label></td>
            <td>
                <input type="text" name="fromTime" id="fromTime"/>
                (yyyy-MM-dd HH:mm)
            </td>
            <td><label for="name">结束时间</label></td>
            <td>
                <input type="text" name="toTime" id="toTime"/>
            </td>
        </tr>
    </table>
</div>

<script id="numberListTemplate" type="x-tmpl-mustache">
{{#numberList}}
<tr role="row" class="number-name odd" data-id="{{id}}"><!--even -->
    <td><a href="#" class="number-edit" data-id="{{id}}">{{name}}</a></td>
    <td><a href="#" class="number-edit" data-id="{{id}}">{{startTime}}</a></td>
    <td><a href="#" class="number-edit" data-id="{{id}}">{{endTime}}</a></td>
    <td><a href="#" class="number-edit" data-id="{{id}}">{{seatType}}</a></td>
    <td>{{showType}}</td>
    <td>{{seatNum}}</td>
    <td>
        <div class="hidden-sm hidden-xs action-buttons">
            <a class="green number-edit" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-pencil bigger-100"></i>
            </a>
            <a class="red number-generate" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-flag bigger-100"></i>
            </a>
        </div>
    </td>
</tr>
{{/numberList}}
</script>

<script type="application/javascript">
    $(function() {

        var numberListTemplate = $('#numberListTemplate').html();
        Mustache.parse(numberListTemplate);
        var numberMap = {};

        loadNumberList();

        function loadNumberList() {
            $.ajax({
                url: "/admin/classroom/number/list.json",
                success : function (result) {
                    if (result.ret) {
                        var numberList = result.data;
                        var rendered = Mustache.render(numberListTemplate, {
                            numberList: result.data,
                            "showType": function() {
                                return showType(this.roomType);
                            }
                        });
                        $(numberList).each(function (i, number) {
                            numberMap[number.id] = number;
                        });
                        $("#numberList").html(rendered);
                        bindNumberClick();
                    } else {
                        showMessage("加载教室列表", result.msg, false);
                    }
                }
            })
        }

        function showType(type) {
            var typeString = "未知";
            switch (type) {
                case 1: typeString = "多媒体教室"; break;
                case 2: typeString = "普通教室"; break;

            }
            return typeString;
        }

        // 绑定点击事件
        function bindNumberClick() {

            $(".number-edit").click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                var numberId = $(this).attr("data-id");
                $("#dialog-number-form").dialog({
                    modal: true,
                    title: "编辑教室",
                    open: function(event, ui) {
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                        $("#numberForm")[0].reset();
                        $("#classroomNumberId").val(numberId);
                        var targetNumber = numberMap[numberId];
                        if (targetNumber) {
                            $("#name").val(targetNumber.name);
                            $("#").val(targetNumber.name);
                            $("#startTime").val(targetNumber.startTime);
                            $("#endTime").val(targetNumber.endTime);
                        }
                    },
                    buttons : {
                        "更新": function(e) {
                            e.preventDefault();
                            updateNumber(false, function (data) {
                                $("#dialog-number-form").dialog("close");
                            }, function (data) {
                                showMessage("更新教室", data.msg, false);
                            })
                        },
                        "取消": function () {
                            $("#dialog-number-form").dialog("close");
                        }
                    }
                });
            });
            $(".number-generate").click(function(e){
                e.preventDefault();
                e.stopPropagation();
                var numberId = $(this).attr("data-id");
                $("#dialog-generate-form").dialog({
                    modal: true,
                    title: "生成座位",
                    open: function(event, ui) {
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                        $("#fromTime").val('');
                        $("#toTime").val('');
                    },
                    buttons : {
                        "生成": function(e) {
                            e.preventDefault();
                            var startTime = $("#fromTime").val();
                            var endTime = $("#toTime").val();
                            if (startTime == ''&&endTime == '') {
                                showMessage("生成座位", "必须指定教室开启和结束时间", false);
                                return;
                            }
                            $.ajax({
                                url: "/admin/classroom/seat/generate.json",
                                data: {
                                    classroomNumberId: numberId,
                                    startTime: startTime,
                                    endTime: endTime
                                },
                                type: 'POST',
                                success: function(result) {
                                    if (result.ret) {
                                        $("#dialog-generate-form").dialog("close");
                                        showMessage("生成座位", "操作成功", true);
                                    } else {
                                        showMessage("生成座位", result.msg, false);
                                    }
                                }
                            })
                        },
                        "取消": function () {
                            $("#dialog-generate-form").dialog("close");
                        }
                    }
                });
            });
        }

        $(".number-add").click(function() {
            $("#dialog-number-form").dialog({
                modal: true,
                title: "新增教室",
                open: function(event, ui) {
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                    $("#numberForm")[0].reset();
                },
                buttons : {
                    "添加": function(e) {
                        e.preventDefault();
                        updateNumber(true, function (data) {
                            $("#dialog-number-form").dialog("close");
                            loadNumberList();
                        }, function (data) {
                            showMessage("新增教室", data.msg, false);
                        })
                    },
                    "取消": function () {
                        $("#dialog-number-form").dialog("close");
                    }
                }
            });
        });

        function updateNumber(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? "/admin/classroom/number/save.json" : "/admin/classroom/number/update.json",
                data: $("#numberForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.ret) {
                        loadNumberList();
                        if (successCallback) {
                            successCallback(result);
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
            })
        }
    })
</script>