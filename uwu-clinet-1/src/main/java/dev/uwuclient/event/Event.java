package dev.uwuclient.event;

import dev.uwuclient.UwUClient;

public class Event {
	public boolean cancelled = false;
	
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled){
		this.cancelled = cancelled;
	}

	public void call(){
		UwUClient.INSTANCE.modManager.onEvent(this);
	}
}
