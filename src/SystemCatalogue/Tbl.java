package SystemCatalogue;

public class Tbl {
	String tblName;
	int tblID;
	String fragmentationType;
	String columnNames;
	String columnTypes;
	String columnConstraints;
	String primaryKeys;
	String foriegnKeyConstraints;
	public String getTblName() {
		return tblName;
	}
	public void setTblName(String tblName) {
		this.tblName = tblName;
	}
	public int getTblID() {
		return tblID;
	}
	public void setTblID(int tblID) {
		this.tblID = tblID;
	}
	public String getFragmentationType() {
		return fragmentationType;
	}
	public void setFragmentationType(String fragmentationType) {
		this.fragmentationType = fragmentationType;
	}
	public String getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}
	public String getColumnTypes() {
		return columnTypes;
	}
	public void setColumnTypes(String columnTypes) {
		this.columnTypes = columnTypes;
	}
	public String getColumnConstraints() {
		return columnConstraints;
	}
	public void setColumnConstraints(String columnConstraints) {
		this.columnConstraints = columnConstraints;
	}
	public String getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(String primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	public String getForiegnKeyConstraints() {
		return foriegnKeyConstraints;
	}
	public void setForiegnKeyConstraints(String foriegnKeyConstraints) {
		this.foriegnKeyConstraints = foriegnKeyConstraints;
	}
	
	
}
