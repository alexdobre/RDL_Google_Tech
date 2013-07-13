package com.therdl.server.api;



import com.therdl.shared.beans.SnipBean;

import java.util.List;

public interface SnipsService {


         SnipBean getSnip(String id);

         void createSnip(SnipBean snip);

         List<SnipBean> getAllSnips();


         void deleteSnip(String id);

         SnipBean updateSnip(SnipBean snip);

         // debug string
         String getDebugString();


}
