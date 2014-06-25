
#include <WiFi.h>

#define CMU

#define MAX_CORNER            4
#define PHOTOCELL_THRESOLD    500

//#define RD_PORT      506
#define PORT         507

#define CMD_ERR      -1
#define CMD_MIN      0
#define CMD_POS      0
#define CMD_LOAD     1
#define CMD_INIT     2
#define CMD_RECOVERY 3
#define CMD_TEST     4
#define CMD_MAX      4

// Note that these pins are the analog pins A0-A3, not the digital pins. 
int PosPin[4] = {0, 1, 2, 3};   // Photocell Pin for corners
int PosVal[4] = {0};            // Value of photocell for corners
char curPosStatus = 0;

// Note that these pins are the standard digital pins
int LoadSWPin[4] = {3, 5, 6, 9};   // Switch for corner 1
int LoadVal[4]   = {HIGH};         // State of switch for corners
char curLoadStatus = 0;

#if defined(CMU)
char ssid[] = "CMU";           // The network SSID (name) 
#else
char ssid[] = "Shadyside Inn";
char pass[] = "hotel5405";     // The network Password
#endif

int status = WL_IDLE_STATUS;   // The status of the network connections
//WiFiServer server(RD_PORT);    // The WIFI server to receive command
IPAddress wmServerIP(128,237,121,217); // 128.237.243.167 // 128,237,121,217
WiFiClient wmClient = 0;               // The Client to send status
IPAddress ip;                  // The IP address of the shield
IPAddress subnet;              // The subnet we are connected to
long rssi;                     // The WIFI shield signal strength
byte mac[6];                   // MAC address of the WIFI shield

unsigned long tLastAlive = 0;

char whCmd = CMD_ERR;          // command char from WarehouseManager

void setup(void) 
{
  int i = 0;
  Serial.begin(9600);
  
  for (i=0 ; i < 4 ; i++)
    pinMode(LoadSWPin[i], INPUT);
    
  startWifi();
  
  Serial.print("setup...done\n");
}

void loop(void) 
{
  whCmd = getCmdFromWarehouseManager();
  if (whCmd >= CMD_MIN && whCmd <= CMD_MAX)
  {
    Serial.print("CMD from Warehouse Manager : ");
    Serial.println(whCmd);
    executeCmd(whCmd);
  }
  
  keepConnectToWM();
  
  if (isLoadStatusChanged() == true)
    sendLoadStatusToServer();
  
  if (isPosChanged() == true)
    sendPosToServer();

//================================================
/*  
  readLoad();
  readPos();
  // Sample rate 1 second 
  delay(1000);
*/
}

boolean keepConnectToWM(void)
{
  unsigned long curTime;
  
  if (wmClient.connected() == false)
  {
    Serial.print("\n\nTrying to connect...");
    Serial.println(wmServerIP);
    if (wmClient.connect(wmServerIP, PORT) == false)
    { // connection fail ... retry next time
      wmClient.stop();
      //wmClient = 0;
      return false;
    }
    Serial.print("Connected to Warehouse Manager: ");
    Serial.print(wmServerIP);
    Serial.print(", Port: ");
    Serial.println(PORT, DEC);
  }
  
  curTime = millis();
  if (curTime < (tLastAlive + 1000))
    return true;
  
  wmClient.print("s");
  //wmClient.print(" ... ");
  wmClient.print("\n");
  wmClient.flush();
  
  tLastAlive = millis();
  
  return true;
}

