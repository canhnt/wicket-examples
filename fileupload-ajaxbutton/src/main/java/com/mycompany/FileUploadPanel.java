package com.mycompany;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

public class FileUploadPanel extends Panel {

    private boolean selectedFile;
    private AbstractDefaultAjaxBehavior onChangeAjaxBehavior;
    private AjaxButton ajaxButton;

    public FileUploadPanel(String id) {
        super(id);

        createForm();
    }

    private void createForm() {

        add(onChangeAjaxBehavior = new AbstractDefaultAjaxBehavior() {
            @Override
            protected void respond(AjaxRequestTarget target) {
                System.out.println("OnChange");
                selectedFile = !selectedFile;
                if (ajaxButton != null) {
                    target.add(ajaxButton);
                } else {
                    System.err.println("'ajaxButton' is null");
                }

            }
        });

        Form form;
        add(form = new Form("form"));

        form.add(ajaxButton = new AjaxButton("ajax-button",
                new StringResourceModel("select-file-label", FileUploadPanel.this, null)) {

            public boolean isEnabled() {
                return selectedFile;
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                selectedFile = !selectedFile;
                target.add(ajaxButton);
            }

            @Override
            protected String getOnClickScript()
            {
                return "alert('uploading selected file');" +
                        "$('#fileinput')[0].value=null;";
            }
        });
        ajaxButton.setLabel(new StringResourceModel("upload-button-label", this, null));

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        final String script = String.format("function updateButton(){%s}", onChangeAjaxBehavior.getCallbackScript());
        response.render(JavaScriptHeaderItem.forScript(script, "updatebutton-js"));
    }
}
