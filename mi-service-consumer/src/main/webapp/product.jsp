<%--<jsp:useBean id="condition" scope="session" type="com.entity.Condition"/>--%>
<%--<jsp:useBean id="msg" scope="request" type="java.lang.String"/>--%>
<%--<jsp:useBean id="typeList" scope="request" type="java.util.List"/>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <script type="text/javascript">
        if ("${msg}" !== "") {
            alert("${msg}");
        }
    </script>

    <c:remove var="msg"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bright.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/addBook.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <title></title>
</head>
<script type="text/javascript">
    function allClick() {
        //取得全选复选框的选中未选 中状态
        let ischeck=$("#all").prop("checked");
        //将此状态赋值给每个商品列表里的复选框
        $("input[name=ck]").each(function () {
            this.checked=ischeck;
        });
    }

    function ckClick() {
        //取得所有name=ck的被选中的复选框
        let length=$("input[name=ck]:checked").length;
        //取得所有name=ck的复选框
        let len=$("input[name=ck]").length;
        //比较
        if(length === len){
            $("#all").prop("checked",true);
        }else
        {
            $("#all").prop("checked",false);
        }
    }

    console.log(${condition.getTypeId()})
</script>
<body>
<div id="brall">
    <div id="nav">
        <p>商品管理>商品列表</p>
    </div>
    <div id="condition" style="text-align: center">
        <form id="myForm">
            商品名称：<input name="pName" id="pName" value="${condition.getpName()==null?"":condition.getpName()}">&nbsp;&nbsp;&nbsp;
            商品类型：<select name="typeId" id="typeId">
            <option value="-1">请选择</option>
            <c:forEach items="${typeList}" var="type">
                <option value="${type.typeId}" ${condition.getTypeId()==type.typeId?"selected":""}>${type.typeName}</option>
            </c:forEach>
        </select>&nbsp;&nbsp;&nbsp;
            价格：<input name="lowPrice" id="lowPrice" value="${condition.getLowPrice()==null?"":condition.getLowPrice()}">-<input name="highPrice" id="highPrice" value="${condition.getHighPrice()==null?"":condition.getHighPrice()}">
            <input type="button" value="查询" onclick="condition()">
            <%--<input type="button" value="查询" onclick="ajaxSplit(${info.pageNum})">--%>
        </form>
    </div>
    <br>
    <div id="table">
        <c:choose>
            <c:when test="${infoList.getSize()!=0}">

                <div id="top">
                    <input type="checkbox" id="all" onclick="allClick()" style="margin-left: 50px">&nbsp;&nbsp;全选
                    <a href="${pageContext.request.contextPath}/addProduct.jsp">

                        <input type="button" class="btn btn-warning" id="add"
                               value="新增商品">
                    </a>
                    <input type="button" class="btn btn-warning" id="delete"
                           value="批量删除" onclick="deleteBatch(${infoList.pageNum})">
                </div>
                <!--显示分页后的商品-->
                <div id="middle">
                    <table class="table table-bordered table-striped">
                        <tr>
                            <th></th>
                            <th>商品名</th>
                            <th>商品介绍</th>
                            <th>定价（元）</th>
                            <th>商品图片</th>
                            <th>商品数量</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${infoList.getList()}" var="p">
                            <tr>
                                <td valign="center" align="center"><input type="checkbox" name="ck" id="ck" value="${p.pId}" onclick="ckClick()"></td>
                                <td>${p.pName}</td>
                                <td>${p.pContent}</td>
                                <td>${p.pPrice}</td>
                                <td><img width="55px" height="45px"
                                         src="${pageContext.request.contextPath}/image_big/${p.pImage}"></td>
                                <td>${p.pNumber}</td>
                                    <%--<td><a href="${pageContext.request.contextPath}/admin/product?flag=delete&pid=${p.pId}" onclick="return confirm('确定删除吗？')">删除</a>--%>
                                    <%--&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/admin/product?flag=one&pid=${p.pId}">修改</a></td>--%>
                                <td>
                                    <button type="button" class="btn btn-info "
                                            onclick="query(${p.pId},${infoList.pageNum})">编辑
                                    </button>
                                    <button type="button" class="btn btn-warning" id="mydel"
                                            onclick="del(${p.pId},${infoList.pageNum})">删除
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    <!--分页栏-->
                    <div id="bottom">
                        <div>
                            <nav aria-label="..." style="text-align:center;">
                                <ul class="pagination">
                                    <li>
                                        <a href="javascript:ajaxSplit(${infoList.prePage})" aria-label="Previous">
                                            <span aria-hidden="true">«</span></a>
                                    </li>
                                    <c:forEach begin="1" end="${infoList.pages}" var="i">
                                        <c:if test="${infoList.pageNum==i}">
                                            <li>
                                                <a href="javascript:ajaxSplit(${i})" style="background-color: grey">${i}</a>
                                            </li>
                                        </c:if>
                                        <c:if test="${infoList.pageNum!=i}">
                                            <li>
                                                <a href="javascript:ajaxSplit(${i})">${i}</a>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                    <li>
                                        <%--  <a href="${pageContext.request.contextPath}/prod/split.action?page=1" aria-label="Next">--%>
                                        <a href="javascript:ajaxSplit(${infoList.nextPage==0? 1 : infoList.nextPage})" aria-label="Next">
                                            <span aria-hidden="true">»</span></a>
                                    </li>
                                    <li style=" margin-left:150px;color: #0e90d2;height: 35px; line-height: 35px;">总共&nbsp;&nbsp;&nbsp;<font
                                            style="color:orange;">${infoList.pages}</font>&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <c:if test="${infoList.pageNum!=0}">
                                            当前&nbsp;&nbsp;&nbsp;<font style="color:orange;">${infoList.pageNum}</font>&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        </c:if>

                                        <c:if test="${infoList.pageNum==0}">
                                            当前&nbsp;&nbsp;&nbsp;<font style="color:orange;">1</font>&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        </c:if>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div>
                    <h2 style="width:1200px; text-align: center;color: orangered;margin-top: 100px">暂时没有符合条件的商品！</h2>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>

