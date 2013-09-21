package com.therdl.server.restapi;

import com.google.inject.Singleton;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.UserService;
import com.therdl.server.data.FileData;
import com.therdl.shared.beans.Beanery;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 */

@Singleton
public class UploadServlet extends HttpServlet {

    private static org.slf4j.Logger sLogger = LoggerFactory.getLogger(UploadServlet.class);
    private final Provider<HttpSession> sessions;
    private Beanery beanery;
    private String avatarText;

    @Inject
    public UploadServlet(Provider<HttpSession> sessions , UserService userService) {
        this.sessions = sessions;
        beanery = AutoBeanFactorySource.create(Beanery.class);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)   throws ServletException, IOException {

        System.out.println( "UploadServlet is go ");
        ServletFileUpload upload = new ServletFileUpload();
        try{
            FileItemIterator iter = upload.getItemIterator(req);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();

                String name = item.getFieldName();
                System.out.println( "item.getFieldName() "+ name);
                if (name.equals("avatarText"))  {
                    avatarText = name;
                    continue;
                } // end image text

               else  if (name.equals("fileElement")) {
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

                FileData  fileData = new FileData(name, out.toByteArray(), "binary");

                }// end if file

                else {
                 continue;     // do nothing
                }
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }


}
