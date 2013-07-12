package com.therdl.server.restapi;


import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Singleton
public class SnipService  extends HttpServlet {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(SnipService.class);
    Beanery beanery;

    @Inject
    public SnipService() {

        beanery = AutoBeanFactorySource.create(Beanery.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)   throws ServletException, IOException {
        resp.setContentType("application/json");
        SnipBean bean =  beanery.snipBean().as();
        bean.setAuthor("Demo Author");
        bean.setContent("demo content");
        bean.setStream("demo stream");
        bean.setTitle("demo title");
        bean.setTimeStamp("demo  timestamp");

        AutoBean<SnipBean> autoBean = AutoBeanUtils.getAutoBean(bean);
        String asJson = AutoBeanCodex.encode(autoBean).getPayload();
        sLogger.info(asJson);
        PrintWriter out = resp.getWriter();
        out.write(asJson);

    }


}
