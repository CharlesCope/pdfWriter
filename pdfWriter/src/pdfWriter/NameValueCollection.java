package pdfWriter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luc
 *
 */
public class NameValueCollection extends HashMap<String, List<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8607314746403705528L;
	private static final String COMMA = ",";
	
	/**
	 * @param pInitialCapacity
	 * @param pLoadFactor
	 */
	public NameValueCollection(int pInitialCapacity, float pLoadFactor) {
		super(pInitialCapacity, pLoadFactor);
	}

	/**
	 * @param pInitialCapacity
	 */
	public NameValueCollection(int pInitialCapacity) {
		super(pInitialCapacity);
	}

	/**
	 * 
	 */
	public NameValueCollection() {
		super();
	}

	/**
	 * @param pM
	 */
	public NameValueCollection(Map<? extends String, ? extends List<String>> pM) {
		super(pM);
	}
	
	public void add(String pKey, String pValue) {
		List<String> oValues = super.get(pKey);
		if (oValues == null) {
			oValues = new ArrayList<String>();
			super.put(pKey, oValues);
		}
		oValues.add(pValue);
	}

	public List<String> getValues(String pKey) {
		return super.get(pKey);
	}
	
	public String get(String pKey) {
		List<String> oValues = getValues(pKey);
		if (oValues == null) return null;
		StringBuffer oBuf = new StringBuffer();
		for (int i = 0; i < oValues.size(); i++) {
			oBuf.append(oValues.get(i));
			if ((i+1) < oValues.size()) oBuf.append(COMMA);
		}
		return oBuf.toString();
	}
	
	
	/**
	 * Override existing entry if any
	 * @param pKey
	 * @param pValue
	 */
	public void set(String pKey, String pValue) {
		List<String> oValues = new ArrayList<String>();
		super.put(pKey, oValues);
	}
}
