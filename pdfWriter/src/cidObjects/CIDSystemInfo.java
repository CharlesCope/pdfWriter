package cidObjects;

public class CIDSystemInfo {
	private final String PDFCRLF = "\r\n";
	private StringBuilder sbCIDSystemInfoDic = new StringBuilder();
	private String strRegistry="";
	private String strOrdering="";
	private int intSupplement;
	
	/** The constructors  for the class	 */
	public CIDSystemInfo(){}
	
	/** Property Getters */
	public String getRegistry() {return strRegistry;	}
	public String getOrdering() {return strOrdering;}
	public int getSupplement() {return intSupplement;}
	
	/** Property Setters */	
	public void setRegistry(String strRegistry) {this.strRegistry = strRegistry;}
	public void setOrdering(String strOrdering) {this.strOrdering = strOrdering;}
	public void setSupplement(int intSupplement) {this.intSupplement = intSupplement;}

	public String toString(){
		sbCIDSystemInfoDic.append("<< " );// Start of dictionary
		sbCIDSystemInfoDic.append("/Registry " + strRegistry + PDFCRLF);
		sbCIDSystemInfoDic.append("/Ordering " + strOrdering+ PDFCRLF);
		sbCIDSystemInfoDic.append("/Supplement " + Integer.toString(intSupplement)+ PDFCRLF);
		sbCIDSystemInfoDic.append(">> " + PDFCRLF);// End of dictionary
		return sbCIDSystemInfoDic.toString();
	}
	
}
