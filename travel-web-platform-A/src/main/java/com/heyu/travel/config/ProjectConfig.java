package com.heyu.travel.config;


import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.util.EnumSet;

/**
 * @Description Web.xml 注解配置
 * 声明启动时加载的类
 */
public class ProjectConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * @Description  配置服务器启动时,需要注册的过滤器
     * @param servletContext servlet上下文环境
     *
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        //添加过滤器
        FilterRegistration.Dynamic dynamic =
                servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        dynamic.setInitParameter("encoding","utf-8");
        //设置需要过滤器,过滤的路径
        // 参数l: 拦截方式
        // 参数2: 加载顺序, 和web.xml文件相比,先加载谁
        // 参数3: 拦截的路径
        dynamic.addMappingForUrlPatterns(null,true,"/*");
       /* 对于servlet filter的代理，用这个类的好处主要是通过Spring容器来管理servlet filter的生命周期，
        还有就是如果filter中需要一些Spring容器的实例，可以通过spring直接注入，
        另外读取一些配置文件这些便利的操作都可以通过Spring来配置实现*/
        FilterRegistration.Dynamic springSessionRepositoryFilter = servletContext.addFilter("springSessionRepositoryFilter", DelegatingFilterProxy.class);
        springSessionRepositoryFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR),true, "/*");
    }


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {SpringConfig.class,MybatisConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {PlatformSpringMvcConfig.class};
    }


    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
