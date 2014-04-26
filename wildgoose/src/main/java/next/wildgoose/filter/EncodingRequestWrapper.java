package next.wildgoose.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import next.wildgoose.utility.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EncodingRequestWrapper extends HttpServletRequestWrapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(EncodingRequestWrapper.class.getName());
	
	private String encodingType;
	private Map<String, String[]> parameters;

	
	public EncodingRequestWrapper(HttpServletRequest request, String encodingType) {
		super(request);
		this.encodingType = encodingType;
		this.parameters = encodeParameters(super.getParameterMap());
	}

	private Map<String, String[]> encodeParameters(Map<String, String[]> originalParam) {
		
		Map<String, String[]> encodedParam = new HashMap<String, String[]>();
		
		// 기존 originalParameter 순회하여 encoding후 encodedParam에 담음
		Iterator<String> originalParamIr = originalParam.keySet().iterator();
		while (originalParamIr.hasNext()) {
			String key = originalParamIr.next();
			String[] values = originalParam.get(key);
			LOGGER.debug(key);
			encodedParam.put(key, encodeValues(values));
		}
		
		return encodedParam;
	}
	
	// valueArr를 순회하면서 encoding
	private String[] encodeValues(String[] valueArr) {
		
		List<String> values = Arrays.asList(valueArr);
		List<String> encodedValues = new ArrayList<String>();
		
		Iterator<String> valuesIr = values.iterator();
		while (valuesIr.hasNext()) {
			encodedValues.add(Utility.encode(valuesIr.next(), encodingType));
		}
		LOGGER.debug(encodedValues.toString());
		return encodedValues.toArray(new String[0]);
	}

	@Override
	public String getParameter(String name) {
		String[] values = this.parameters.get(name);
		
		if (values == null || values.length == 0) {
			return null;
		}
		
		return values[0];
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.parameters;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(this.parameters.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return this.parameters.get(name);
	}

}
