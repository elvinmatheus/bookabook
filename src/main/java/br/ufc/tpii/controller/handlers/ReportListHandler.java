package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;

import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.Reports.Type;
import br.ufc.tpii.view.components.ReportList;

public class ReportListHandler implements ActionListener {

    /** Gera o popup de relatório apropriado conforme o item selecionado no combobox "chooser".
     * A janela é definida pela classe ReportList.
     */

    private JComboBox<String> chooser;
    private JTextField from, to;
    private JCheckBox complete;
    public ReportListHandler(JComboBox<String> chooser, JTextField from, JTextField to, JCheckBox complete) {
        this.chooser = chooser;
        this.from = from;
        this.to = to;
        this.complete = complete;
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        App app = App.get();
        int selectedIndex = this.chooser.getSelectedIndex() - 1;
        if (selectedIndex < 0) return;
        String fromText = this.from.getText();
        String toText = this.to.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fromTime = null, toTime = null;
        // quando o checkbox "Relatório completo" estiver marcado,
        // fromTime == toTime == null, o que significa todo o intervalo de tempo
        if (!this.complete.isSelected()) {
            try {
                if (!fromText.isBlank()) fromTime = LocalDate.parse(fromText, formatter);
                if (!toText.isBlank()) toTime = LocalDate.parse(toText, formatter);
            } catch (DateTimeParseException exc) {
                app.control().invoke(new DisplayPopupCmd("Formato de data deve seguir dd/MM/yyyy"));
                return;
            }
        }
        Type[] types = new Type[] {Type.EMPRESTIMO, Type.DEVOLUCAO, Type.BOOK_REGISTER, Type.USER_REGISTER, Type.USER_EDIT};
        JFrame frame = new JFrame();
        BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.setLayout(layout);
        JComponent reportList = new ReportList(app, types[selectedIndex], fromTime, toTime).paint();
        JComponent wrapper = Box.createVerticalBox();
        wrapper.add(Margin.rigidVertical(30));
        wrapper.add(Margin.horizontal(reportList, 100));
        wrapper.add(Margin.rigidVertical(30));
        frame.add(wrapper);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
