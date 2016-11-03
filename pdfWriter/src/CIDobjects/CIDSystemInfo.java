package CIDobjects;

public class CIDSystemInfo {
	final private String JavaNewLine = System.getProperty("line.separator");
	private StringBuilder sbCIDSystemInfoDic = new StringBuilder();
	private String strRegistry;
	private String strOrdering;
	private int intSupplement;
	
	/** The constructors  for the class	 */
	public CIDSystemInfo(){}
	
	public String getStrRegistry() {return strRegistry;	}

	public void setStrRegistry(String strRegistry) {this.strRegistry = strRegistry;}

	public String getStrOrdering() {return strOrdering;}

	public void setStrOrdering(String strOrdering) {this.strOrdering = strOrdering;}

	public int getIntSupplement() {return intSupplement;}

	public void setIntSupplement(int intSupplement) {this.intSupplement = intSupplement;}

	public String toString(){
		sbCIDSystemInfoDic.append("/Registry " + strRegistry + JavaNewLine);
		sbCIDSystemInfoDic.append("/Ordering " + strOrdering+ JavaNewLine);
		sbCIDSystemInfoDic.append("/Supplement " + Integer.toString(intSupplement)+ JavaNewLine);
		return sbCIDSystemInfoDic.toString();
	}
	
}
