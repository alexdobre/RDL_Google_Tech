package com.therdl.server.api;



import com.therdl.shared.beans.SnipBean;

import java.util.List;

public interface SnipsService {


         SnipBean getSnip(String id);

         void createSnip(SnipBean snip);

         // get all snips for a user
         List<SnipBean> getAllSnips(String id);


         void deleteSnip(String id);

         SnipBean updateSnip(SnipBean snip);

         //  testing and development methods debug string
         String getDebugString();

         SnipBean getLastSnip(String id);


}