int getCmdFromWarehouseManager()
{
   char cmddata;
   int  cnt = 0, cmd = CMD_ERR;
   
   if (wmClient.connected() == false)
   {
     Serial.print("getCmdFromWarehouseManager: Err\n");
     wmClient.stop();
     return CMD_ERR;
   }
   
   if (wmClient.available())
   {
     cmddata = wmClient.read();
     Serial.print( "Cmd: ");
     Serial.println(cmddata, HEX);
      
     switch (cmddata)
     {
         case 'L':
            cmd = CMD_LOAD;
            break;
         case 'P':
            cmd = CMD_POS;
            break;
         case 'I':
            cmd = CMD_INIT;
            break;
         case 'R':
            cmd = CMD_RECOVERY;
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
   }
   
   return cmd;
}

void executeCmd(int cmd)
{
    switch (cmd)
    {
        case CMD_LOAD:
             Serial.println("Request Load Status");
             sendLoadStatusToServer();
             break;
        case CMD_POS:
             Serial.println("Request Pos Status");
             sendPosToServer();
             break;
        case CMD_INIT:
             Serial.println("Initialize Warehouse");
             resetLoadStatus();
             resetPosStatus();
             break;
        case CMD_RECOVERY:
             Serial.println("Recovery Warehouse");
             recoveryWarehouse();
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
  int i, loadVal = 0;
  boolean ret = false;
  
  Serial.print("sendLoadStatusToServer: ");
  
  if (wmClient.connected() == false)
  {
    wmClient.stop();
    return false;
  }
  
  for (i = MAX_CORNER ; i > 0 ; i--)
  {
    if (LoadVal[(i % MAX_CORNER)] != 0)
    {
      loadVal = i;
      break;
    }
  }
  
  Serial.print("loadVal = ");
  Serial.println(loadVal, DEC);
  
  wmClient.print("L");
  wmClient.print(loadVal, DEC);
  wmClient.print("\n");
  wmClient.flush();

  return true;
}

boolean sendPosToServer(void)
{
  int i, posVal = 0;
  boolean ret = false;
  
  Serial.print("sendPosToServer: ");
  
  if (wmClient.connected() == false)
  {
    wmClient.stop();
    return false;
  }
  
  for (i = MAX_CORNER ; i > 0 ; i--)
  {
    if (LoadVal[(i % MAX_CORNER)] != 0)
    {
      posVal = i;
      break;
    }
  }
  
  Serial.print("posVal = ");
  Serial.println(posVal, DEC);
 
  wmClient.print("P");
  wmClient.print(posVal, DEC);
  wmClient.print("\n");
  wmClient.flush();

  return true;
}

void resetLoadStatus(void)
{
  int i;
  for (i=0 ; i < MAX_CORNER ; i++)
     LoadVal[i] = HIGH;
  curLoadStatus = 0;
}

void resetPosStatus(void)
{
  int i;
  for (i=0 ; i < MAX_CORNER ; i++)
     PosVal[i] = 0;
  curPosStatus = 0;
}

void recoveryWarehouse()
{
}

boolean isLoadStatusChanged(void)
{
  char newLoadStatus;
  newLoadStatus = readLoadStatus();
  
  if (curLoadStatus != newLoadStatus)
  {
    Serial.print("Prev Load = ");
    Serial.print(curLoadStatus, HEX);
    Serial.print(", New Load = ");
    Serial.println(newLoadStatus, HEX);
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
    if (val == LOW)
      LoadVal[i] = 1;
  }
  
  for (i=0 ; i < MAX_CORNER ; i++)
  {
    if (LoadVal[i])
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
    if (val == LOW)
      Serial.println(" -> YES");
    else
      Serial.println(" -> NO");    
  }
}

boolean isPosChanged(void)
{
  char newPosStatus;
  newPosStatus = readPosStatus();
  
  if (curPosStatus != newPosStatus)
  {
    Serial.print("Prev Pos = ");
    Serial.print(curPosStatus, HEX);
    Serial.print(", New Pos = ");
    Serial.println(newPosStatus, HEX);
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
      PosVal[i] = 1;
    }
    else
    {
      PosVal[i] = 0;
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
      Serial.println(" -> YES");
    else
      Serial.println(" -> NO");
  }
}

/************************************************************************************************
* Start Wifi Server
************************************************************************************************/
void startWifi()
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
   Serial.print("PORT for Warehouse : ");
   Serial.print(PORT);
   
   // Start the server and print a message to the terminial.
   //server.begin();
   //Serial.println("The Command Server is started.");
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

