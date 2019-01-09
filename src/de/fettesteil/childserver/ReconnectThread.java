package de.fettesteil.childserver;

public class ReconnectThread implements Runnable {

	private String host;
	private int port;

	public ReconnectThread(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().interrupted()) {
			synchronized (this) {
				try {
					
					Thread.currentThread().sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void connect() throws Exception{
		
	}

}
