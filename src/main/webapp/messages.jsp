<%@ page import="com.vladm.demoservlet.model.Message" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Messages</title>
</head>
<body>

<%
    List<Message> messageList = (List<Message>) request.getAttribute("messages");
%>

Your messages:
<% for(int i = 0; i < messageList.size(); i++) { %>
<div style="border-style: solid; border-width: 3px">
    <%=messageList.get(i).getText()%>
</div>
<% }
%>
</body>
</html>
