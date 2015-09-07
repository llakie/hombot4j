package de.bytevalue.hb4j;

import de.bytevalue.hb4j.json.JsonConnection;
import de.bytevalue.hb4j.json.JsonConnectionListenerPool;
import de.bytevalue.hb4j.json.JsonResponse;

public abstract class HombotModel extends JsonConnectionListenerPool<HombotModelListener> {
	@Override
	public final void onResponse(JsonConnection conn, JsonResponse response) {
		this.apply(response);
		super.onResponse(conn, response);
	}
	
	protected abstract void apply(JsonResponse response);
}