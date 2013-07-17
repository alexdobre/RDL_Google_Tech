package com.therdl.client.view.widget.editor;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

    private List<AutoBean<SnipBean>> beans;
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
                onSubmit();

            }
        });

    }


    public void addBeans(List<AutoBean<SnipBean>> beans) {

        this.beans = beans;

        // set up the snip map to store for the beans for reuse

        snipMap = new HashMap<String, AutoBean<SnipBean>>();

        // populate the map and the drop down

        for (AutoBean<SnipBean> bean : beans) {

            snipMap.put(bean.as().getId(), bean);

            dropBox.addItem(bean.as().getTitle(), bean.as().getId());

        }
    }


    public void  onSubmit() {

         if(currentBean != null)  {
         log.info("DynamicPostListBox: onSubmit editing bean " + currentBean.as().getTitle());
             snipEditorWorkflow.submitBean(currentBean);

         }

       // create new empty bean
        snipEditorWorkflow.submitBean(beanery.snipBean());
    }




}
