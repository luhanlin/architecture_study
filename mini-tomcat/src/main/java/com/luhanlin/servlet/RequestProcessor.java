package com.luhanlin.servlet;

import com.luhanlin.entity.Request;
import com.luhanlin.entity.Response;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * 请求执行器
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class RequestProcessor extends Thread {

    private Socket socket;
    private Map<String,HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();

            // ��װRequest�����Response����
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // ��̬��Դ����
            if(servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            }else{
                // ��̬��Դservlet����
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }

            socket.close();

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