<script type="text/javascript">

    //批量删除
    function deleteBatch(num) {

        //取得所有被选中删除商品的pid
        let zhi=$("input[name=ck]:checked");
        let str="";
        let id="";

        let pName = $("#pName").val();
        let typeId = $("#typeId").val();
        let lowPrice = $("#lowPrice").val();
        let highPrice = $("#highPrice").val();

        if(zhi.length===0){
            alert("请选择将要删除的商品！");
        }else{
            // 有选中的商品，则取出每个选 中商品的ID，拼提交的ID的数据
            if(confirm("您确定删除"+zhi.length+"条商品吗？")){
                //拼接ID
                $.each(zhi,function (index,item) {
                    id=$(item).val(); //22 33
                    if(id!=null)
                        str += id+",";  //22,33,44
                });
                 //发送请求到服务器端
                 $.ajax({
                     url:"${pageContext.request.contextPath}/prod/deleteBatch.action",
                     data:{pIds:str,pageNum:num,"pName":pName,"typeId":typeId,"lowPrice":lowPrice,"highPrice":highPrice},
                     method: "post",
                     dataType: "text",
                     success: msg=>{
                         alert(msg);
                         $("#table").load("${pageContext.request.contextPath}/product.jsp #table")
                     }
                 })
             }
         }
    }
    //单个删除
    function del(pid,num) {

        let pName = $("#pName").val();
        let typeId = $("#typeId").val();
        let lowPrice = $("#lowPrice").val();
        let highPrice = $("#highPrice").val();

        if (confirm("确定删除吗")) {
          //向服务器提交请求完成删除
            <%--window.location="${pageContext.request.contextPath}/prod/delete.action?pid="+pid;--%>
            $.ajax({
                url:"${pageContext.request.contextPath}/prod/delete.action",
                data:{"pid":pid,"pageNum":num,"pName":pName,"typeId":typeId,"lowPrice":lowPrice,"highPrice":highPrice},
                method:"post",
                dataType:"text",
                success: res =>{
                    alert(res);
                    $("#table").load("${pageContext.request.contextPath}/product.jsp #table")
                }
            })
        }
    }

    // 点击编辑按钮，根据pid查询商品详情
    function query(pid, ispage) {
        let pName = $("#pName").val();
        let typeId = $("#typeId").val();
        let lowPrice = $("#lowPrice").val();
        let highPrice = $("#highPrice").val();

        let condition = "?pid="+pid+"&pageNum="+ispage+"&pName="+pName+"&typeId="+typeId+"&lowPrice="+lowPrice+"&highPrice="+highPrice;

        location.href = "${pageContext.request.contextPath}/prod/queryById.action"+condition;
        <%--location.href = "${pageContext.request.contextPath}/prod/queryById.action?pid=" + pid + "&pageNum=" + ispage;--%>
    }

    // 多条件查询
    function condition() {
        let pName = $("#pName").val();
        let typeId = $("#typeId").val();
        let lowPrice = $("#lowPrice").val();
        let highPrice = $("#highPrice").val();

        $.ajax({
            url:"${pageContext.request.contextPath}/prod/ajaxSplit.action",
            data:{"pName":pName,"typeId":typeId,"lowPrice":lowPrice,"highPrice":highPrice},
            success : ()=>{
                $("#table").load("${pageContext.request.contextPath}/product.jsp #table")
            }
        })

        // alert(typeId)
    }
</script>
<!--分页的AJAX实现-->
<script type="text/javascript">
    function ajaxSplit(page) {
        let pName = $("#pName").val();
        let typeId = $("#typeId").val();
        let lowPrice = $("#lowPrice").val();
        let highPrice = $("#highPrice").val();

        //异步ajax分页请求
        $.ajax({
        url:"${pageContext.request.contextPath}/prod/ajaxSplit.action",
            data:{"pageNum":page,"pName":pName,"typeId":typeId,"lowPrice":lowPrice,"highPrice":highPrice},
            type:"post",
            success:function () {
                //重新加载分页显示的组件table
                $("#table").load("http://localhost:5180/product.jsp #table");
            }
        })
    }
</script>

</html>