package com.therdl.server.api;

import com.therdl.server.model.Snip;

import java.util.List;

public interface SnipsService {


         Snip getSnip(String id);

         void createSnip(Snip snip);

         List<Snip> getAllLabs(String match);

         void deleteSnip(String id);

         Snip updateSnip(Snip snip);

         // debug string
         String getDebugString();


}
