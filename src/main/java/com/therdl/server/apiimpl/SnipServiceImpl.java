package com.therdl.server.apiimpl;


import com.therdl.server.api.SnipsService;
import com.therdl.server.model.Snip;

import javax.inject.Singleton;
import java.util.List;


@Singleton
public class SnipServiceImpl implements SnipsService {

    // this code will eventually use various dao's for now dao code is embedded
    // dao methods will be moved to dao objets


    @Override
    public Snip getSnip(String id) {
        return null;
    }

    @Override
    public void createSnip(Snip snip) {

    }

    @Override
    public List<Snip> getAllLabs(String match) {
        return null;
    }

    @Override
    public void deleteSnip(String id) {

    }

    @Override
    public Snip updateSnip(Snip snip) {
        return null;

    }

    @Override
    public String getDebugString() {
        return "Snip Service wired up for guice injection ok";
    }


}
