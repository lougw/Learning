package com.lougw.jettyserver;


import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by gaoweilou on 2017/5/26
 * Email.lgw0727@163.com
 */

public class ControllerServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);

        resp.setContentType("text/html");
        resp.getWriter().println("<h1>" + "aaaaaaaaaaaa" + "</h1>");

//
//
//        String callback = req.getParameter("callback");
//        String op = req.getParameter("direction");
//       // ControlManager.getInstance().sendWeChatKey(op);
//        String str = callback + "( {\n" +
//                "    \"statusCode\":1000,\n" +
//                "    \"statusDesc\":null,\n" +
//                "    \"result\":null,\n" +
//                "    \"thirdDatas\":null\n" +
//                "})\n";
//        byte[] b = str.getBytes();
//        resp.setContentLength(b.length);
//        OutputStream out = resp.getOutputStream();
//        out.write(b);
//        out.close();
    }
}
