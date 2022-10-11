package com.github.mihe7.difftoclipboard;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import static java.awt.datatransfer.DataFlavor.stringFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import  java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.JEditorPane;
import org.netbeans.api.diff.Diff;
import org.netbeans.api.diff.DiffView;
import org.netbeans.api.diff.StreamSource;
import org.openide.cookies.EditorCookie;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Diff",
        id = "com.github.mihe7.difftoclipboard.DiffToClipboardAction"
)
@ActionRegistration(
        displayName = "#CTL_DiffToClipboardAction"
)
@ActionReference(path = "Editors/text/x-java/Popup", position = 400)
@Messages("CTL_DiffToClipboardAction=Diff to Clipboard")
public final class DiffToClipboardAction implements ActionListener {

    private final EditorCookie context;
    
    public DiffToClipboardAction(EditorCookie context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        diff(getSelectedText(), getClipboardText());
    }
    
    private void diff(String sourceText, String destText) {
        if (sourceText == null || destText == null) return;
        
        try {
            StreamSource source = StreamSource.createSource("Selection", "Selection", "text/x-java", new StringReader(sourceText));
            StreamSource dest = StreamSource.createSource("Clipboard", "Clipboard", "text/x-java", new StringReader(destText));
            DiffView view = Diff.getDefault().createDiff(source, dest);
            show(view);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void show(DiffView view) {
        TopComponent tc = new TopComponent();
        tc.setLayout(new BorderLayout());
        tc.add(view.getComponent(), BorderLayout.CENTER);
        tc.setName("MY_DIFF");
        tc.setDisplayName("Diff to Clipbaord");
        tc.open();
        tc.requestActive();
    }

    private String getSelectedText() {
        return getSelectedText(getCurrentEditor());
    }
    
    private JEditorPane getCurrentEditor() {
        JEditorPane[] editors =  context.getOpenedPanes();
        if (editors.length == 0) {
            return null;            
        }

        return editors[0];
    }    
    
    private String getSelectedText(JEditorPane editor) {
        if (editor == null) {
            return null;
        }

        String text = editor.getSelectedText(); 
        if (text == null) {
            text = editor.getText();
        }
        return text;
    }
        
    private String getClipboardText() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            if (cb != null && cb.isDataFlavorAvailable(stringFlavor)) {
                return (String) cb.getData(stringFlavor);
            }
            return null;
        } catch (UnsupportedFlavorException | IOException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
}
