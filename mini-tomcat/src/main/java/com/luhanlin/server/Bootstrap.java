package com.luhanlin.server;

import com.luhanlin.mapper.Context;
import com.luhanlin.mapper.Host;
import com.luhanlin.mapper.Mapper;
import com.luhanlin.mapper.Wrapper;
import com.luhanlin.servlet.HttpServlet;
import com.luhanlin.servlet.RequestProcessor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 启动类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class Bootstrap {

    private static final int PORT = 8080;

    private static final Map<String,HttpServlet> servletMap = new HashMap<String,HttpServlet>();

    private ThreadPoolExecutor threadPoolExecutor;

    private Mapper mapper;

    public Bootstrap() {
        this.threadPoolExecutor = new ThreadPoolExecutor(10,
                50,
                100L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(50));
        this.mapper = new Mapper();
    }

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.startServer();
    }

    private void startServer() throws Exception {
        // 加载解析相关的配置，web.xml
        loadServer();
        /*
            完成Minicat 1.0版本
            需求：浏览器请求http://localhost:8080,返回一个固定的字符串到页面"Hello Minicat!"
         */
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("=====>>>Minicat start on port：" + PORT);

        /*while(true) {
            Socket socket = serverSocket.accept();
            // 有了socket，接收到请求，获取输出流
            OutputStream outputStream = socket.getOutputStream();
            String data = "Hello my tomcat !";
            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
            outputStream.write(responseText.getBytes());
            socket.close();
        }*/


        /**
         * 完成Minicat 2.0版本
         * 需求：封装Request和Response对象，返回html静态资源文件
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            response.outputHtml(request.getUrl());
            socket.close();
        }*/

        /**
         * 完成Minicat 3.0版本
         * 需求：可以请求动态资源（Servlet）
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // 静态资源处理
            if(servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            }else{
                // 动态资源servlet请求
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }

            socket.close();

        }*/

        /**
         多线程改造（不使用线程池）
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,servletMap);
            requestProcessor.start();
        }*/

        System.out.println("=========>>>>>>使用线程池进行多线程改造");
        /**
         多线程改造（使用线程池）
         */
        while(true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,servletMap);
            threadPoolExecutor.execute(requestProcessor);
        }
    }

    private void loadServer() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//Host");
            for (int i = 0; i < selectNodes.size(); i++) {

                Host host = new Host();

                Element element = selectNodes.get(i);
                String hostName = element.attributeValue("name");
                String appBase = element.attributeValue("appBase");
                // 扫描appBase下的文件夹，每一个文件夹认为是一个项目（Context）
                File appBaseFolder = new File(appBase);
                File[] files = appBaseFolder.listFiles();
                for(File file: files) {
                    if(file.isDirectory()) {
                        Context context = new Context();
                        String contextPath = file.getName();
                        context.setPath(contextPath);
                        // 构建Wrappers，一个Wrapper对应一个Servlet
                        File webFile = new File(file,"web.xml");
                        List<Wrapper> list = loadWebXml(webFile.getAbsolutePath());
                        context.setWrappers(list);
                        host.getContexts().add(context);
                    }
                }

                host.setName(hostName);
                mapper.getHosts().add(host);
            }

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Wrapper> loadWebXml(String webXmlPath) throws FileNotFoundException {
        List<Wrapper> list = new ArrayList<>();
        InputStream resourceAsStream = new FileInputStream(webXmlPath);
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            MyClassLoader myClassLoader = new MyClassLoader();
            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element =  selectNodes.get(i);
                // <servlet-name>lagou</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                // <servlet-class>server.LagouServlet</servlet-class>
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();


                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                Class<?> aClass = myClassLoader.findClass(webXmlPath.replace("web.xml", "")  + "/"+servletClass);
                HttpServlet servlet = (HttpServlet) aClass.newInstance();
                Wrapper wrapper = new Wrapper();
                wrapper.setUrlPattern(urlPattern);
                wrapper.setServlet(servlet);

                list.add(wrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}