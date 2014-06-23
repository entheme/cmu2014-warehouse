
#include <WiFi.h>

#define CMU

#define MAX_CORNER            4
#define PHOTOCELL_THRESOLD    100
#define RD_PORT               506
#define WR_PORT               507

#define CMD_ERR      -1
#define CMD_MIN      0
#define CMD_POS      0
#define CMD_LOAD     1
#define CMD_RESET    2
#define CMD_TEST     3
#define CMD_MAX      3

// Note that these pins are the analog pins A0-A3, not the digital pins. 
int PosPin[4] = {0, 1, 2, 3};   // Photocell Pin for corners
int PosVal[4] = {0};            // Value of photocell for corners
char curPosStatus = 0;

// Note that these pins are the standard digital pins
int LoadSWPin[4] = {3, 5, 6, 9};   // Switch for corner 1
int LoadVal[4]   = {LOW};            // State of switch for corners
char curLoadStatus = 0;

#if defined(CMU)
char ssid[] = "CMU";           // The network SSID (name) 
#else
char ssid[] = "Shadyside Inn";
char pass[] = "hotel5405";     // The network Password
#endif

int status = WL_IDLE_STATUS;   // The status of the network connections
WiFiServer server(RD_PORT);    // The WIFI server to receive command
IPAddress serverIP(128,237,243,167);
WiFiClient client;             // The Client to send status
IPAddress ip;                  // The IP address of the shield
IPAddress subnet;              // The subnet we are connected to
long rssi;                     // The WIFI shield signal strength
byte mac[6];                   // MAC address of the WIFI shield

char whCmd;                    // command char from WarehouseManager

void setup(void) 
{
  int i = 0;
  Serial.begin(9600);
  
  for (i=0 ; i < 4 ; i++)
    pinMode(LoadSWPin[i], INPUT);
    
  startWifiServer();
  
  Serial.print("setup...done\n");
}

void loop(void) 
{
  whCmd = getCmdFromWarehouseManager();
  if (whCmd >= CMD_MIN && whCmd <= CMD_MAX)
  {
    executeCmd(whCmd);
  }
  
  if (isLoadStatusChanged() == true)
    sendLoadStatusToServer();
  
  if (isPosChanged() == true)
    sendPosToServer();
    
//================================================
  
  readLoad();
  readPos();
  // Sample rate 1 second 
  delay(1000);
}

int getCmdFromWarehouseManager()
{
   char cmddata;
   int  cmd = CMD_ERR;
   // Waiting for a new Client.
   WiFiClient client = server.available();
   
   if (client)
   {
      Serial.print("Welcome Client !!!\n");
      while (client.available() == 0) 
      {
         if (!client.connected())
         {
            client.stop();
            return CMD_ERR;
         }
         delay(10);
      }
      
      cmddata = client.read();
      Serial.print( "Cmd: ");
      Serial.println(cmddata);
      
      switch (cmddata)
      {
         case 'L':
            cmd = CMD_LOAD;
            break;
         case 'P':
            cmd = CMD_POS;
            break;
         case 'R':
            cmd = CMD_RESET;
            break;
         case 'T':
            cmd = CMD_TEST;
            break;
         default:
            Serial.print( "Invalid Command : " );
            Serial.println( cmddata );
            cmd = CMD_ERR;
            break;
      }
      
      client.stop();
   }
   
   return cmd;
}

void executeCmd(int cmd)
{
    switch (cmd)
    {
        case CMD_LOAD:
             sendLoadStatusToServer();
             break;
        case CMD_POS:
             sendPosToServer();
             break;
        case CMD_RESET:
             resetLoadStatus();
             break;
        //------------------------------------
        // Test Cases are below...
        //------------------------------------
        case CMD_TEST:
             Serial.println("Test command...");
             break;
        default: // If we don't know what the command is, we just say so and exit.
             Serial.print("Invalid Command : ");
             Serial.println(cmd);
    }
}

boolean sendLoadStatusToServer(void)
{
  boolean ret = false;
  
  Serial.print("\nAttempting to connect to Warehouse Manager...");
  Serial.println(serverIP);
  if (client.connect(serverIP, WD_PORT)) 
  {
      Serial.println("connected");
      client.write("L");
      client.write(curLoadStatus);
      client.flush();
      ret = true;
  }
  return ret;
}

boolean sendPosToServer(void)
{
  boolean ret = false;
  
  Serial.print("\nAttempting to connect to Warehouse Manager...");
  Serial.println(serverIP);
  if (client.connect(serverIP, WD_PORT)) 
  {
      Serial.println("connected");
      client.write("P");
      client.write(curPosStatus);
      client.flush();
      ret = true;
  }
  return ret;
}

