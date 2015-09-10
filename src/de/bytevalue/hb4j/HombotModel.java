package de.bytevalue.hb4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;

import de.bytevalue.hb4j.json.JsonConnection;
import de.bytevalue.hb4j.json.JsonConnectionListener;
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
			JsonConnectionListener.threadPool.execute(() -> {
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
	
	protected abstract void parse(JsonResponse response, JSONObject payload);
}