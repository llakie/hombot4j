package de.bytevalue.hb4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.bytevalue.hb4j.json.JsonConnection;
import de.bytevalue.hb4j.json.JsonConnectionListenerPool;
import de.bytevalue.hb4j.json.JsonResponse;

public abstract class HombotModel extends JsonConnectionListenerPool<HombotModelListener> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public final void onResponse(JsonConnection conn, JsonResponse response) {
		this.apply(response);
		super.onResponse(conn, response);
	}
	
	public final BigInteger getChecksum() {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)){
			oos.writeObject(this);
			
			MessageDigest m = MessageDigest.getInstance("SHA1");
		    m.update(baos.toByteArray());

		    return new BigInteger(1, m.digest());
		}
		catch (IOException | NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return BigInteger.ZERO;
		}
	}
	
	protected abstract void apply(JsonResponse response);
}