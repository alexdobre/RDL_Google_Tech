package com.therdl.client.view.widget.editor;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.widget.SnipEditorWorkflow;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.beans.SnipBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DynamicPostListBox extends Composite {

    private static Logger log = Logger.getLogger("");

    final SnipEditorWorkflow snipEditorWorkflow;

    private String title = "Select a Post";

    private ListBox dropBox;
    private Map<String, AutoBean<SnipBean>> snipMap;
    private AutoBean<SnipBean>  currentBean;
    private Beanery beanery = GWT.create(Beanery.class);

    public DynamicPostListBox(final SnipEditorWorkflow snipEditorWorkflow) {
        this.snipEditorWorkflow = snipEditorWorkflow;
        dropBox = new ListBox(false);

        FlowPanel dropBoxPanel = new FlowPanel();
        dropBoxPanel.add(new HTML(title));
        dropBoxPanel.add(dropBox);
        initWidget(dropBoxPanel);
        dropBoxPanel.setStyleName("postListBoxRightBar");

        dropBox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {


                int k = dropBox.getSelectedIndex();
                String key = dropBox.getValue(k);

                currentBean =  snipMap.get(key);

                snipEditorWorkflow.setContent(currentBean.as().getContentAsHtml());
                snipEditorWorkflow.setEditorTitle(currentBean.as().getTitle());
             //   onSubmit();

            }
        });

    }


    public void addBeans(List<AutoBean<SnipBean>> beans) {
        log.info("DynamicPostListBox addBeans adding this many beans " + beans.size());
        if(beans.size()==0) return;
    //    if (snipMap !=null) snipMap.clear();
        // set up the snip map to store for the beans for reuse
        dropBox.clear();

        snipMap = new HashMap<String, AutoBean<SnipBean>>();
        log.info("DynamicPostListBox addBeans " + beans.get(0).as().getId() );
        log.info("DynamicPostListBox addBeans " + beans.get(0).as().getTitle());
        // populate the map and the drop down

        for (AutoBean<SnipBean> bean : beans) {
            String title =  bean.as().getTitle();
            String id =  bean.as().getId();

            snipMap.put(bean.as().getId(), bean);
            dropBox.addItem(title , id );
            log.info("DynamicPostListBox: addBeans added bean with title" + title);
            log.info("DynamicPostListBox: addBeans snipMap.size()" + snipMap.size());
        }
        log.info("DynamicPostListBox addBeans snipMap has this many beans " + beans.size());
        beans.clear();
    }


    public void  onSubmit() {
        log.info("DynamicPostListBox: onSubmit new bean ");
       // will have no id for now
        AutoBean<SnipBean> newBean =   beanery.snipBean();
        snipEditorWorkflow.submitBean(newBean);


    }


    public void  onSubmitEdit() {
        log.info("DynamicPostListBox: SubmitEdit editing bean ");
        if(currentBean == null) {
            log.info("DynamicPostListBox: SubmitEdit editing bean currentBean == null ");

            Window.alert("this is a new snip , please use 'new post " +
                    "submit to create a new post" );
            return;
        }

        if(currentBean.as().getId() == null) {
            log.info("DynamicPostListBox: SubmitEdit editing bean currentBean.as().getId() == null ");
            Window.alert("this is a new snip, please use 'new post " +
                    "submit to create a new post" );
            return;
        }

            log.info("DynamicPostListBox: onSubmit editing bean " + currentBean.as().getTitle());
            snipEditorWorkflow.submitEditBean(currentBean);


    }


    public void onDelete() {

        if(currentBean == null) {
            Window.alert("this is a new snip nothing to delete, select new post to clear the editor instead" );
            return;
        }

        if(currentBean.as().getId() == null) {
            Window.alert("this is a new snip nothing to delete, select new post to clear the editor instead" );
            return;
        }


        snipEditorWorkflow.onDelete( currentBean.as().getId());



    }


    public void clear() {

        dropBox.clear();

    }
}