void resetLoadStatus(void)
{
  int i;
  for (i=0 ; i < MAX_CORNER ; i++)
     LoadVal[i] = LOW;
  
  curLoadStatus = 0;
}

boolean isLoadStatusChanged(void)
{
  char newLoadStatus;
  newLoadStatus = readLoadStatus();
  
  if (curLoadStatus != newLoadStatus)
  {
    curLoadStatus = newLoadStatus;
    return true;
  }

  return false;
}

char readLoadStatus(void)
{
  int i, val;
  char ret = 0;
  for (i=0 ; i < MAX_CORNER ; i++)
  {
    val = digitalRead(LoadSWPin[i]);
    if (val == HIGH)
      LoadVal[i] = HIGH;
  }
  
  for (i=0 ; i < MAX_CORNER ; i++)
  {
    if (LoadVal[i] == HIGH)
      ret |= (0x1 << i);
  }

  return ret;
}

// Test Function
void readLoad(void)
{
  int i, val;
  for (i=0 ; i < MAX_CORNER ; i++)
  {
    val = digitalRead(LoadSWPin[i]);
    Serial.print("SW Pin ");
    Serial.print(i);
    Serial.print(" = ");
    Serial.print(val);
    if (val == HIGH)
      Serial.println(" -> HIGH");
    else
      Serial.println(" -> LOW");    
  }
}

boolean isPosChanged(void)
{
  char newPosStatus;
  newPosStatus = readPosStatus();
  
  if (curPosStatus != newPosStatus)
  {
    curPosStatus = newPosStatus;
    return true;
  }

  return false;
}

char readPosStatus(void)
{
  int i, val;
  char ret = 0;
  for (i=0 ; i < MAX_CORNER ; i++)
  {
    val = analogRead(PosPin[i]);
    if (val < PHOTOCELL_THRESOLD)
    {
      PosVal[i] = 0;
    }
    else
    {
      PosVal[i] = 1;
    }
  }
  
  for (i=0 ; i < MAX_CORNER ; i++)
  {
    if (PosVal[i])
      ret |= (0x1 << i);
  }

  return ret;
}

// Test Function
void readPos(void)
{
  int i, val;
  for (i=0 ; i < MAX_CORNER ; i++)
  {
    val = analogRead(PosPin[i]);
    Serial.print("Pos ");
    Serial.print(i);
    Serial.print(" = ");
    Serial.print(val);

    if (val < PHOTOCELL_THRESOLD)
      Serial.println(" -> NO");
    else
      Serial.println(" -> YES");
  }
}

/************************************************************************************************
* Start Wifi Server
************************************************************************************************/
void startWifiServer()
{
   // Attempt to connect to Wifi network indicated by SSID.
   while ( status != WL_CONNECTED) 
   {
     #if 0
     Serial.print("Attempting to connect to SSID: ");
     #else
     Serial.print("####### SSID: ");
     #endif
     Serial.println(ssid);
     #if defined(CMU)
     status = WiFi.begin(ssid);
     #else
     status = WiFi.begin(ssid, pass);
     #endif
   }  
   
   // Print connection information to the debug terminal
   printConnectionStatus();
   
   // Start the server and print a message to the terminial.
   server.begin();
   Serial.println("The Command Server is started.");
   Serial.print("--------------------------------------\n\n");
}

void printConnectionStatus() 
 {
     // Print the basic connection and network information: Network, IP, and Subnet mask
     ip = WiFi.localIP();
     Serial.print("Connected to ");
     Serial.print(ssid);
     Serial.print(" IP Address:: ");
     Serial.println(ip);
     subnet = WiFi.subnetMask();
     Serial.print("Netmask: ");
     Serial.println(subnet);
   
     // Print our MAC address.
     WiFi.macAddress(mac);
     Serial.print("WiFi Shield MAC address: ");
     Serial.print(mac[5],HEX);
     Serial.print(":");
     Serial.print(mac[4],HEX);
     Serial.print(":");
     Serial.print(mac[3],HEX);
     Serial.print(":");
     Serial.print(mac[2],HEX);
     Serial.print(":");
     Serial.print(mac[1],HEX);
     Serial.print(":");
     Serial.println(mac[0],HEX);
   
     // Print the wireless signal strength:
     rssi = WiFi.RSSI();
     Serial.print("Signal strength (RSSI): ");
     Serial.print(rssi);
     Serial.println(" dBm");

 } // printConnectionStatus

