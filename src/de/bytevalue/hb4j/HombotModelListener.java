package de.bytevalue.hb4j;

import de.bytevalue.hb4j.json.DefaultJsonConnectionListener;

public abstract class HombotModelListener extends DefaultJsonConnectionListener {
	public abstract void onModelChange(int requestId);
}