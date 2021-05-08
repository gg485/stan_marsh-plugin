<%@ page import="java.util.List" %>
<%@ page import="stan.marsh.plugin.domain.PluginInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Methods</title>
</head>
<body>
<%
    List<String>methods=(List<String>)request.getAttribute("methods");
    List<PluginInfo>plugins=(List<PluginInfo>)request.getAttribute("methods");
%>

<form>
    <p>select methods</p>
    <table width="200px" align="center" border="0" cellspacing="0">
        <tr>
            <td>#</td>
            <td>plugin name</td>
            <td></td>
        </tr>

        <c:forEach items="${methods}" var="method" varStatus="st"  >
            <tr>
                <td><c:out value="${st.count}" /></td>
                <td><c:out value="${method}" /></td>
                <td>
                    <input type="checkbox">
                </td>
            </tr>
        </c:forEach>
    </table>

    <input type="submit" value="select plugin" />

</form>

</body>
</html>
