/**
 * Distributed Systems
 * Robotic Arm Controller
 * 01.05.2019
 * Luis Nickel & Leo Peters
 */

package nameserver;

import java.util.LinkedList;

import structure.Address;
import structure.ConnectionNameserver;
import structure.Constants;

/**
 * This is the nameserver for the control system. Servers can
 * {@link #register()} themselves and the nameserver will hold a list of all
 * registered servers. It will provide the addresses for these servers in a
 * list. Clients can get the complete list by calling {@link #update()}.
 * 
 * @author acd219
 *
 */
public class Nameserver {

	private LinkedList<ConnectionNameserver> racList = new LinkedList<ConnectionNameserver>();
	private LinkedList<ConnectionNameserver> philoList = new LinkedList<ConnectionNameserver>();
	private LinkedList<ConnectionNameserver> viewList = new LinkedList<ConnectionNameserver>();
	private Thread t;

	public Nameserver() {
		startAging();
	}

	private void startAging() {
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!t.isInterrupted()) {
					try {
						Thread.sleep(Constants.SLEEP_TIME_MS_NAMESERVER);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < racList.size(); i++) {
						racList.get(i).decreaseCounter();

						if (racList.get(i).getCounter() == 0) {
							synchronized (racList) {
								System.out.println("RAC: "+racList.getLast().getAddress().getId() + " wurde entfernt");
								racList.remove(i);	
							}
						}
					}
					
					for (int i = 0; i < philoList.size(); i++) {
						philoList.get(i).decreaseCounter();

						if (philoList.get(i).getCounter() == 0) {
							synchronized (philoList) {
								System.out.println("Philo: "+philoList.get(i).getAddress().getId() + " wurde entfernt");
								philoList.remove(i);	
							}
						}
					}
					
					for (int i = 0; i < viewList.size(); i++) {
						viewList.get(i).decreaseCounter();

						if (viewList.get(i).getCounter() == 0) {
							synchronized (viewList) {
								System.out.println("View: "+viewList.get(i).getAddress().getId() + " wurde entfernt");
								viewList.remove(i);	
							}
						}
					}
				}
			}
		});
		t.start();
	}

	
	public int registerRac(int id, String ipAddress, int port) {
		int errorCode = 1; // 1 - accepted; 2 - changed Name; 3 - name already existing

		for (int i = 0; i < racList.size(); i++) {
			if (racList.get(i).getAddress().getId()==id	&& racList.get(i).getAddress().getIpAddress().equals(ipAddress)
					&& racList.get(i).getAddress().getPort() == port) {
				racList.get(i).setCounter(Constants.COUNTER_START);
				return errorCode;
			} else if (racList.get(i).getAddress().getId()==id) {
				errorCode = 3;
				return errorCode;
			} else if (!(racList.get(i).getAddress().getId()==id)
					&& racList.get(i).getAddress().getIpAddress().equals(ipAddress)
					&& racList.get(i).getAddress().getPort() == port) {
				errorCode = 2;
				racList.get(i).getAddress().setId(id);
				return errorCode;
			}
		}
		Address addressEntry = new Address(id, ipAddress, port);
		synchronized (racList) {
			racList.add(new ConnectionNameserver(addressEntry));
			System.out.println("RAC: "+racList.getLast().getAddress().getId() + " wurde hinzugefuegt");
		}
		return errorCode;
	}

	public int registerPhilo(int id, String ipAddress, int port) {
		int errorCode = 1; // 1 - accepted; 2 - changed Name; 3 - name already existing

		for (int i = 0; i < philoList.size(); i++) {
			if (philoList.get(i).getAddress().getId()==id
					&& philoList.get(i).getAddress().getIpAddress().equals(ipAddress)
					&& philoList.get(i).getAddress().getPort() == port) {
				philoList.get(i).setCounter(Constants.COUNTER_START);
				return errorCode;
			} else if (philoList.get(i).getAddress().getId()==id) {
				errorCode = 3;
				return errorCode;
			} else if (!(philoList.get(i).getAddress().getId()==id)
					&& philoList.get(i).getAddress().getIpAddress().equals(ipAddress)
					&& philoList.get(i).getAddress().getPort() == port) {
				errorCode = 2;
				philoList.get(i).getAddress().setId(id);
				return errorCode;
			}
		}
		Address addressEntry = new Address(id, ipAddress, port);
		synchronized (philoList) {
			philoList.add(new ConnectionNameserver(addressEntry));
			System.out.println("Philo: "+philoList.getLast().getAddress().getId() + " wurde hinzugefuegt");
		}
		return errorCode;
	}
	
	public int registerView(int id, String ipAddress, int port) {
		int errorCode = 1; // 1 - accepted; 2 - changed Name; 3 - name already existing

		for (int i = 0; i < viewList.size(); i++) {
			if (viewList.get(i).getAddress().getId()==id
					&& viewList.get(i).getAddress().getIpAddress().equals(ipAddress)
					&& viewList.get(i).getAddress().getPort() == port) {
				viewList.get(i).setCounter(Constants.COUNTER_START);
				return errorCode;
			} else if (viewList.get(i).getAddress().getId()==id) {
				errorCode = 3;
				return errorCode;
			} else if (!(viewList.get(i).getAddress().getId()==id)
					&& viewList.get(i).getAddress().getIpAddress().equals(ipAddress)
					&& viewList.get(i).getAddress().getPort() == port) {
				errorCode = 2;
				viewList.get(i).getAddress().setId(id);
				return errorCode;
			}
		}
		Address addressEntry = new Address(id, ipAddress, port);
		synchronized (viewList) {
			viewList.add(new ConnectionNameserver(addressEntry));
			System.out.println("View: "+viewList.getLast().getAddress().getId() + " wurde hinzugefuegt");
		}
		return errorCode;
	}
	@Override
	protected void finalize() {
		t.interrupt();
	}


	public LinkedList<Address> getRacList() {
		LinkedList<Address> list = new LinkedList<Address>();
		for (ConnectionNameserver i : racList) {
			list.add(i.getAddress());
		}
		return list;
	}
	public LinkedList<Address> getViewList() {
		LinkedList<Address> list = new LinkedList<Address>();
		for (ConnectionNameserver i : viewList) {
			list.add(i.getAddress());
		}
		return list;
	}
	public LinkedList<Address> getPhiloList() {
		LinkedList<Address> list = new LinkedList<Address>();
		for (ConnectionNameserver i : philoList) {
			list.add(i.getAddress());
		}
		return list;
	}
}
