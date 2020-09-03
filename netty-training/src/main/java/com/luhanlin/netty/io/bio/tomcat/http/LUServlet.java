package com.luhanlin.netty.io.bio.tomcat.http;

public abstract class LUServlet {
    public void service(LURequest request, LUResponse response) throws Exception{
        if("GET".equalsIgnoreCase(request.getMethod())){
            doGet(request,response);
        }else{
            doPost(request,response);
        }
    }

    public abstract void doGet(LURequest request, LUResponse response) throws Exception;
    public abstract void doPost(LURequest request, LUResponse response) throws Exception;
}
