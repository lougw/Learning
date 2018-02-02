package com.lougw.jettyserver;

import android.view.View;

import org.eclipse.jetty.util.ByteArrayISO8859Writer;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by gaoweilou on 2017/5/26
 * Email.lgw0727@163.com
 */

public class LinkStatusServlet extends BaseServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        int page = req.getParameter("page")==null?0:Integer.parseInt(req.getParameter("page"));
        String direction = req.getParameter("direction");
        if (page >= 0) {
            if ("up".equals(direction)) {
                SpeechManager.getInstance().notifySpeechMessage(page, View.FOCUS_UP);
            } else {
                SpeechManager.getInstance().notifySpeechMessage(page, View.FOCUS_DOWN);
            }
        }
        resp.setContentType("text/html;charset=UTF-8");
        ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer(1500);
        writer.write("<html>");
        writer.write("<body>");
        writer.write("<form>");
        writer.write("<div style=\"text-align:center\">");
        writer.write("page:<input type=\"number\" min=\"0\" name=\"page\"> <br />");
        writer.write("<input type=\"radio\" name=\"direction\" value=\"up\" />up");
        writer.write("<input type=\"radio\" checked=\"true\" name=\"direction\" value=\"down\" /> down<br />");
        writer.write("<button style=\"text-align:center\" type=\"submit\">button</button><br />");
        writer.write("</div>");
        writer.write("</form>");
        writer.write("</body>");
        writer.write("</html>");
        writer.flush();
        resp.setContentLength(writer.size());
        OutputStream out = resp.getOutputStream();
        writer.writeTo(out);
        out.close();
    }
}
