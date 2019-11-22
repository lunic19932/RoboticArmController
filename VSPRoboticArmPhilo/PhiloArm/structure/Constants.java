package structure;

public class Constants {
	// GUI
	public static final int REFRESH_START_VALUE = 1000;

	// SCCP
	// Broadcast address values
	public static final String BROADCAST_ADDRESS = "255.255.255.255";
	public static final int NAMESERVER_PORT = 55555;

	// Headervalues for SCCP
	public static final int HEADER_SIZE = 13;
	public static final int MAX_PACKET_SIZE = HEADER_SIZE + 500;
	public static final int MAX_DATA_SIZE = MAX_PACKET_SIZE - HEADER_SIZE;
	public static final int SIZE_OF_INT_BYTE = 4;
	// Indexes
	public static final int HEADER_INDEX_MESSAGE_TYPE = 0;
	public static final int HEADER_INDEX_DATA_SIZE = 1;
	public static final int HEADER_INDEX_SQN_NUMBER = 5;
	public static final int HEADER_INDEX_ACK_NUMBER = 9;
	// MessageTypes SCCP
	public static final int HEARTBEAT = 0;
	public static final int STARTUP_CONVERSATION = 1;
	public static final int FUNCTION_CALL = 2;
	public static final int RETURN_VALUE = 3;
	// Values
	public static final int MAX_VERSION_NUMBER = 5;
	public static final int QUEUE_CAPACITY = 50;

	// Heartbeat Timer-/Counter-values
	public static final int SLEEP_TIME_MS_HEARTBEAT = 200;
	public static final int MAX_HEARTBEAT_COUNTER = 4;
	public static final int MAX_HEARTBEAT_CONNECTIONLOST = 12;//MAX_HEARTBEAT_COUNTER *3;

	// Nameserver
	// Headervalues for Nameserver communication
	public static final int HEADER_SIZE_NAMESERVER = 4;
	public static final int MAX_PACKET_SIZE_NAMESERVER = HEADER_SIZE_NAMESERVER + 1000;
	// Nameserver Timer-/Counter-values
	public static final int COUNTER_START = 4;
	public static final int SLEEP_TIME_MS_NAMESERVER = 1000;
	// Strings for JSON
	public static final String PARAMETERLIST = "ParameterList";
	public static final String FUNCTIONNAME = "Functionname";
	public static final String IDOFREGISTERED = "id";
	public static final String IPOFREGISTERED = "IpAdress";
	public static final String LISTENINGPORTOFREGISTERED = "Port";
	public static final String RETURNVALUE = "ReturnValue";

	public static final String BACK_FORTH = "backForth";
	public static final String GRIPPER = "gripper";
	public static final String LEFT_RIGHT = "leftRight";
	public static final String UP_DOWN = "upDown";

	// FUNCTIONNAMES
	public static final String REGISTER_RAC_FUNCTION = "registerRac";
	public static final String GETRACLIST_FUNCTION = "getRacList";
	public static final String REGISTER_PHILO_FUNCTION = "registerPhilo";
	public static final String GETPHILOLIST_FUNCTION = "getPhiloList";
	public static final String GETVIEWLIST_FUNCTION = "getViewList";
	public static final String REGISTER_VIEW_FUNCTION = "registerView";

	// Philocontroller
	public static final int WORKPLACE_COUNT = 3;
	public static final int NUMBER_OF_PHILOS = 4;

	// Philo states
	public static final int INIT = 1;
	public static final int SLEEP = 2;
	public static final int GET_RESOURCES = 3;
	public static final int WAIT_LEFT = 4;
	public static final int MOVE_LEFT = 5;
	public static final int WAIT_RIGHT = 6;
	public static final int MOVE_RIGHT = 7;
	public static final int EAT = 8;
	public static final int MOVE_BLOCKED_RIGHT = 9;
	public static final int PUT_RIGHT_RESOURCE_MID = 10;
	public static final int PUT_LEFT_RESOURCE_MID = 11;
	public static final int MOVE_BLOCKED_LEFT = 12;
	// Philonames
	public static final int LEFT_PHILO = 0;
	public static final int OWN_PHILO = 1;
	public static final int RIGHT_PHILO = 2;
	// Philo Sleeptimer
	public static final int MAX_SLEEP_TIME_PHILO = 5000;

	//Stepworker
	public static final int STEPDISTANCE = 4;

	
}
