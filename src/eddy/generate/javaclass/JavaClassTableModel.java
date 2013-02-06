package eddy.generate.javaclass;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class JavaClassTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	final String[] names = { "¿‡–Õ", "√˚≥∆" };
	private ArrayList<JavaClass> javaClassList = new ArrayList<JavaClass>();

	public void clearData() {
		javaClassList.clear();
	}

	public ArrayList<JavaClass> getJavaClassList() {
		return javaClassList;
	}

	public void addClass(JavaClass jc) {
		this.javaClassList.add(jc);
	}

	public String getColumnName(int column) {
		return names[column];
	}

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return javaClassList.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		JavaClass jc = javaClassList.get(rowIndex);
		if (columnIndex == 0)
			return jc.getFieldClass();
		return jc.getFieldName();
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}

	public void setValueAt(Object aValue, int row, int column) {
		JavaClass jc = javaClassList.get(row);
		if (column == 0) 
			jc.setFieldClass((String) aValue);
		else 
			jc.setFieldName((String) aValue);
	}
}
