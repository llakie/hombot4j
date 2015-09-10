package de.bytevalue.hb4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import de.bytevalue.hb4j.json.JsonConnection;
import de.bytevalue.hb4j.json.JsonConnectionListenerPool;
import de.bytevalue.hb4j.json.JsonResponse;

public abstract class HombotModel extends JsonConnectionListenerPool<HombotModelListener> implements Serializable {
	public static final int VIRTUAL_RESPONSE_ID = -1000;
	private static final long serialVersionUID = 1L;

	@Override
	public final void onResponse(JsonConnection conn, JsonResponse response) {
		this.apply(response);
		super.onResponse(conn, response);
	}
	
	protected void triggerModelChange(int responseId) {
		for(HombotModelListener listener: this.listeners) {
			ThreadPool.getInstance().execute(() -> {
				listener.onModelChange(responseId);
			});
		}
	}
	
	private void apply(JsonResponse response) {
		BigInteger preParseChecksum = this.getChecksum();
		
		this.parse(response, response.getPayload());
		
		BigInteger postParseChecksum = this.getChecksum();
		
		if(preParseChecksum.compareTo(postParseChecksum) == 0) {
			return;
		}
		
		this.triggerModelChange(response.getHeader().getId());
	}
	
	/**
	 * @description
	 * Not very efficient method of comparing one model state to another, but the easiest one possible
	 * No matter how many fields the model contains. Make sure, that every data structure added to the model
	 * has to implement the java.io.Serializable interface
	 */
	private BigInteger getChecksum() {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(4096); ObjectOutputStream oos = new ObjectOutputStream(baos)){
			oos.writeObject(this);
			
			MessageDigest m = MessageDigest.getInstance("SHA1");
		    m.update(baos.toByteArray());

		    baos.flush();
		    
		    return new BigInteger(1, m.digest());
		}
		catch (IOException | NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return BigInteger.ZERO;
		}
	}
	
	protected void parseJsonArray(JSONArray array) {
		for(int i = 0; i < array.length(); i++) {
			if(array.optJSONArray(i) != null) {
				this.parseJsonArray(array.optJSONArray(i));
			}
			else if(array.optJSONObject(i) != null) {
				this.parseJsonObject(array.optJSONObject(i));
			}
		}
	}
	
	protected void parseJsonObject(JSONObject object) {
		Iterator<String> it = object.keys();
		
		while(it.hasNext()) {
			String key = it.next();
			
			if(object.optJSONArray(key) != null) {
				this.parseJsonArray(object.optJSONArray(key));
			}
			else if(object.optJSONObject(key) != null) {
				this.parseJsonObject(object.optJSONObject(key));
			}
			else if(object.optString(key) != null) {
				this.parseKeyValue(key, object.optString(key));
			}
		}
	}
	
	protected abstract void parse(JsonResponse response, JSONObject payload);
	protected abstract void parseKeyValue(String key, String value);
}