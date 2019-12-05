<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>
        <%
            request.setAttribute("context", request.getContextPath());
        %>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>菜单</title>
        </head>
    </title>
    <div>
        <div class="">
            <li><a href="${context}/font.jsp"><i class="fa fa-edit fa-fw"></i>文字互动</a></li>
            <li><a href="${context}/picture.jsp"><i class="fa fa-edit fa-fw"></i>图片互动</a></li>
        </div>
        <div>
            asgag
        </div>
    </div>
</head>
<body>

</body>
</html>
