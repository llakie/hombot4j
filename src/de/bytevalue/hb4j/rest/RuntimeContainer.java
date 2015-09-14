package de.bytevalue.hb4j.rest;

import de.bytevalue.hb4j.Hombot;

public class RuntimeContainer<T extends Hombot<?>> {
	public T hombot;
	
	public RuntimeContainer(T hombot) {
		this.hombot = hombot;
	}
}
