package com.aaron.mbpet;

import java.util.HashSet;
import java.util.Set;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.vaadin.client.ui.ui.UIConnector;
import com.vaadin.server.widgetsetutils.ConnectorBundleLoaderFactory;
import com.vaadin.shared.ui.Connect.LoadStyle;

public class OptimizedConnectorBundleLoaderFactory extends
            ConnectorBundleLoaderFactory {
    private Set<String> eagerConnectors = new HashSet<String>();
    {
            eagerConnectors.add(com.vaadin.client.ui.ui.UIConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.passwordfield.PasswordFieldConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.textfield.TextFieldConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.button.ButtonConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.orderedlayout.HorizontalLayoutConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.label.LabelConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.window.WindowConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.form.FormConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.formlayout.FormLayoutConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.panel.PanelConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.menubar.MenuBarConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.tree.TreeConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.customcomponent.CustomComponentConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.gridlayout.GridLayoutConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.table.TableConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.csslayout.CssLayoutConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.combobox.ComboBoxConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.tabsheet.TabsheetConnector.class.getName());
            eagerConnectors.add(org.vaadin.aceeditor.client.AceEditorConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.splitpanel.HorizontalSplitPanelConnector.class.getName());
            eagerConnectors.add(org.vaadin.diagrambuilder.client.DiagramBuilderConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.JavaScriptComponentConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.image.ImageConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.browserframe.BrowserFrameConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.progressindicator.ProgressBarConnector.class.getName());
            eagerConnectors.add(com.vaadin.client.ui.treetable.TreeTableConnector.class.getName());
    }

    @Override
    protected LoadStyle getLoadStyle(JClassType connectorType) {
            if (eagerConnectors.contains(connectorType.getQualifiedBinaryName())) {
                    return LoadStyle.EAGER;
            } else {
                    // Loads all other connectors immediately after the initial view has
                    // been rendered
                    return LoadStyle.DEFERRED;
            }
    }
}
