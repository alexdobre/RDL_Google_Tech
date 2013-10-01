package com.therdl.server.restapi;

import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.UserService;
import com.therdl.server.data.FileData;
import com.therdl.server.data.FileStorage;
import com.therdl.server.data.LocalFileStorage;
import com.therdl.shared.beans.AuthUserBean;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

/**
 */

@Singleton
public class UploadServlet extends HttpServlet {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(UploadServlet.class);
    private final Provider<HttpSession> session;

    private Beanery beanery;

    private String avatarText;
    private  FileStorage pictureStorage;

    @Inject
    public UploadServlet(Provider<HttpSession> session , UserService userService) {
        this.session = session;
        beanery = AutoBeanFactorySource.create(Beanery.class);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)   throws ServletException, IOException {

        System.out.println( "UploadServlet is go ");
        String userId = (String) session.get().getAttribute("userid" );

        ServletFileUpload upload = new ServletFileUpload();
        try{
            FileItemIterator iter = upload.getItemIterator(req);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();

                String name = item.getFieldName();
                System.out.println( "item.getName(); "+ item.getName());

               if (name.equals("fileElement")) {
                InputStream stream = item.openStream();


                // Process the input stream
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int len;
                byte[] buffer = new byte[8192];
                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }

                int maxFileSize = 10*(1024*1024); //10 megs max
                if (out.size() > maxFileSize) {
                    throw new RuntimeException("File is > than " + maxFileSize);
                }

                System.out.println( "UploadServlet file size "+ out.size());
                System.out.println( "UploadServlet user id "+ userId);
                System.out.println( "UploadServlet getContentType "+ item.getContentType());
                System.out.println("UploadServlet   getServletContext().getRealPath() " + getServletContext().getRealPath("/"));
                String contextRoot = getServletContext().getRealPath("/");

                String  contentType =  item.getContentType();
                String fileExtension =  contentType.substring(contentType.indexOf("/")+1);
                System.out.println( "UploadServlet fileExtension "+ fileExtension);
                FileData  fileData = new FileData(userId, out.toByteArray(), "binary");
                // url to find correct directory for file upload on server
                String avatarDirUrl = contextRoot+ File.separator+"userAvatar";
                System.out.println( "UploadServlet avatar DirUrl "+ avatarDirUrl);
                pictureStorage = new LocalFileStorage(avatarDirUrl);
                pictureStorage.storeFile(fileData, fileExtension);


                }  // end if file

              else continue;
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

         // can get the user id from the session
        String userName = (String) session.get().getAttribute("name");

        String avatarUrl = "userAvatar"+ File.separator+ userName+"small.jpg";

        AutoBean<AuthUserBean> actionBean = beanery.authBean();
        actionBean.as().setAction("ok");
        actionBean.as().setAvatarUrl(avatarUrl);
        PrintWriter out = resp.getWriter();
        out.write(AutoBeanCodex.encode(actionBean).getPayload());


    }

}
