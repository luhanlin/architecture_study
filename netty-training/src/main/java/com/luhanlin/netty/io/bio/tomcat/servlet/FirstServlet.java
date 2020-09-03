package com.luhanlin.netty.io.bio.tomcat.servlet;


import com.luhanlin.netty.io.bio.tomcat.http.LURequest;
import com.luhanlin.netty.io.bio.tomcat.http.LUResponse;
import com.luhanlin.netty.io.bio.tomcat.http.LUServlet;

public class FirstServlet extends LUServlet {

	public void doGet(LURequest request, LUResponse response) throws Exception {
		this.doPost(request, response);
	}

	public void doPost(LURequest request, LUResponse response) throws Exception {
		response.write("This is First Serlvet");
	}

}
